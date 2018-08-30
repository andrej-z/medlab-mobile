package com.az.data_client.db.dbMappers;


import com.az.data_client.db.dbModels.dbUserInfo;
import com.az.data_client.domain.Models.UserInfo;

public class dbUserInfoMapper {
    public static UserInfo MapUserFromDb(dbUserInfo source){
        return  new UserInfo(source);
    }

    public static  dbUserInfo MapUserToDb (UserInfo source){
        dbUserInfo result = new dbUserInfo();
        result.pid = source.PID;

        result.first_name = source.Name;
         result.last_naame = source.Surname;
         result.birth_date = source.BirthDate;
        result.sex = source.Sex.ordinal();
         result.phone = source.Phone;
         result.email = source.Email;
        result.address = source.Address;
        result.city = source.City;
         result.postal_code = source.PostalCode;
         result.country = source.Country;
         result.comment = source.Comment;
         result.deleted = source.Deleted;
         return  result;
    }
}
