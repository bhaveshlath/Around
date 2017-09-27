package com.example.blath.around.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.blath.around.R;
import com.example.blath.around.commons.Utils.Operations;
import com.example.blath.around.commons.Utils.ResponseOperations;
import com.example.blath.around.commons.Utils.UIUtils;
import com.example.blath.around.commons.Utils.app.AroundApplication;
import com.example.blath.around.events.LoginUserEvent;
import com.example.blath.around.models.AroundLocation;
import com.example.blath.around.models.User;
import com.example.blath.around.models.UserPersonalInformation;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        Button signupButton = (Button) findViewById(R.id.signup_button);
        signupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                EditText username = (EditText) findViewById(R.id.username);
                EditText password = (EditText) findViewById(R.id.password);
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();
                verifyLoginCredentials(usernameText, passwordText);
                UIUtils.hideKeyboard(this);
                break;

            case R.id.signup_button:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private boolean verifyLoginCredentials(String username, String password) {
        if (username.equals("")) {
            UIUtils.showShortToast("Please enter username", this);
        } else if (password.equals("")) {
            UIUtils.showShortToast("Please enter password", this);
        } else {
            findViewById(R.id.progress_overlay_container).setVisibility(View.VISIBLE);
            Operations.loginUserOperation(username, password);
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    public void onEvent(LoginUserEvent result) {
        if (ResponseOperations.isError(result.getResponseObject())) {
            findViewById(R.id.progress_overlay_container).setVisibility(View.GONE);
            UIUtils.showLongToast(result.getResponseObject().getMessage(), this);
        } else {
            SharedPreferences sharedPref = getSharedPreferences(AroundApplication.AROUND_SHARED_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            User userObject = (User)result.getResponseObject().getBody();
            UserPersonalInformation personalInformationObject = userObject.getUserPersonalInformation();
            AroundLocation locationObject = userObject.getLastLocation();

            editor.putString(User.KEY_USER_PROFILE_STATUS, userObject.getProfileStatus());

            editor.putString(User.KEY_USER_FIRST_NAME, personalInformationObject.getFirstName());
            editor.putString(User.KEY_USER_LAST_NAME, personalInformationObject.getLastName());
            editor.putString(User.KEY_USER_EMAIL, personalInformationObject.getEmailID());
            editor.putString(User.KEY_USER_PHONE_NUMBER, personalInformationObject.getPhoneNumber());
            editor.putString(User.KEY_USER_DOB, personalInformationObject.getDOB());

            editor.putString(User.KEY_USER_LATITUDE, Double.toString(locationObject.getLatitude()));
            editor.putString(User.KEY_USER_LONGITUTDE, Double.toString(locationObject.getLongitude()));
            editor.putString(User.KEY_USER_LOCATION_ADDRESS, locationObject.getAddress());
            editor.putString(User.KEY_USER_LOCATION_POSTALCODE, locationObject.getPostalCode());
            editor.putString(User.KEY_USER_LOCATION_COUNTRY, locationObject.getCountry());

            editor.commit();
            findViewById(R.id.progress_overlay_container).setVisibility(View.GONE);
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }
}
