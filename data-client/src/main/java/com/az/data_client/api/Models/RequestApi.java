package com.az.data_client.api.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestApi {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("number")
    @Expose
    public Integer number;
    @SerializedName("fluidDate")
    @Expose
    public String fluidDate;
    @SerializedName("readyDate")
    @Expose
    public String readyDate;
    @SerializedName("fluidType")
    @Expose
    public String fluidType;
    @SerializedName("tests")
    @Expose
    public List<TestApi> tests = null;
}

