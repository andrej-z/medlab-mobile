package com.az.data_client.api.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestsApiResponce {
    @SerializedName("lastLogRecordId")
    @Expose
    public int lastLogRecordId;
    @SerializedName("requests")
    @Expose
    public List<RequestApi> requests = null;
}
