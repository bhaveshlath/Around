package com.example.blath.around.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.blath.around.R;
import com.example.blath.around.commons.Utils.Operations;
import com.example.blath.around.commons.Utils.ResponseOperations;
import com.example.blath.around.commons.Utils.UIUtils;
import com.example.blath.around.commons.Utils.app.AroundAppHandles;
import com.example.blath.around.events.LoginUserEvent;
import com.example.blath.around.models.AroundLocation;
import com.example.blath.around.models.User;
import com.example.blath.around.models.UserPersonalInformation;

import de.greenrobot.event.EventBus;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        UIUtils.animateStatusBarColorTransition(this, R.color.around_background_start_color, R.color.around_background_start_color);
        SharedPreferences userInfo = getSharedPreferences(AroundAppHandles.AROUND_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        String emailText = userInfo.getString(User.KEY_USER_EMAIL, "");
        String passwordText = userInfo.getString(User.KEY_USER_PASSWORD, "");
        if(!emailText.equals("") && !passwordText.equals("")) {
            verifyLoginCredentials(emailText, passwordText);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        UIUtils.animateStatusBarColorTransition(this, R.color.around_background_start_color, R.color.around_background_start_color);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    private boolean verifyLoginCredentials(String emailID, String password) {
        if (emailID.equals("")) {
            UIUtils.showShortToast("Please enter emailID", this);
        } else if (password.equals("")) {
            UIUtils.showShortToast("Please enter password", this);
        } else {
            Operations.loginUserOperation(emailID, password);
        }
        return false;
    }

    public void onEventMainThread(LoginUserEvent result) {
        if (ResponseOperations.isError(result.getResponseObject())) {
            UIUtils.showLongToast(result.getResponseObject().getMessage(), this);
        } else {
            SharedPreferences sharedPref = getSharedPreferences(AroundAppHandles.AROUND_SHARED_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.clear();

            User userObject = (User)result.getResponseObject().getBody();
            UserPersonalInformation personalInformationObject = userObject.getUserPersonalInformation();
            AroundLocation locationObject = userObject.getLastLocation();

            editor.putString(User.KEY_USER_PROFILE_STATUS, userObject.getProfileStatus());
            editor.putString(User.KEY_USER_ID, userObject.getUserId());
            editor.putString(User.KEY_USER_PROFILE_IMAGE, userObject.getProfileImage());

            editor.putString(User.KEY_USER_FIRST_NAME, personalInformationObject.getFirstName());
            editor.putString(User.KEY_USER_LAST_NAME, personalInformationObject.getLastName());
            editor.putString(User.KEY_USERNAME, personalInformationObject.getFirstName() + (personalInformationObject.getLastName().isEmpty() ? "" : " " + personalInformationObject.getLastName()));
            editor.putString(User.KEY_USER_EMAIL, personalInformationObject.getEmailID());
            editor.putString(User.KEY_USER_PHONE_NUMBER, personalInformationObject.getPhoneNumber());
            editor.putString(User.KEY_USER_DOB, personalInformationObject.getDOB());
            editor.putString(User.KEY_USER_PASSWORD, personalInformationObject.getPassword());

            editor.putString(User.KEY_USER_LATITUDE, Double.toString(locationObject.getLoc().getCoordinates()[1]));
            editor.putString(User.KEY_USER_LONGITUTDE, Double.toString(locationObject.getLoc().getCoordinates()[0]));
            editor.putString(User.KEY_USER_LOCATION_ADDRESS, locationObject.getAddress());
            editor.putString(User.KEY_USER_LOCATION_POSTALCODE, locationObject.getPostalCode());
            editor.putString(User.KEY_USER_LOCATION_COUNTRY, locationObject.getCountry());

            editor.commit();

            Intent loginIntent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(loginIntent);
        }
    }
}
