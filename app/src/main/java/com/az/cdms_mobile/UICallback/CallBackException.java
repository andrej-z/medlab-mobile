package com.az.cdms_mobile.UICallback;

import com.az.cdms_mobile.CdmsMobileApplication;
import com.az.cdms_mobile.Common.Util;
import com.az.cdms_mobile.Domain.AppContext;

import io.reactivex.functions.Consumer;

public class CallBackException implements Consumer<Throwable> {
    @Override
    public void accept(Throwable throwable) throws Exception {
        Util.showToast(CdmsMobileApplication.getAppContext(),throwable.getMessage());
    }
}
