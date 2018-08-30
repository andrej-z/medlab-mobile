package com.az.cdms_mobile.Views;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.az.cdms_mobile.Domain.AppContext;
import com.az.cdms_mobile.R;
import com.az.cdms_mobile.databinding.ActivityPatientInfoBinding;

import java.text.DateFormat;

public class PatientInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        ActivityPatientInfoBinding  binding = DataBindingUtil.setContentView(this, R.layout.activity_patient_info);
        binding.setPatient(AppContext.Get().repository.userInfo);
        SetupToolbar();

    }

    private void SetupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Jan kowalski");
        getSupportActionBar().setSubtitle("PatientInfo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
