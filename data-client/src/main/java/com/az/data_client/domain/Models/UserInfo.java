package com.az.data_client.domain.Models;

import com.az.data_client.api.Models.UserInfoApiResponce;
import com.az.data_client.db.dbModels.dbUserInfo;

import org.joda.time.DateTime;


public class UserInfo {
    public int Id ;
    public String PID ;
    public String Name ;
    public String Surname ;
    public DateTime BirthDate ;
    public Sex Sex ;
    public String Phone ;
    public String Email ;
    public String Address ;
    public String City ;
    public String PostalCode ;
    public String Country ;
    public String Comment ;
    public boolean Deleted ;
    public UserInfo(UserInfoApiResponce responce){
        Id = responce.id;
        PID = responce.pid;
        Name = responce.name;
        Surname = responce.surname;
        BirthDate = new DateTime(responce.birthDate);
        Sex = com.az.data_client.domain.Models.Sex.values()[responce.sex];
        Phone = responce.phone;
        Email = responce.email;
        Address = responce.address;
        City = responce.city;
        PostalCode = responce.postalCode;
        Country = responce.country;
        Comment = responce.comment;
        Deleted = responce.deleted;
    }
    public UserInfo(UserInfo responce){
        Id = responce.Id;
        PID = responce.PID;
        Name = responce.Name;
        Surname = responce.Surname;
        BirthDate = new DateTime(responce.BirthDate);
        Sex = responce.Sex;
        Phone = responce.Phone;
        Email = responce.Email;
        Address = responce.Address;
        City = responce.City;
        PostalCode = responce.PostalCode;
        Country = responce.Country;
        Comment = responce.Comment;
        Deleted = responce.Deleted;
    }

    public UserInfo(dbUserInfo responce){
        Id = responce.id;
        PID = responce.pid;
        Name = responce.first_name;
        Surname = responce.last_naame;
        BirthDate = new DateTime(responce.birth_date);
        Sex = com.az.data_client.domain.Models.Sex.values()[responce.sex];
        Phone = responce.phone;
        Email = responce.email;
        Address = responce.address;
        City = responce.city;
        PostalCode = responce.postal_code;
        Country = responce.country;
        Comment = responce.comment;
        Deleted = responce.deleted;
    }
}
