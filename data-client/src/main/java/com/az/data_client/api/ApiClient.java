package com.az.data_client.api;

import com.az.data_client.api.Models.LoginApiRequest;
import com.az.data_client.api.Models.RequestsApiResponce;
import com.az.data_client.domain.AccountState;
import com.az.data_client.domain.Constants;
import com.az.data_client.domain.CredentialStorage;
import com.az.data_client.domain.Models.LoginResponse;
import com.az.data_client.domain.Models.MobileRegistrationResult;
import com.az.data_client.domain.Models.Ping;
import com.az.data_client.domain.Models.RegistrationResult;
import com.az.data_client.domain.Models.UserInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    private Retrofit.Builder retrofitBuilder;
    OkHttpClient okHttpClient;
    Retrofit retrofit;
    Api api;
    private CredentialStorage storage;

    public ApiClient(CredentialStorage storage) {
        //create new gson which serialize nulls.
        this.storage = storage;
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();
        okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor(new AddCookiesInterceptor(storage))
                .addInterceptor(new ReceivedCookiesInterceptor(storage))
                .build();

         //create retrofit builder to start connection.
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .build();
        api = retrofit.create(Api.class);
    }

    public Api getApi() {
        return api;
    }

    //create requestbody which contains all ele you want to send to server.
    public RequestBody doCreateRequest(Object request) {
        Gson gson = new Gson();
        // Object object = new Object();
        String requestStr = gson.toJson(request);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestStr);
        return requestBody;
    }

    //parse response to a class.
    public <T> T parseResponse(String response, Class<T> typeClass) {
        String result = response;
        T t = new Gson().fromJson(result, typeClass);
        return t;
    }

    public Disposable getPing(final CallBack<Ping> callback)
    {
        Disposable disposable = api.getPing()
                .map(x ->{
                    if (x == null)
                        return null;
                    return new Ping(x);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback.onSuccess,callback.onException);
        return disposable;
    }


    public Disposable register(String googleAuthToken, final CallBack<MobileRegistrationResult> result) {
        Disposable disposable = api.register("Bearer "+googleAuthToken)
                .map(x -> {
                    MobileRegistrationResult registrationResult = new MobileRegistrationResult(x);
                    if (registrationResult.result == RegistrationResult.Success){
                        storage.UpdateCredentials(registrationResult);
                    }
                    return registrationResult;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result.onSuccess, result.onException);
        return disposable;
    }

    public Disposable login(LoginApiRequest request, final CallBack<LoginResponse> callback) {

        Disposable disposable = api.login(doCreateRequest(request))
                .map(x -> {return new LoginResponse(x);})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback.onSuccess,callback.onException);
        return disposable;
    }

    public Maybe<LoginResponse> login_(LoginApiRequest request) {

        Maybe<LoginResponse> r = api.login(doCreateRequest(request))
                .map(x -> {return new LoginResponse(x);});

        return r;
    }

    public Disposable getUserInfo(final CallBack<UserInfo> callback)
    {
        Disposable disposable = api.getUserInfo()
                .map(x ->{ return  new UserInfo(x);})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback.onSuccess,callback.onException);
        return disposable;
    }

    public Disposable getRequests(int recordId, final CallBack<RequestsApiResponce> callback){
        Disposable disposable = api.getRequests(recordId)

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(callback.onSuccess, callback.onException);
        return disposable;
    }

    public class ReceivedCookiesInterceptor implements Interceptor {
        private CredentialStorage storage;

        public ReceivedCookiesInterceptor(CredentialStorage storage) {
            this.storage = storage;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = new HashSet<>();

                for (String header : originalResponse.headers("Set-Cookie")) {
                    storage.UpdateSessionKey(header);
                }


            }

            return originalResponse;
        }
    }

    public class AddCookiesInterceptor implements Interceptor {
        private CredentialStorage storage;

        public AddCookiesInterceptor(CredentialStorage storage) {
            this.storage = storage;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            //HashSet<String> preferences = (HashSet) Preferences.getDefaultPreferences().getStringSet(Preferences.PREF_COOKIES, new HashSet<>());
            if (storage.getState() == AccountState.LoggedIn) {
                // for (String cookie : preferences) {
                builder.addHeader("Cookie", storage.SessionKey);
                //Log.v("OkHttp", "Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
                // }
            }
            return chain.proceed(builder.build());
        }
    }

    public class OnErrorInterceptor implements Interceptor{
        private CredentialStorage storage;

        public OnErrorInterceptor(CredentialStorage storage) {
            this.storage = storage;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response responce = chain.proceed(chain.request());

            if (!responce.isSuccessful() && responce.code() == 401 && storage.getState() == AccountState.LoggedIn)
            {
                storage.UpdateSessionKey("");
            }
            return responce;
        }
    }
}
