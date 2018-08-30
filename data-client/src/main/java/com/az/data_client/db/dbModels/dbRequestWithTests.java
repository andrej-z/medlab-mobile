package com.az.data_client.db.dbModels;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class dbRequestWithTests {
    @Embedded
    public dbRequest request;
    @Relation(parentColumn = "id", entityColumn = "fk_id_request", entity = dbTest.class)
    public List<dbTest> tests;
}
