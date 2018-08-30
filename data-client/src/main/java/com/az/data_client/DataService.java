package com.az.data_client;

import android.content.Context;

import com.az.data_client.api.ApiClient;
import com.az.data_client.api.CallBack;
import com.az.data_client.api.Mappers.RequestMapper;
import com.az.data_client.api.Models.LoginApiRequest;
import com.az.data_client.db.CdmsDb;
import com.az.data_client.db.DbBuilder;
import com.az.data_client.db.dbClient;
import com.az.data_client.db.dbMappers.dbRequestMapper;
import com.az.data_client.db.dbMappers.dbUserInfoMapper;
import com.az.data_client.db.dbModels.dbSyncLog;
import com.az.data_client.db.dbModels.dbUserInfo;
import com.az.data_client.domain.AccountState;
import com.az.data_client.domain.CredentialStorage;
import com.az.data_client.domain.Models.MobileLoginResult;
import com.az.data_client.domain.Models.MobileRegistrationResult;
import com.az.data_client.domain.Models.RegistrationResult;
import com.az.data_client.domain.Models.Tests.DataRequest;
import com.az.data_client.domain.Models.Tests.DataTest;

import com.az.data_client.domain.Models.UserInfo;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class DataService {
    private List<DataRequest> requests;
    private Context mContext;
    private CredentialStorage storage;
    private ApiClient apiClient;
    private CdmsDb db;
    private int lastSyncRecord;
    private UserInfo userInfo;
    private dbClient dbClient;
    private boolean initialized;

    public DataService(Context context) {
        mContext = context;

    }

    public void init(CallBack<List<DataRequest>> requestsCallback, CallBack<UserInfo> userInfoCallback) {
        Action action = () -> {
            storage = new CredentialStorage(mContext);
            apiClient = new ApiClient(storage);
            db = new DbBuilder(mContext).getDatabase();
            requests = new ArrayList<>();
            dbClient = new dbClient(mContext);

            db.syncLogDao().getLastSuccessLogRecord()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((x)->{
                        if (x != null) {
                            lastSyncRecord = x.last_log_record;
                        }
                    });

             db.userInfoDao().getUserInfo()
                     .map(x -> new UserInfo(x))
                     .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(userInfoCallback.onSuccess,userInfoCallback.onException);

        };
        Completable.fromAction(action)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    db.cdmsDao().getRequestsWithTests()
                            .toObservable()
                            .flatMapIterable(x -> dbRequestMapper.MapRequestsFromDb(x))
                            .toList()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSuccess((x)->{requests=x; initialized = true;})
                            .subscribe(requestsCallback.onSuccess,requestsCallback.onException);
                }).subscribe();

    }

    public  boolean getInitialized(){
        return initialized;
    }

    public AccountState getAccountState(){
        return storage.getState();
    }
    public void updateFromApi(CallBack<List<DataRequest>> callback) {
        AuthorizeRequest(()->{
            apiClient.getApi().getRequests(lastSyncRecord)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess( s -> {lastSyncRecord = s.lastLogRecordId;})
                    .map(s -> {return  RequestMapper.Map(s.requests);})
                    .map(s ->{return updateRequests(s);})
                    .subscribe(callback.onSuccess,callback.onException);
        },callback);

    }

    public void getUserInfo(CallBack<UserInfo> callback) {
        AuthorizeRequest(()-> {
            apiClient.getApi().getUserInfo()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(s ->{
                        UserInfo u =  new UserInfo(s);
                        saveUserInfo(u);
                        return u;
                    })

                    .subscribe(callback.onSuccess,callback.onException);
            },callback);

    }


    public void register(String googleAuthToken, final CallBack<MobileRegistrationResult> callback) {

        apiClient.register(googleAuthToken, callback);
    }

    private void saveUserInfo(UserInfo data) {
        dbUserInfo updateUserInfo = dbUserInfoMapper.MapUserToDb(data);
        db.userInfoDao().getUserInfo().subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread()).subscribe(new Consumer<dbUserInfo>() {
            @Override
            public void accept(dbUserInfo dbUserInfo) throws Exception {
                if (dbUserInfo != null) {
                    updateUserInfo.id = dbUserInfo.id;
                    db.userInfoDao().update(updateUserInfo);

                } else {
                    db.userInfoDao().insert(updateUserInfo);
                }
            }
        });
    }

    private List<DataRequest> updateRequests(List<DataRequest> data) {
        int testsUpdate = 0;
        HashSet<DataRequest> changedRequests = new HashSet<>();
        for (DataRequest r : data) {
            DataRequest similarRequest = findRequest(r);

            if (similarRequest ==null){
                similarRequest = new DataRequest();
                similarRequest.FluidDate = r.FluidDate;
                similarRequest.FluidType = r.FluidType;
                similarRequest.Id = r.Id;
                similarRequest.Tests = new ArrayList<>();
                requests.add(similarRequest);
            }

            for (DataTest t : r.Tests) {
                DataTest sourceTest = findTest(t);
                if (sourceTest != null) {
                    updateTest(t, sourceTest);
                    final DataRequest sourceRequest = getTestRequest(sourceTest);
                    if (sourceRequest != similarRequest) {
                        sourceRequest.Tests.remove(sourceTest);
                        similarRequest.Tests.add(sourceTest);
                        changedRequests.add(sourceRequest);
                     } else {
                        changedRequests.add(similarRequest);
                    }
                } else {
                    sourceTest = new DataTest();
                    updateTest(t, sourceTest);
                    similarRequest.Tests.add(sourceTest);
                    changedRequests.add(similarRequest);
                    }
                testsUpdate++;
            }
            dbClient.SaveRequest(similarRequest);

        }
        cleanEmptyRequests();
        if (testsUpdate > 0) {
            final int testsCount = testsUpdate;
            Completable.fromAction(() -> {
                saveSyncLog(testsCount, true);
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe();

        }

        List<DataRequest> result = new ArrayList<>();
        for (DataRequest r:changedRequests)
            result.add(r);
        return  result;


    }

    private void cleanEmptyRequests() {
        for (int i=0;i<requests.size();i++){
            if (requests.get(i).Tests.size()==0) {
                requests.remove(i);
                i--;
            }
        }
    }


    private void updateTest(DataTest t, DataTest sourceTest) {
        sourceTest.Fluid = t.Fluid;
        sourceTest.FluidDate = t.FluidDate;
        sourceTest.LowerLimit = t.LowerLimit;
        sourceTest.UpperLimit = t.UpperLimit;
        sourceTest.Name = t.Name;
        sourceTest.ReadyDate = t.ReadyDate;
        sourceTest.Result = t.Result;
        sourceTest.Unit = t.Unit;
        sourceTest.Value = t.Value;
        sourceTest.Id = t.Id;
    }

    private DataRequest findRequest(DataRequest request) {
        for (DataRequest r : requests) {
            if (requestsEqual(request, r))
                return r;
        }
        return null;
    }

    private boolean requestsEqual(DataRequest sourceRequest, DataRequest r) {
        if (!r.FluidType.equals(sourceRequest.FluidType))
            return false;
        if (r.Id != sourceRequest.Id)
            return false;
        return true;
    }

    private DataRequest getTestRequest(DataTest sourceTest) {
        for (DataRequest r : requests) {
            for (DataTest test : r.Tests) {
                if (test.Id == sourceTest.Id)
                    return r;

            }
        }
        return null;
    }

    private DataTest findTest(DataTest t) {
        for (DataRequest r : requests) {
            for (DataTest test : r.Tests) {
                if (test.Id == t.Id)
                    return test;

            }
        }
        return null;
    }

    private void saveSyncLog(Integer recordsCount, boolean success) {
        dbSyncLog record = new dbSyncLog();
        record.sync_date = DateTime.now();
        record.tests_count = recordsCount;
        record.success = success;
        record.last_log_record = lastSyncRecord;
        db.syncLogDao().insertLogRecord(record);

    }

    private void AuthorizeRequest(Action action, CallBack callback){
        if (storage.getState() == AccountState.LoggedIn) {
            apiClient.getApi().getPing()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((x)->{
                        RunRequest(action,callback);
                    },callback.onException);


        } else {
            apiClient.getApi().getPing()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((x)->{
                        apiClient.login_(new LoginApiRequest(storage.Login, storage.Password))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe((s) ->{
                                    if (s.Result == MobileLoginResult.Success) {
                                        RunRequest(action,callback);
                                    }
                                },callback.onException);

                    },callback.onException);
        }
    }

    private void RunRequest(Action action, CallBack callback){
        Completable.fromAction(action)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->{}, (t) ->{
                    if (t instanceof HttpException && ((HttpException)t).code() == 401 && storage.getState() == AccountState.LoggedIn)
                    {
                        storage.UpdateSessionKey("");
                        AuthorizeRequest(action, callback);

                    }
                });
    }

    public void resetCredentials() {
        MobileRegistrationResult fakeResult = new MobileRegistrationResult();
        fakeResult.result = RegistrationResult.Success;
        storage.UpdateCredentials(fakeResult);

    }
}



