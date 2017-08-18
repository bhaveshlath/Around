package com.example.blath.around.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.blath.around.R;
import com.example.blath.around.commons.Utils.Operations;
import com.example.blath.around.commons.Utils.ResponseOperations;
import com.example.blath.around.commons.Utils.UIUtils;
import com.example.blath.around.commons.Utils.app.AroundApplication;
import com.example.blath.around.events.LoginUserEvent;
import com.example.blath.around.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

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
    switch(v.getId()){
        case R.id.login_button:
            EditText username = (EditText) findViewById(R.id.username);
            EditText password = (EditText) findViewById(R.id.password);
            String usernameText = username.getText().toString();
            String passwordText = password.getText().toString();
            loginUser(usernameText, passwordText);
            break;

        case R.id.signup_button:
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            break;
    }
}

    private boolean loginUser(String username, String password){
        if(username.equals("")){
            UIUtils.showShortToast("Please enter username", this);
        } else if(password.equals("")){
            UIUtils.showShortToast("Please enter password", this);
        }else{
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
        if (result.mIsError) {
            UIUtils.showLongToast(ResponseOperations.getErrorMessage(result.mResponseObject), this);
        } else {

            SharedPreferences sharedPref = getSharedPreferences(AroundApplication.AROUND_SHARED_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            try{
                JSONObject userObject = result.mResponseObject.getJSONObject("userDetails");
                JSONObject personalInformationObject = userObject.getJSONObject("mUserPersonalInformation");
                JSONObject locationObject = userObject.getJSONObject("mLocation");

                editor.putString(User.KEY_USER_PROFILE_STATUS, userObject.getString("mProfileStatus"));

                editor.putString(User.KEY_USER_FIRST_NAME, personalInformationObject.getString("mFirstName"));
                editor.putString(User.KEY_USER_LAST_NAME, personalInformationObject.getString("mLastName"));
                editor.putString(User.KEY_USER_EMAIL, personalInformationObject.getString("mEmailID"));
                editor.putString(User.KEY_USER_PHONE_NUMBER, personalInformationObject.getString("mPhoneNumber"));
                editor.putString(User.KEY_USER_DOB, personalInformationObject.getString("mDOB"));

                editor.putString(User.KEY_USER_LATITUDE, locationObject.getString("mLatitude"));
                editor.putString(User.KEY_USER_LONGITUTDE, locationObject.getString("mLongitude"));
                editor.putString(User.KEY_USER_LOCATION_ADDRESS, locationObject.getString("mAddress"));
                editor.putString(User.KEY_USER_LOCATION_POSTALCODE, locationObject.getString("mPostalCode"));
                editor.putString(User.KEY_USER_LOCATION_COUNTRY, locationObject.getString("mCountry"));
            } catch (JSONException e){
                e.getStackTrace();
            }

            editor.commit();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }
}
