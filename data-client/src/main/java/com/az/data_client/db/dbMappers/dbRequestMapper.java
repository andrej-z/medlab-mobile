package com.az.data_client.db.dbMappers;

import android.support.annotation.NonNull;

import com.az.data_client.db.dbModels.dbRequest;
import com.az.data_client.db.dbModels.dbRequestWithTests;
import com.az.data_client.db.dbModels.dbTest;
import com.az.data_client.domain.Models.Tests.DataRequest;
import com.az.data_client.domain.Models.Tests.DataTest;


import java.util.ArrayList;
import java.util.List;



public class dbRequestMapper {
    public static dbRequestWithTests MapRequestToDb(DataRequest source){
        dbRequestWithTests result = new dbRequestWithTests();
        result.request = new dbRequest();
        result.tests = new ArrayList<>();
        result.request.ext_id = source.Id;
        result.request.fluid_date = source.FluidDate;
        result.request.fluid_type = source.FluidType;
        result.request.number = source.Number;
        result.request.ready_date = source.ReadyDate;
        for (DataTest t:source.Tests){
            dbTest test = MapTestToDb(t);

            result.tests.add(test);
        }
        return result;
    }

    @NonNull
    public static dbTest MapTestToDb(DataTest t) {
        dbTest test = new dbTest();
        test.ext_id = t.Id;
        test.fluid = t.Fluid;
        test.fluid_date = t.FluidDate;
        test.name = t.Name;
        test.low_limit = t.LowerLimit;
        test.up_limit = t.UpperLimit;
        test.ready_date = t.ReadyDate;
        test.unit = t.Unit;
        test.value = t.Value;
        return test;
    }

    public static DataRequest MapRequestFromDb(dbRequestWithTests source){
        DataRequest result = new DataRequest();
        result.Id = source.request.ext_id;
        result.Tests = new ArrayList<>();
        result.FluidDate = source.request.fluid_date ;
        result.FluidType =source.request.fluid_type ;
        result.Number =source.request.number ;
        result.ReadyDate =source.request.ready_date ;
        for (dbTest t:source.tests){
            DataTest test = MapTestFromDb(t);

            result.Tests.add(test);
        }
        return result;
    }

    @NonNull
    public static DataTest MapTestFromDb(dbTest t) {
        DataTest test = new DataTest();
        test.Id = t.ext_id;
        test.Fluid = t.fluid;
        test.FluidDate = t.fluid_date;
        test.Name = t.name;
        test.LowerLimit = t.low_limit;
        test.UpperLimit = t.up_limit;
        test.ReadyDate = t.ready_date;
        test.Unit = t.unit;
        test.Value = t.value;
        return test;
    }

    public static List<DataRequest> MapRequestsFromDb(List<dbRequestWithTests> source){
        List<DataRequest> result = new ArrayList<>();

        for (dbRequestWithTests r:source){
            result.add(MapRequestFromDb(r));
        }
        return result;
    }


}
