package com.az.cdms_mobile.Models;

import android.content.Context;
import android.content.Intent;

import com.az.cdms_mobile.Views.RequestsActivity;
import com.az.cdms_mobile.Views.TestDetailActivity;
import com.az.cdms_mobile.Views.TestsChartActivity;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

public class Test {
    public int Id;
    public DateTime FluidDate;
    public DateTime ReadyDate;
    public String Fluid;
    public String Name;
    public Double Value;
    public String Unit;
    public Double LowerLimit;
    public Double UpperLimit;
    public TestResult getResult(){
        if (Value == null)
            return TestResult.Unknown;
        if (LowerLimit == null && UpperLimit == null)
            return TestResult.Passed;
        if (Value>LowerLimit && Value<UpperLimit)
            return TestResult.Passed;
        return TestResult.Failed;

    }
public boolean HistoryChartAvailable;



}
