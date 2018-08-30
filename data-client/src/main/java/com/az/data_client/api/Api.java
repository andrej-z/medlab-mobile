package com.az.data_client.api;


import com.az.data_client.api.Models.LoginApiResponce;
import com.az.data_client.api.Models.PingApiResponce;
import com.az.data_client.api.Models.RegistrationApiResponce;
import com.az.data_client.api.Models.RequestsApiResponce;
import com.az.data_client.api.Models.UserInfoApiResponce;

import io.reactivex.Maybe;
import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @GET("mobile/register")
    Maybe<RegistrationApiResponce> register(@Header("Authorization") String credentials);

    //this is a method to send raw data.
    @POST("mobile/login")
    @Headers({
            "Content-Type: application/json",
            "Cache-Control: no-cache"
    })
    Maybe<LoginApiResponce> login(@Body RequestBody body);

    @GET("/mobile/user")
    Single<UserInfoApiResponce> getUserInfo();

    @GET("/mobile/requests")
    Single<RequestsApiResponce> getRequests(@Query("lastRecordId") int recordId);

    @GET("/mobile/ping")
    Single<PingApiResponce> getPing();


}