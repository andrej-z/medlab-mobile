package com.az.cdms_mobile.Views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.az.cdms_mobile.Domain.AppContext;
import com.az.cdms_mobile.Domain.Mappers.VmRequestMapper;
import com.az.cdms_mobile.Domain.RequestDataAdapter;
import com.az.cdms_mobile.Models.Request;
import com.az.cdms_mobile.R;
import com.az.cdms_mobile.Repository.IRepository;
import com.az.cdms_mobile.Repository.TestRepository;
import com.az.cdms_mobile.UICallback.CallBackException;
import com.az.data_client.api.CallBack;
import com.az.data_client.domain.Models.Sex;
import com.az.data_client.domain.Models.Tests.DataRequest;
import com.az.data_client.domain.Models.UserInfo;

import java.text.DateFormat;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RequestsActivity extends AppCompatActivity
        implements RequestDataAdapter.RequestClickListener,
        SwipeRefreshLayout.OnRefreshListener, NavigationView.OnNavigationItemSelectedListener{
IRepository repository;
RecyclerView recyclerView;
ProgressBar progressBar;
SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager layoutManager;
    DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        swipeRefreshLayout = findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        progressBar = findViewById(R.id.progressBar);


        progressBar.setVisibility(View.VISIBLE);
        onLoad(AppContext.Get().repository.requests);

        AppContext.Get().dataService.updateFromApi(new CallBack<List<DataRequest>>(new Consumer<List<DataRequest>>() {
            @Override
            public void accept(List<DataRequest> dataRequests) throws Exception {
                List<Request> changedRequests = VmRequestMapper.Map(dataRequests);
                onUpdate(changedRequests);
                progressBar.setVisibility(View.GONE);

            }
        }, new CallBackException(progressBar)));
        SetupToolbar();
        SetupDrawer();
    }

    private void SetupDrawer() {
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        View header = navView.getHeaderView(0);
        TextView patientName = header.findViewById(R.id.patient_name);
        ImageView patientPhoto = header.findViewById(R.id.patient_photo);
        UserInfo userInfo = AppContext.Get().repository.userInfo;
       if (userInfo == null)
       {
           AppContext.Get().dataService.getUserInfo(new CallBack<UserInfo>(new Consumer<UserInfo>() {
               @Override
               public void accept(UserInfo userInfo) throws Exception {
                   userInfo  = new UserInfo(userInfo); AppContext.Get().repository.userInfo = userInfo;
                   patientName.setText(userInfo.Name+" "+userInfo.Surname);
                   if (userInfo.Sex == Sex.F)
                       patientPhoto.setImageResource(R.drawable.woman);
               }
           }));
       } else {
           patientName.setText(userInfo.Name + " " + userInfo.Surname);
           if (userInfo.Sex == Sex.F)
               patientPhoto.setImageResource(R.drawable.woman);
       }
        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                });




    }

    private void SetupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My requests");
       // getSupportActionBar().setSubtitle(DateFormat.getDateInstance(DateFormat.SHORT).format(request.FluidDate.toDate()));
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

    }

    void onLoad(List<Request> requests){
       // loadingIndicator.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new RequestDataAdapter(requests, this));
       // progressBar.setVisibility(View.GONE);
    }

    void onUpdate (List<Request> requests) {
        ((RequestDataAdapter)recyclerView.getAdapter()).Update(requests);
    }
    @Override
    public void onItemClick(Request r) {

        Intent intent = new Intent(RequestsActivity.this, TestDetailActivity.class);
        intent.putExtra("ID",r.Id);
        startActivityForResult(intent,0);
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.VISIBLE);
        AppContext.Get().dataService.updateFromApi(new CallBack<List<DataRequest>>(new Consumer<List<DataRequest>>() {
            @Override
            public void accept(List<DataRequest> dataRequests) throws Exception {
                List<Request> changedRequests = VmRequestMapper.Map(dataRequests);
                onUpdate(changedRequests);
                progressBar.setVisibility(View.GONE);

            }
        }, new CallBackException(progressBar)));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;



        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        switch (item.getItemId()){

            case R.id.nav_refresh:

                onRefresh();
                return true;
            case  R.id.nav_patient:
                Intent intent1 = new Intent(RequestsActivity.this, PatientInfoActivity.class);
                startActivityForResult(intent1,1);
                return true;
            case R.id.nav_exit:
                new AlertDialog.Builder(this)
                        .setTitle("Exit?")
                        .setMessage("Do you really want to exit?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

                return true;
            case R.id.nav_settings:
                Intent intent2 = new Intent(RequestsActivity.this, SettingsActivity.class);
                startActivityForResult(intent2,2);
                return true;
        }
        return true;
    }
}
