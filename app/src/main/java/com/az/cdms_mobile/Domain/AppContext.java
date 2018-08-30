package com.az.cdms_mobile.Domain;

import android.content.Context;

import com.az.cdms_mobile.CdmsMobileApplication;
import com.az.cdms_mobile.Repository.CdmsRepository;
import com.az.data_client.DataService;

public class AppContext {
    public DataService dataService;
    private static AppContext instance;
    public CdmsRepository repository;
    public static AppContext Get(){
        if (instance == null)
            instance = new AppContext(CdmsMobileApplication.getAppContext());
        return instance;
    }
    private AppContext(Context context){
        dataService= new DataService(context);
        repository = new CdmsRepository();
    }
}
