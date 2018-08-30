package com.az.data_client.domain.Models.Tests;

import org.joda.time.DateTime;

import java.util.List;

public class DataRequest {
    public int Id;
    public int Number;
    public DateTime FluidDate;
    public DateTime ReadyDate;
    public String FluidType;
    public List<DataTest> Tests;
}
