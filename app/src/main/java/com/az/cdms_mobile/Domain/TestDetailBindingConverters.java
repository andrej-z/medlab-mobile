package com.az.cdms_mobile.Domain;


import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.az.cdms_mobile.CdmsMobileApplication;
import com.az.cdms_mobile.Common.Util;
import com.az.cdms_mobile.Models.Test;
import com.az.cdms_mobile.Models.TestResult;
import com.az.cdms_mobile.R;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TestDetailBindingConverters {
    @BindingAdapter("bindResultIcon")
    public  static void setRequestNumber(ImageView ImgView, Test t){

    if (t.getResult() == TestResult.Passed) {
        ImgView.setImageResource(R.drawable.ic_smile_good);
    } else if (t.getResult() == TestResult.Failed)
        ImgView.setImageResource(R.drawable.ic_smile_bad);

    }

    @BindingAdapter("bindResultValue")
    public  static void setResultValue(TextView textView, Test d){
        NumberFormat nf = new DecimalFormat("##.##");

        if (d.Value != null) {
            textView.setText(nf.format(d.Value)+" "+ d.Unit.toLowerCase());
        }
    }

    @BindingAdapter("bindResultText")
    public  static void setResultText(TextView textView,  Test t){
        if (t.getResult() == TestResult.Passed) {
            textView.setText("Normal");
        }
        else if (t.LowerLimit != null && t.Value != null && t.Value<t.LowerLimit)
        textView.setText("Below normal");
        else if (t.UpperLimit != null && t.Value != null && t.Value>t.UpperLimit)
            textView.setText("Above normal");
        else if (t.UpperLimit == null && t.LowerLimit == null && t.Value != null)
            textView.setText("Detected");
        else
            textView.setText("No data");
    }
    @BindingAdapter("HistoryButtonVisible")
    public static void IsHistoryButtonVisible(final ImageView button, final Test t){

        AppContext.Get().repository
        .GetSimilarTests(t.Fluid,t.Name).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Test>>() {
                    @Override
                    public void accept(List<Test> tests) throws Exception {
                       if (tests.size()>1){
                           button.setVisibility(View.VISIBLE);
                           t.HistoryChartAvailable = true;
                       }
                    }
                });
    }



}
