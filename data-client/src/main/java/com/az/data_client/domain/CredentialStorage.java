package com.az.data_client.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.az.data_client.domain.Models.MobileRegistrationResult;
import com.az.data_client.domain.Models.RegistrationResult;

import org.joda.time.DateTime;


import static android.content.Context.MODE_PRIVATE;

public class CredentialStorage {
    private SharedPreferences preferences;
    private String login = "Login";
    private String pass = "Password";
    private String sessionKey = "SessionKey";
    private  String sessionKeyDate = "SessionKeyDate";
    public String Login;
    public String Password;
    public String SessionKey;
    public DateTime SessionKeyDate;

    private boolean CredentialsSet(){
        return (Login != null && Login.length()>0 && Password != null && Password.length()>0);
    }

    private boolean SessionKeyValid(){
        return  SessionKey != null && SessionKey.length()>0 && SessionKeyDate != null
                && SessionKeyDate.plusHours(6).isAfterNow();
    }

    public CredentialStorage(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences("cdms_mobile", MODE_PRIVATE);
        Login = decrypt(preferences.getString(encrypt(login), ""));
        Password = decrypt(preferences.getString(encrypt(pass), ""));
        Login = "92048125-faa4-4293-bee9-c43dee171c2f";
        Password = "DiIgQK24(L]sCD]Xz^J[O/tC(&BSN>GnO&H[";
        SessionKey = decrypt(preferences.getString(encrypt(sessionKey), ""));
        long ticks = preferences.getLong(encrypt(sessionKeyDate),0);
        if (ticks>0)
            SessionKeyDate = new DateTime(ticks);

    }
    public void UpdateCredentials(MobileRegistrationResult responce){
        if (responce.result == RegistrationResult.Success) {
            Login = responce.Login;
            Password = responce.Pass;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(encrypt(login), encrypt(responce.Login));
            editor.putString(encrypt(pass), encrypt(responce.Pass));
             editor.commit();
        }
    }

    public void  UpdateSessionKey(String key){
        SessionKey = key;
        SessionKeyDate = DateTime.now();
        SessionKeyDate.getMillis();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(encrypt(sessionKey), encrypt(key));
        editor.putLong(encrypt(sessionKeyDate), SessionKeyDate.getMillis());
        editor.apply();
    }

    private static String encrypt(String input) {
        // This is base64 encoding, which is not an encryption
        return Base64.encodeToString(input.getBytes(), Base64.NO_WRAP);
    }

    private static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.NO_WRAP));
    }
    private AccountState _state;

    public AccountState getState() {
        if (_state != null && _state != AccountState.Unknown)
            return _state;
        if (SessionKeyValid())
            return AccountState.LoggedIn;
        if ( CredentialsSet())
            return AccountState.NotLoggedIn;
        return AccountState.NotRegistered;
    }

    public void SetState(AccountState state) {
        if (state == AccountState.NotLoggedIn) {
            UpdateSessionKey("");
        } else {
            _state = state;
        }
    }
}

