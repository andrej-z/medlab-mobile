package com.az.cdms_mobile.Views;

import android.content.Intent;
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
import com.az.cdms_mobile.Domain.RequestDataAdapter;
import com.az.cdms_mobile.Domain.TestDetailDataAdapter;
import com.az.cdms_mobile.Models.Request;
import com.az.cdms_mobile.Models.Test;
import com.az.cdms_mobile.R;
import com.az.cdms_mobile.Repository.IRepository;
import com.az.cdms_mobile.Repository.TestRepository;

import java.text.DateFormat;

public class TestDetailActivity extends AppCompatActivity implements TestDetailDataAdapter.TestClickListener {
    private Request request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detail);


        int id = getIntent().getIntExtra("ID",0);
        request = AppContext.Get().repository.FindById(id);

        SetupToolbar();

       RecyclerView recyclerView = findViewById(R.id.detail_list);

       LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(new TestDetailDataAdapter(request.Tests, this));

    }

    private void SetupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(request.FluidType.toString());
        getSupportActionBar().setSubtitle(DateFormat.getDateInstance(DateFormat.SHORT).format(request.FluidDate.toDate()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onItemClick(Test t) {
        if (t.HistoryChartAvailable) {
            Intent intent = new Intent(this, TestsChartActivity.class);
            intent.putExtra("NAME", t.Name);
            intent.putExtra("FLUID", t.Fluid);
            this.startActivity(intent);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
