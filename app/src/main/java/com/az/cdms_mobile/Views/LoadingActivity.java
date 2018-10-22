package com.az.cdms_mobile.Views;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.az.cdms_mobile.CdmsMobileApplication;
import com.az.cdms_mobile.Common.Util;
import com.az.cdms_mobile.Domain.AppContext;
import com.az.cdms_mobile.Domain.Mappers.VmRequestMapper;
import com.az.cdms_mobile.R;
import com.az.cdms_mobile.UICallback.CallBackException;
import com.az.data_client.api.CallBack;
import com.az.data_client.domain.AccountState;
import com.az.data_client.domain.Models.MobileRegistrationResult;
import com.az.data_client.domain.Models.RegistrationResult;
import com.az.data_client.domain.Models.Tests.DataRequest;
import com.az.data_client.domain.Models.UserInfo;

import java.util.List;

import az.com.smsauth.LogonActivity;
import io.reactivex.functions.Consumer;

public class LoadingActivity extends AppCompatActivity {
    boolean timerFired = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        //deleteDatabase("cdms");


        final CountDownTimer timer = new CountDownTimer(30000,2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (AppContext.Get().dataService.getInitialized() && !timerFired) {
                    this.cancel();
                    this.onFinish();
                    timerFired = true;
                }
            }

            @Override
            public void onFinish() {
                if (timerFired)
                    return;
                if (AppContext.Get().dataService.getAccountState() == AccountState.NotRegistered){
                    CallLoginActivity();
                    return;
                }
//                Intent intent = new Intent(CdmsMobileApplication.getAppContext(), RequestsActivity.class);
//                startActivity(intent);
//                finish();
                if (AppContext.Get().dataService.getPinProtectionEnabled()){
                    showPinProtectionDialog();
                    return;
                }
                Intent intent = new Intent(CdmsMobileApplication.getAppContext(), RequestsActivity.class);

                startActivity(intent);
                finish();
            }
        };
        timer.start();


        AppContext.Get().dataService.init(new CallBack<List<DataRequest>>(new Consumer<List<DataRequest>>() {
            @Override
            public void accept(List<DataRequest> requests) throws Exception {
                AppContext.Get().repository.requests = VmRequestMapper.Map(requests);
            }
        }, new CallBackException()), new CallBack<UserInfo>(new Consumer<UserInfo>() {
            @Override
            public void accept(UserInfo userInfo) throws Exception {
                AppContext.Get().repository.userInfo = new UserInfo(userInfo);
            }
        }, new CallBackException()));

    }
    public void CallLoginActivity(){
        Intent intent = new Intent(CdmsMobileApplication.getAppContext(), LogonActivity.class);
        startActivityForResult(intent,11);
    }

    private void showPinProtectionDialog(){
        final  pinAttempts attempts = new pinAttempts();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoadingActivity.this);
        alertDialog.setTitle("Please enter PIN");
        alertDialog.setMessage(attempts.count+" attempts left");
        alertDialog.setView(R.layout.input_pin_dialog);

        alertDialog.setIcon(R.drawable.ic_lock_outline_black_24dp);


        alertDialog.setPositiveButton("Done",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        EditText input = ((AlertDialog)dialog).findViewById(R.id.pinInput);
                        if (attempts.count<1){
                            AppContext.Get().dataService.resetCredentials();
                            CallLoginActivity();
                            return;
                        }
                      String pin = input.getText().toString();
                      if (AppContext.Get().dataService.validatePin(pin)){
                          Intent intent = new Intent(CdmsMobileApplication.getAppContext(), RequestsActivity.class);

                          startActivity(intent);
                          finish();
                      } else{
                          input.setText("");
                          attempts.count--;
                          ((AlertDialog)dialog).setMessage( attempts.count+" attempts left");
                          Util.preventClose(dialog,true);
                      }


                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                        dialog.cancel();

                    }
                });

        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("FIREBASE", "ACtivityResponce received" );
        if (requestCode == 11) {
            if(resultCode == Activity.RESULT_OK){
                String idToken=data.getStringExtra("IDTOKEN");
                Log.d("FIREBASE", "ACtivityResponce received idtoken="+idToken );
                AppContext.Get().dataService.register(idToken,new CallBack<MobileRegistrationResult>(new Consumer<MobileRegistrationResult>() {
                    @Override
                    public void accept(MobileRegistrationResult mobileRegistrationResult) throws Exception {
                        if (mobileRegistrationResult.result == RegistrationResult.Success){
                            recreate();
                        }
                    }
                }));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    class pinAttempts{
        public int count;
        public pinAttempts(){
            count = 3;
        }
    }
}
