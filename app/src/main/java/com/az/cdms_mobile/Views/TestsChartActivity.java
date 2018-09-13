package com.az.cdms_mobile.Views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.az.cdms_mobile.Domain.AppContext;
import com.az.cdms_mobile.Domain.TestChartDataAdapter;
import com.az.cdms_mobile.Domain.TestDetailDataAdapter;
import com.az.cdms_mobile.Models.Fluid;
import com.az.cdms_mobile.Models.Request;
import com.az.cdms_mobile.Models.Test;
import com.az.cdms_mobile.R;
import com.az.cdms_mobile.Repository.TestRepository;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lecho.lib.hellocharts.formatter.AxisValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class TestsChartActivity extends AppCompatActivity implements TestChartDataAdapter.TestClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String name = getIntent().getStringExtra("NAME");
        getSupportActionBar().setTitle("Test history");
        getSupportActionBar().setSubtitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String fluid = getIntent().getStringExtra("FLUID");

        AppContext.Get().repository.GetSimilarTests(fluid,name).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Test>>() {
                    @Override
                    public void accept(List<Test> tests) throws Exception {
                        if (tests.size()>1){
                            drawChart(tests);
                            bindList(tests);
                        }
                    }
                });

    }

    private void bindList(List<Test> tests) {
        RecyclerView recyclerView = findViewById(R.id.tests_chart);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(new TestChartDataAdapter(tests, this));
    }

    private void drawChart(List<Test> tests) {

        List<Line> lines = new ArrayList<Line>();
        List<PointValue> values = new ArrayList<PointValue>();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<AxisValue> axisYValues = new ArrayList<AxisValue>();
        for(Test t:tests){
            PointValue v = new PointValue((float) t.FluidDate.getMillis(), t.Value.floatValue());
            v.setLabel(t.FluidDate.toString());
            values.add(v );
            axisYValues.add(new AxisValue(v.getY()));
        }
        float middleValue = (values.get(0).getX() + values.get(values.size()-1).getX())/2;
        axisValues.add(new AxisValue(values.get(0).getX(),
                DateFormat.getDateInstance(DateFormat.SHORT).format(values.get(0).getX()).toCharArray()));
        axisValues.add(new AxisValue(middleValue,
                DateFormat.getDateInstance(DateFormat.SHORT).format(middleValue).toCharArray()));

        axisValues.add(new AxisValue(values.get(values.size()-1).getX(),
                DateFormat.getDateInstance(DateFormat.SHORT).format(values.get(values.size()-1).getX()).toCharArray()));
        Line line = new Line(values);
        line.setColor(Color.GRAY);
        line.setPointRadius(3);
        line.setStrokeWidth(1);

        lines.add(line);
        LineChartData data = new LineChartData(lines);
        LineChartView chart = findViewById(R.id.chart);
        Axis axisX = new Axis(axisValues);


        Axis axisY = new Axis(axisYValues);

//            axisX.setName("Axis X");
//            axisY.setName("Axis Y");

        
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        chart.setLineChartData(data);
        setChartViewport(chart, values);

    }

    private void setChartViewport(LineChartView chart, List<PointValue> values) {
        final Viewport v = new Viewport(chart.getMaximumViewport());
       float Ysize = v.top-v.bottom;
       if (Ysize==0)
           Ysize=1;
       float Xsize = v.right-v.left;
       float Ymargin = Ysize*0.05f;
        float Xmargin = Xsize*0.05f;
        v.top = v.top+Ymargin;
        v.bottom = v.bottom-Ymargin;
        v.left = v.left-Xmargin;
        v.right = v.right+Xmargin;

        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
        chart.setViewportCalculationEnabled(false);

    }


    @Override
    public void onItemClick(Request r) {

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
