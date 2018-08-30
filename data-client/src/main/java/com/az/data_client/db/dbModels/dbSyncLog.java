package com.az.data_client.db.dbModels;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.az.data_client.db.dbConverters.DateConverter;

import org.joda.time.DateTime;


@Entity(tableName = "sync_log")
@TypeConverters(DateConverter.class)
public class dbSyncLog {
    @PrimaryKey(autoGenerate = true)
    public Integer id;
    public DateTime sync_date;
    public Integer tests_count;
    public int last_log_record;
    public boolean success;
}
