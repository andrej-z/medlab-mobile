package com.az.data_client.db.dbModels;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.az.data_client.db.dbConverters.DateConverter;

import org.joda.time.DateTime;

@Entity(tableName = "requests")
@TypeConverters(DateConverter.class)
public class dbRequest {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int ext_id;
    public int number;
    public DateTime fluid_date;
    public DateTime ready_date;
    public String fluid_type;

}
