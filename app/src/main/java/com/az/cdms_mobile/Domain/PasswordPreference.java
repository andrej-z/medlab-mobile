package com.az.cdms_mobile.Domain;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.az.cdms_mobile.Common.Util;
import com.az.cdms_mobile.R;

import java.lang.reflect.Field;

public class PasswordPreference extends DialogPreference {
    private String newPassword;

    TextView oldPasswordView;
    TextView newPasswordView;
    TextView repeatNewPasswordView;
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
       if (positiveResult){
       super.callChangeListener(newPassword);
       }
    }

    public PasswordPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.pin_preference_layout);


    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        oldPasswordView = view.findViewById(R.id.oldPasswordTxt);
        newPasswordView = view.findViewById(R.id.newPasswordTxt);
        repeatNewPasswordView = view.findViewById(R.id.repatNewPassowrdTxt);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        super.onDismiss(dialog);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE){
            String oldPin = oldPasswordView.getText().toString();
            String newPin = newPasswordView.getText().toString();
            String repeatNewPin= repeatNewPasswordView.getText().toString();
            if (!newPin.equals(repeatNewPin))
            {
                repeatNewPasswordView.setError("Pin not match");
                Util.preventClose(dialog,true);
                return;
            }
            if (!AppContext.Get().dataService.validatePin(oldPin)){
                oldPasswordView.setError("Wrong old Pin");
                Util.preventClose(dialog,true);
                return;
            }
            newPassword = newPin;
            Util.preventClose(dialog,false);
        }

        super.onClick(dialog, which);

    }


}
