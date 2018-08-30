package com.az.data_client.domain.Models;

import com.az.data_client.api.Models.LoginApiResponce;

import org.joda.time.DateTime;

import java.util.Date;


public class LoginResponse
{
    public MobileLoginResult Result ;
    public Date DbTime ;

    public LoginResponse() {
    }

    public LoginResponse(MobileLoginResult result) {
        Result = result;
    }

    public LoginResponse(LoginApiResponce responce){
        Result = MobileLoginResult.values()[responce.result];
        DbTime = DateTime.parse(responce.dbTime).toDate();
    }
}

