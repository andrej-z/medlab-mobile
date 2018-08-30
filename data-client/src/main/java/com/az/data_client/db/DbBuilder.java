package com.az.data_client.db;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DbBuilder {
    private CdmsDb database;
    public DbBuilder(Context context) {
        database = Room.databaseBuilder(context, CdmsDb.class,"cdms").build();
    }

    public CdmsDb getDatabase() {
        return database;
    }
}
