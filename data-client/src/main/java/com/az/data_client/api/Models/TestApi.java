package com.az.data_client.api.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestApi {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("fluidDate")
    @Expose
    public String fluidDate;
    @SerializedName("readyDate")
    @Expose
    public String readyDate;
    @SerializedName("result")
    @Expose
    public Object result;
    @SerializedName("fluid")
    @Expose
    public String fluid;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("value")
    @Expose
    public String value;
    @SerializedName("unit")
    @Expose
    public String unit;
    @SerializedName("referenceLimits")
    @Expose
    public ReferenceLimits referenceLimits;
    @SerializedName("fluidType")
    @Expose
    public String fluidType;
    @SerializedName("updateType")
    @Expose
    public Integer updateType;

}
