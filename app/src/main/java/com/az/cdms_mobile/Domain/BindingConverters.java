package com.az.cdms_mobile.Domain;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.az.cdms_mobile.Models.Fluid;
import com.az.cdms_mobile.Models.Request;
import com.az.cdms_mobile.Models.Test;
import com.az.cdms_mobile.Models.TestResult;
import com.az.cdms_mobile.R;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class BindingConverters {
    @BindingAdapter("requestDateBind")
    public static void setRequestDateBind(TextView textView, DateTime date){
        textView.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(date.toDate()));
    }
    @BindingAdapter("requestNumberBind")
    public  static void setRequestNumber(TextView textView, int number){
        textView.setText(String.format("Request %s",number));
    }
    @BindingAdapter("requestTypeBind")
    public  static void setRequestType(TextView textView, String fluid){
        textView.setText(fluid);
    }
    @BindingAdapter("passedTests")
    public  static void setPassedTests(TextView textView, List<Test> tests){
       int passedCount =0;
        for (Test t: tests){

           if (t.getResult() == TestResult.Passed)
                passedCount++;

        }
        textView.setText(passedCount+"/"+tests.size());
    }
    @BindingAdapter("requestChartBind")
    public static void setChart(PieChartView chart, Request r)
    {
         boolean hasLabels = false;
        boolean hasLabelsOutside = false;
        boolean hasCenterCircle = true;
        boolean hasCenterText1 = false;
        boolean hasCenterText2 = false;
        boolean isExploded = false;
        boolean hasLabelForSelected = false;

        List<SliceValue> values = new ArrayList<SliceValue>();
        int notReadyCount=0;
        int passedCount =0;
        int failedCount =0;
        for (Test t: r.Tests){

            if (t.getResult() == TestResult.Unknown)
                notReadyCount++;
            else if (t.getResult() == TestResult.Passed)
                passedCount++;
            else if (t.getResult() == TestResult.Failed)
                failedCount++;
        }
        if (notReadyCount>0)
        {
            SliceValue sliceValue = new SliceValue((float) notReadyCount, chart.getResources().getColor(R.color.NotReady));
            values.add(sliceValue);
        }
        if (passedCount>0)
        {
            SliceValue sliceValue = new SliceValue((float) passedCount, chart.getResources().getColor(R.color.Passed));
            values.add(sliceValue);
        }
        if (failedCount>0)
        {
            SliceValue sliceValue = new SliceValue((float) failedCount, chart.getResources().getColor(R.color.Failed));
            values.add(sliceValue);
        }


        PieChartData data = new PieChartData(values);
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);
        data.setCenterCircleScale(0.8f);

        if (isExploded) {
            data.setSlicesSpacing(24);
        }


        data.setCenterText1(r.Tests.size()-notReadyCount+"/"+r.Tests.size());
        data.setCenterText1FontSize(20);
        data.setCenterText2("Tests done");
        data.setCenterText2FontSize(12);
            // Get roboto-italic font.
//            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
//            data.setCenterText1Typeface(tf);
//
//            // Get font size from dimens.xml and convert it to sp(library uses sp values).
//            data.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
//                    (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));
       // }

//        if (hasCenterText2) {
//            data.setCenterText2("Charts (Roboto Italic)");
//
////            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
//
//            data.setCenterText2Typeface(tf);
//            data.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
//                    (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));
//        }
        chart.setChartRotation(270,false);
        chart.setPieChartData(data);

    }

}
