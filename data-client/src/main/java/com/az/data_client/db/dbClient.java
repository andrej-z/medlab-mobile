package com.az.data_client.db;

import android.content.Context;
import android.util.Log;

import com.az.data_client.db.dbMappers.dbRequestMapper;
import com.az.data_client.db.dbModels.dbRequestWithTests;
import com.az.data_client.db.dbModels.dbTest;
import com.az.data_client.domain.Models.Tests.DataRequest;
import com.az.data_client.domain.Models.Tests.DataTest;



import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class dbClient {
    private CdmsDb db;
    public dbClient(Context mContext){
        db = new DbBuilder(mContext).getDatabase();
    }

    public void SaveRequest(DataRequest r){

        Completable.fromAction(()->{
            dbRequestWithTests dbRequest = dbRequestMapper.MapRequestToDb(r);
            Long requestId = db.cdmsDao().getRequestId(r.Id);


            if (requestId == null) {
                requestId = db.cdmsDao().insertRequest(dbRequest.request);
            } else{
                dbRequest.request.id = requestId.intValue();
                db.cdmsDao().updateRequest(dbRequest.request);
            }
            for (DataTest t : r.Tests){

                    final dbTest dbtest = dbRequestMapper.MapTestToDb(t);
                    dbtest.fk_id_request = requestId.intValue();
                    Long testId = db.cdmsDao().getTestId(t.Id);
                    if (testId == null) {
                        db.cdmsDao().insertTest(dbtest);
                    } else {

                        dbtest.id = testId.intValue();
                        db.cdmsDao().updateTest(dbtest);
                    }

            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public void saveTest(DataRequest r, int testId){

    }
}
