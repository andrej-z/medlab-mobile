package com.az.data_client.api.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReferenceLimits {

    @SerializedName("lowLimit")
    @Expose
    public Float lowLimit;
    @SerializedName("highLimit")
    @Expose
    public Float highLimit;

}
