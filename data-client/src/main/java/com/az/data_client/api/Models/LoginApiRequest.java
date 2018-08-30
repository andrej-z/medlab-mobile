package com.az.data_client.api.Models;

public class LoginApiRequest {
    public String Login;
    public String Password;

    public LoginApiRequest(String login, String password) {
        Login = login;
        Password = password;
    }
}
