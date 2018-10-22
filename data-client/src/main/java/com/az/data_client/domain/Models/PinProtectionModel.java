package com.az.data_client.domain.Models;

import com.google.gson.Gson;

import java.util.Date;

public class PinProtectionModel {
    public boolean PinProtectionEnabled;
    public String Pin;
    public boolean IsGenerated;
    public PinProtectionModel(String data){
        if (data == null || data.equals(""))
            IsGenerated = true;
        else {
            try {
                PinProtectionModel model = new Gson().fromJson(data, PinProtectionModel.class);
                this.PinProtectionEnabled = model.PinProtectionEnabled;
                this.Pin = model.Pin;
            } catch (Exception ex){
                IsGenerated = true;
            }
        }
    }
    public String GetModel(){
        return new Gson().toJson(this);
    }
}
