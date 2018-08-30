package com.az.data_client.api.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationApiResponce {

    @SerializedName("result")
    @Expose
    public Integer result;
    @SerializedName("accountData")
    @Expose
    public AccountData accountData;

}