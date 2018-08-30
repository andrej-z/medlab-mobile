package com.az.cdms_mobile;

import android.app.Application;
import android.content.Context;

public class CdmsMobileApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        CdmsMobileApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return CdmsMobileApplication.context;
    }
}