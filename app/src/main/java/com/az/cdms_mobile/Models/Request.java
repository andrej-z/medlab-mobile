package com.az.cdms_mobile.Models;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

public class Request {
    public int Id;
    public int Number;
    public DateTime FluidDate;
    public DateTime ReadyDate;
    public String FluidType;
    public List<Test> Tests;
}
