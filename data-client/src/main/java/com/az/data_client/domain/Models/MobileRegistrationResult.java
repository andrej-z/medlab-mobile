package com.az.data_client.domain.Models;

import com.az.data_client.api.Models.RegistrationApiResponce;


public class MobileRegistrationResult
{
    public RegistrationResult result;
    public String Login;
    public String Pass;
    public boolean Disabled;

    public MobileRegistrationResult(RegistrationApiResponce responce) {
        if (responce.accountData != null) {
            Login = responce.accountData.login;
            Pass = responce.accountData.pass;
            Disabled = responce.accountData.disabled;
        }
        result = RegistrationResult.values()[responce.result];
    }
    public MobileRegistrationResult(){

    }
}

