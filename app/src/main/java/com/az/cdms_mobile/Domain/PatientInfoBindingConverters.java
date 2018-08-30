package com.az.cdms_mobile.Domain;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.az.cdms_mobile.Models.Test;
import com.az.data_client.domain.Models.UserInfo;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PatientInfoBindingConverters {
    @BindingAdapter("bindName")
    public  static void setPatientName(TextView textView, UserInfo u){
        textView.setText(u.Name + " "+u.Surname);

    }

    @BindingAdapter("bindBirthDate")
    public  static void setBirthDate(TextView textView, UserInfo u){
        textView.setText(u.BirthDate.toString());

    }
    @BindingAdapter("bindAddress")
    public  static void setAddress(TextView textView, UserInfo u){
        textView.setText(u.Country +", "+ u.City + " "+ u.Address);

    }
}
