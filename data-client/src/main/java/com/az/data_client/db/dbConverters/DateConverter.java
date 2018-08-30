package com.az.data_client.db.dbConverters;

import android.arch.persistence.room.TypeConverter;

import org.joda.time.DateTime;

public class DateConverter {
    @TypeConverter
    public static DateTime toDate(Long dateLong){
        return dateLong == null ? null: new DateTime(dateLong);
    }

    @TypeConverter
    public static Long fromDate(DateTime date){
        return date == null ? null :date.getMillis();
    }
}
