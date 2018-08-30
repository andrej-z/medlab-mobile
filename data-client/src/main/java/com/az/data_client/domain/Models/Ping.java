package com.az.data_client.domain.Models;

import com.az.data_client.api.Models.PingApiResponce;


public class Ping {
    public String ServerState;

    public Ping(PingApiResponce responce) {
        ServerState = responce.serverStatus;
    }
}
