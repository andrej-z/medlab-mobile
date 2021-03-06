package com.az.cdms_mobile.Common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.lang.reflect.Field;

public class Util {
    public static void showToast(final Context ctx, final String message) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
            }
        });
    }



    public static void hideKeyBoardFromView(Activity mActivity) {
        InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = mActivity.getCurrentFocus();
        if (view == null) {
            view = new View(mActivity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideSoftKeyboard(Activity mActivity) {
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    public static void preventClose(DialogInterface dialog, boolean prevent){
        try {
            // do not close
            Class DialogClass = dialog.getClass().getSuperclass();
            if (!DialogClass.getName().equals("android.app.Dialog"))
                DialogClass = DialogClass.getSuperclass();
            Field field = DialogClass
                    .getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, !prevent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
