package com.az.data_client.api.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountData {

    @SerializedName("login")
    @Expose
    public String login;
    @SerializedName("pass")
    @Expose
    public String pass;
    @SerializedName("disabled")
    @Expose
    public Boolean disabled;

}