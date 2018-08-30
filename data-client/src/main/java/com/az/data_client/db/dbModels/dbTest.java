package com.az.data_client.db.dbModels;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.az.data_client.db.dbConverters.DateConverter;

import org.joda.time.DateTime;


@Entity(tableName = "tests")
@TypeConverters(DateConverter.class)
public class dbTest {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public int fk_id_request;
    public int ext_id;
    public DateTime fluid_date;
    public DateTime ready_date;
    public String fluid;
    public String name;
    public Double value;
    public String unit;
    public Double low_limit;
    public Double up_limit;
}
