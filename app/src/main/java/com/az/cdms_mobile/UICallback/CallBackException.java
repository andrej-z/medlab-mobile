package com.az.cdms_mobile.UICallback;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.az.cdms_mobile.CdmsMobileApplication;
import com.az.cdms_mobile.Common.Util;
import com.az.cdms_mobile.Domain.AppContext;

import io.reactivex.functions.Consumer;

public class CallBackException implements Consumer<Throwable> {
    private ProgressBar progressBar;
    @Override
    public void accept(Throwable throwable) throws Exception {
        Util.showToast(CdmsMobileApplication.getAppContext(),throwable.getMessage());
        if (progressBar != null){
            progressBar.setVisibility(View.GONE);
        }
    }

    public CallBackException(){

    }

    public CallBackException(ProgressBar progressBar){
        this.progressBar = progressBar;
    }
}
