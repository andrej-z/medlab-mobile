package com.az.data_client.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.az.data_client.domain.Models.MobileRegistrationResult;
import com.az.data_client.domain.Models.PinProtectionModel;
import com.az.data_client.domain.Models.RegistrationResult;

import org.joda.time.DateTime;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.content.Context.MODE_PRIVATE;

public class CredentialStorage {
    private SharedPreferences preferences;
    private String login = "Login";
    private String pass = "Password";
    private String sessionKey = "SessionKey";
    private  String sessionKeyDate = "SessionKeyDate";
    private String pinEncryptionEnabled = "PinEncryptionEnabled";
    private String securePin = "SecurePin";
    public String Login;
    public String Password;
    public String SessionKey;
    public String PingProtectionField = "PinProtectionField";
    public PinProtectionModel PinProtection;
    public DateTime SessionKeyDate;

    private boolean CredentialsSet(){
        return (Login != null && Login.length()>0 && Password != null && Password.length()>0/* && !PinProtection.IsGenerated*/);
    }

    private boolean SessionKeyValid(){
        return  SessionKey != null && SessionKey.length()>0 && SessionKeyDate != null
                && SessionKeyDate.plusHours(6).isAfterNow();
    }

    public CredentialStorage(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences("cdms_mobile", MODE_PRIVATE);
        //Login = decrypt(preferences.getString(encrypt(login), ""));
        //Password = decrypt(preferences.getString(encrypt(pass), ""));
        //Log.d("LoginPassword",Login);
        //Log.d("LoginPassword",Password);
        Login = "92048125-faa4-4293-bee9-c43dee171c2f";
        Password = "DiIgQK24(L]sCD]Xz^J[O/tC(&BSN>GnO&H[";
        SessionKey = decrypt(preferences.getString(encrypt(sessionKey), ""));

        long ticks = preferences.getLong(encrypt(sessionKeyDate),0);
        if (ticks>0)
            SessionKeyDate = new DateTime(ticks);

        PinProtection = new PinProtectionModel(
                decrypt(preferences.getString(encrypt(PingProtectionField),"")));

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

    public  void UpdatePinProtection(String pin){
        if (!CredentialsSet())
            return;
        if (pin == null || pin.equals("")){
            PinProtection.PinProtectionEnabled = false;
            PinProtection.Pin="";
        } else{
            PinProtection.PinProtectionEnabled = true;
            PinProtection.Pin = md5(pin);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(encrypt(PingProtectionField), encrypt(PinProtection.GetModel()));
        editor.commit();
    }

    public boolean validatePin(String pin){
        if (pin.equals("") && PinProtection.Pin.equals(""))
            return true;
        return md5(pin).equals(PinProtection.Pin);
    }
    private static String encrypt(String input) {
        // This is base64 encoding, which is not an encryption
        return Base64.encodeToString(input.getBytes(), Base64.NO_WRAP);
    }

    private static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.NO_WRAP));
    }

    private static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
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

