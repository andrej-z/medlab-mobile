package com.az.data_client.db.dbModels;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.az.data_client.db.dbConverters.DateConverter;

import org.joda.time.DateTime;


@Entity(tableName = "user_info")
@TypeConverters(DateConverter.class)
public class dbUserInfo {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int ext_id ;
    public String pid ;
    public String first_name ;
    public String last_naame ;
    public DateTime birth_date ;
    public Integer sex ;
    public String phone ;
    public String email ;
    public String address ;
    public String city ;
    public String postal_code ;
    public String country ;
    public String comment ;
    public boolean deleted ;
}
