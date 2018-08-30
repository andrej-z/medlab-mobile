package com.az.data_client.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.az.data_client.db.dbModels.dbRequest;
import com.az.data_client.db.dbModels.dbRequestWithTests;
import com.az.data_client.db.dbModels.dbTest;

import java.util.List;


import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface CdmsDao {
    @Query("SELECT * FROM requests")
    Single<List<dbRequest>> getRequests();

    @Query("SELECT * FROM requests")
    Single<List<dbRequestWithTests>> getRequestsWithTests();

    @Query("SELECT * FROM requests  WHERE ext_id = :id LIMIT 1")
    Maybe<dbRequestWithTests> getRequestById(int id);

    @Query("SELECT id FROM requests  WHERE ext_id = :id LIMIT 1")
    Long getRequestId(int id);

    @Query("SELECT id FROM tests  WHERE ext_id = :id LIMIT 1")
    Long getTestId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRequest(dbRequest entity);

    @Update
    void updateRequest(dbRequest entity);

    @Delete
    void deleteRequest(dbRequest entity);

    @Query("SELECT * FROM tests")
    Single<List<dbTest>> getTests();

    @Query("SELECT * FROM tests  WHERE ext_id = :id")
    Maybe<dbTest> getTestById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTest(dbTest entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTest(List<dbTest> entities);

    @Update
    void updateTest(dbTest entity);

    @Delete
    void deleteTest(dbTest entity);

}
