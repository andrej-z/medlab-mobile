package com.az.data_client.db;

import android.arch.persistence.room.RoomDatabase;

import com.az.data_client.db.dbModels.dbRequest;
import com.az.data_client.db.dbModels.dbSyncLog;
import com.az.data_client.db.dbModels.dbTest;
import com.az.data_client.db.dbModels.dbUserInfo;


@android.arch.persistence.room.Database(entities = {
        dbRequest.class,
        dbTest.class,
        dbUserInfo.class,
        dbSyncLog.class
    }, version = 1)
public abstract class CdmsDb extends RoomDatabase{


        public abstract CdmsDao cdmsDao();
        public abstract SyncLogDao syncLogDao();
    public abstract UserInfoDao userInfoDao();
}
