package com.az.cdms_mobile.Repository;

import android.app.Activity;

import com.az.cdms_mobile.Models.Fluid;
import com.az.cdms_mobile.Models.Request;
import com.az.cdms_mobile.Models.Test;
import com.az.cdms_mobile.Models.TestResult;
import com.az.cdms_mobile.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestRepositoryHelper {
    private static final int MIN_REQUESTS = 1;
    private static final int MAX_REQUESTS = 40;
    private static final int MIN_TESTS =1;
    private static final int MAX_TESTS =10;
    private static final int MAX_DAYS_BETWEEN_REQUESTS=60;
    private static final int DAYS_READY=5;


    private static List<CdmsData> data;
    public static List<Request> Generate(Activity context) {
        data = GetCdmsData(context);
        List<Request> result = new ArrayList<>();
        int requestsCount = getRequestsCount(MIN_REQUESTS,MAX_REQUESTS);
        Date[] liquidDates = generateLiquidDates(requestsCount);
        for (int i=0; i < requestsCount; i++) {
            Request r = new Request();
          //  r.FluidType = getFluidType();
          //  r.FluidDate = liquidDates[i];
            r.Number = i;
            //r.ReadyDate = getReadyDate(r.FluidDate);
            r.Tests = generateTests(r);
            if (r.Tests.size()!=0) {
                result.add(r);
            } else {
                i--;
            }
        }
        return result;
    }

    private static List<Test> generateTests(Request r) {
        int testsCount = getRequestsCount(MIN_TESTS,MAX_TESTS);
        List<Test> result = new ArrayList<>();
        List<CdmsData> testrecords = getTestRecords(Fluid.Blood);
        testsCount = Math.min(testsCount,testrecords.size());
        for (int i=0;i<testsCount;i++){
            Test t = new Test();

                CdmsData testrecord = GetTestRecord(testrecords);

                    t.Fluid = r.FluidType;
                    t.FluidDate = r.FluidDate;
                    t.Name = testrecord.description;
                    t.LowerLimit = testrecord.rangeManL;
                    t.UpperLimit = testrecord.rangeManH;
                    t.Unit = testrecord.unit;
                    //t.ReadyDate = getReadyDate(t);
                    if (t.ReadyDate != null)
                        SetResult(t);
                    result.add(t);

            }

        return result;
    }

    private static List<CdmsData> getTestRecords(Fluid fluidType) {
        List<CdmsData> result = new ArrayList<>();
        for (int i=0;i<data.size();i++){
            if (data.get(i).fluid.equals(fluidType.toString())){
                result.add(data.get(i));
            }
        }
        return result;
    }

    private static CdmsData GetTestRecord(List<CdmsData> records) {
synchronized (records) {
    int testNumber = (int) (Math.random() * records.size());

    Gson gson = new Gson();
    String json = gson.toJson(records.get(testNumber));
    CdmsData record = gson.fromJson(json, CdmsData.class);
    records.remove(testNumber);
    return record;
}

    }

    private static Date getReadyDate(Test t) {
        Calendar c = Calendar.getInstance();
       // c.setTime(t.FluidDate);
        c.add(Calendar.DATE,(int)(Math.random()*DAYS_READY));
        return c.getTime().after(new Date())?null:c.getTime();
    }

    private static void SetResult(Test t) {
        if (t.UpperLimit == null || t.LowerLimit == null)
        {
            t.Value = null;
           // t.Result = TestResult.Unknown;
            return;
        }
        t.Value = Math.max(0,generateTestValue(t));
      //  t.Result = t.Value>t.UpperLimit || t.Value<t.LowerLimit? TestResult.Failed:TestResult.Passed;
    }

    private static Double generateTestValue(Test t) {
        int overLimitPercentage = 50;
        Double overLimitValue = (t.UpperLimit - t.LowerLimit)*overLimitPercentage/100;
        Double MaxOffset = t.UpperLimit - t.LowerLimit+ overLimitValue + overLimitValue;
        return t.LowerLimit -overLimitValue/2 +
                Math.random()*MaxOffset;

    }

//    private static Date getReadyDate(Date fluidDate) {
//        Calendar c = Calendar.getInstance();
//        c.setTime(fluidDate);
//        c.add(Calendar.DATE,(int)(Math.random()*DAYS_READY));
//        return c.getTime();
//    }

    private static Date[] generateLiquidDates(int requestsCount) {
        Date[] result = new Date[requestsCount];
        Calendar c = Calendar.getInstance();
c.setTime(new Date());
        for (int i=0;i<requestsCount;i++){
            int daysBetweenRequests = (int)(Math.random()*MAX_DAYS_BETWEEN_REQUESTS);
            c.add(Calendar.DATE,-daysBetweenRequests);
            result[i]=c.getTime();
        }
        return result;
    }

    private static Fluid getFluidType() {
        int fluidsCount = Fluid.values().length;
        int fluid = (int)(Math.random()*fluidsCount);
        return Fluid.values()[fluid];
    }

    private static int getRequestsCount(int minRequests, int maxRequests) {
        return (int)( minRequests + (Math.random()*(maxRequests-minRequests)));
    }

    public static List<CdmsData> GetCdmsData(Activity context)  {
    if (data == null) {
        InputStream in = context.getResources().openRawResource(R.raw.cdmsdata);

        JsonReader reader = new JsonReader(new InputStreamReader(in));
        Type listType = new TypeToken<ArrayList<CdmsData>>() {
        }.getType();
        data = new Gson().fromJson(reader, listType);

    }
    return data;

    }
}
