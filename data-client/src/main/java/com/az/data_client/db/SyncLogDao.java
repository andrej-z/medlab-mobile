package com.az.data_client.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.az.data_client.db.dbModels.dbSyncLog;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Dao
public interface SyncLogDao {
    @Query("SELECT * FROM sync_log")
    Flowable<List<dbSyncLog>> getLogRecords();



    @Query("SELECT * FROM sync_log  ORDER BY id desc LIMIT 1")
    Maybe<dbSyncLog> getLastLogRecord();
    @Query("SELECT * FROM sync_log WHERE success =1 ORDER BY id desc LIMIT 1")
    Maybe<dbSyncLog> getLastSuccessLogRecord();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLogRecord(dbSyncLog entity);

    @Update
    void updateLogRecord(dbSyncLog entity);

    @Delete
    void deleteLogRecord(dbSyncLog entity);
}
