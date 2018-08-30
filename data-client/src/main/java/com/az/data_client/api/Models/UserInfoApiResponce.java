package com.az.data_client.api.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfoApiResponce {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("pid")
    @Expose
    public String pid;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("surname")
    @Expose
    public String surname;
    @SerializedName("birthDate")
    @Expose
    public String birthDate;
    @SerializedName("sex")
    @Expose
    public Integer sex;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("postalCode")
    @Expose
    public String postalCode;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("comment")
    @Expose
    public String comment;
    @SerializedName("deleted")
    @Expose
    public Boolean deleted;

}
