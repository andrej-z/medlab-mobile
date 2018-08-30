package com.az.data_client.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.az.data_client.db.dbModels.dbUserInfo;

import io.reactivex.Maybe;


@Dao
public interface UserInfoDao {
    @Query("SELECT * FROM user_info ORDER BY id DESC LIMIT 1")
    Maybe<dbUserInfo> getUserInfo();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(dbUserInfo entity);

    @Update
    void update(dbUserInfo entity);

    @Delete
    void deleteLogRecord(dbUserInfo entity);
}
