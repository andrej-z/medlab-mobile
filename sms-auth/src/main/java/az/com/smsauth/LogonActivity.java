package az.com.smsauth;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import az.com.smsauth.Common.Constants;
import az.com.smsauth.Common.CountryUtils;
import az.com.smsauth.Common.Util;
import az.com.smsauth.Models.Country;
import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

public class LogonActivity extends AppCompatActivity {
    private AppCompatEditText etCountryCode;
    private AppCompatEditText etPhoneNumber;
    private AppCompatButton btnSendConfirmationCode;
    private ImageView imgFlag;
    private AppCompatTextView tvToolbarTitle;
    private Activity mActivity = LogonActivity.this;
    private PhoneNumberUtil mPhoneUtil;
    private Country mSelectedCountry;
    private static final int COUNTRYCODE_ACTION = 1;
    private static final int VERIFICATION_ACTION = 2;
    public String title = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_logon);
//        Button loginBtn = findViewById(R.id.login_btn);
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), RequestsActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                startActivity(intent);
//            }
//        });
//        if (AppContext.Get().dataService.()){
//            //show wait animation
//            //show request activity if login successful
//        }
        setUpUI();

    }



    private void setUpUI() {
        etCountryCode = findViewById(R.id.etCountryCode);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        imgFlag = findViewById(R.id.flag_imv);
        btnSendConfirmationCode = findViewById(R.id.login_btn);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        mPhoneUtil = PhoneNumberUtil.createInstance(mActivity);

//
        TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        String countryISO = tm.getNetworkCountryIso();
        String countryNumber = "";
        String countryName = "";

        if(!TextUtils.isEmpty(countryISO))
        {
            for (Country country : CountryUtils.getAllCountries(mActivity)) {
                if (countryISO.toLowerCase().equalsIgnoreCase(country.getIso().toLowerCase())) {
                    countryNumber = country.getPhoneCode();
                    countryName = country.getName();
                    break;
                }
            }
            Country country = new Country(countryISO,
                    countryNumber,
                    countryName);
            this.mSelectedCountry = country;
            etCountryCode.setText("+" + country.getPhoneCode() + "");
            imgFlag.setImageResource(CountryUtils.getFlagDrawableResId(country.getIso()));

        }
        else {
            Country country = new Country(getString(R.string.country_united_states_code),
                    getString(R.string.country_united_states_number),
                    getString(R.string.country_united_states_name));
            this.mSelectedCountry = country;
            etCountryCode.setText("+" + country.getPhoneCode() + "");
            imgFlag.setImageResource(CountryUtils.getFlagDrawableResId(country.getIso()));

        }

        setPhoneNumberHint();
        etCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.hideKeyBoardFromView(mActivity);
                etPhoneNumber.setError(null);
                Intent intent = new Intent(mActivity, CountryCodeActivity.class);
                intent.putExtra("TITLE", getResources().getString(R.string.app_name));
                startActivityForResult(intent, COUNTRYCODE_ACTION);
            }
        });
        btnSendConfirmationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.hideKeyBoardFromView(mActivity);
                etPhoneNumber.setError(null);
                if (validate()) {
                    Intent verificationIntent = new Intent(mActivity, VerificationCodeActivity.class);
                    verificationIntent.putExtra(Constants.PhoneNumber, etPhoneNumber.getText().toString().trim());
                    verificationIntent.putExtra(Constants.PhoneCode, mSelectedCountry.getPhoneCode() + "");

                    startActivityForResult(verificationIntent,VERIFICATION_ACTION);

                }
            }
        });

        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra("PHONE_NUMBER")) {

                etPhoneNumber.setText(getIntent().getStringExtra("PHONE_NUMBER"));
                etPhoneNumber.setSelection(etPhoneNumber.getText().toString().trim().length());
            }
        }
    }


    private void setPhoneNumberHint() {
        if (mSelectedCountry != null) {
            Phonenumber.PhoneNumber phoneNumber =
                    mPhoneUtil.getExampleNumberForType(mSelectedCountry.getIso().toUpperCase(),
                            PhoneNumberUtil.PhoneNumberType.MOBILE);
            if (phoneNumber != null) {
                String format = mPhoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                if (format.length() > mSelectedCountry.getPhoneCode().length())
                    etPhoneNumber.setHint(
                            format.substring((mSelectedCountry.getPhoneCode().length() + 1), format.length()));
            }
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(etPhoneNumber.getText().toString().trim())) {
            etPhoneNumber.setError("Please enter phone number");
            etPhoneNumber.requestFocus();
            return false;
        } else if (!isValid()) {
            etPhoneNumber.setError("Please enter valid phone number");
            etPhoneNumber.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COUNTRYCODE_ACTION) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    if (data.hasExtra(Constants.COUNTRY)) {
                        Country country = (Country) data.getSerializableExtra(Constants.COUNTRY);
                        this.mSelectedCountry = country;
                        setPhoneNumberHint();
                        etCountryCode.setText("+" + country.getPhoneCode() + "");
                        imgFlag.setImageResource(CountryUtils.getFlagDrawableResId(country.getIso()));
                    }
                }
            }
        } else if (requestCode == VERIFICATION_ACTION) {
            if (data != null && resultCode == Activity.RESULT_OK) {

                String idToken=data.getStringExtra("IDTOKEN");
                Intent intent = new Intent();
                intent.putExtra("IDTOKEN", idToken);
                setResult(Activity.RESULT_OK,intent);
                Log.d("FIREBASE", "ACtivityResponce result = " + idToken);
                finish();
            } else {
                Intent intent = new Intent();

                setResult(Activity.RESULT_CANCELED,intent);
                Log.d("FIREBASE", "ACtivityResponce result = " );
                finish();

            }
        }
    }

    public boolean isValid() {
        Phonenumber.PhoneNumber phoneNumber = getPhoneNumber();
        return phoneNumber != null && mPhoneUtil.isValidNumber(phoneNumber);
    }

    public Phonenumber.PhoneNumber getPhoneNumber() {
        try {
            String iso = null;
            if (mSelectedCountry != null) {
                iso = mSelectedCountry.getIso().toUpperCase();
            }
            return mPhoneUtil.parse(etPhoneNumber.getText().toString().trim(), iso);
        } catch (NumberParseException ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
}

