package com.az.data_client.api.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginApiResponce {
    @SerializedName("result")
    @Expose
    public Integer result;
    @SerializedName("dbTime")
    @Expose
    public String dbTime;
}
