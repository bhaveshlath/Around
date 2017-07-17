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
import com.example.blath.around.commons.Utils.UIUTils;
import com.example.blath.around.events.LoginUserEvent;

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
            UIUTils.showShortToast("Please enter username", this);
        } else if(password.equals("")){
            UIUTils.showShortToast("Please enter password", this);
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
            UIUTils.showLongToast(ResponseOperations.getErrorMessage(result.mResponseObject), this);
        } else {

            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            try{
                JSONObject userObject = result.mResponseObject.getJSONObject("userDetails");
                JSONObject personalInformationObject = userObject.getJSONObject("mUserPersonalInformation");
                JSONObject locationObject = userObject.getJSONObject("mLocation");

                editor.putString(getString(R.string.first_name), personalInformationObject.getString("mFirstName"));
                editor.putString(getString(R.string.last_name), personalInformationObject.getString("mLastName"));
                editor.putString(getString(R.string.email), personalInformationObject.getString("mEmailID"));
                editor.putString(getString(R.string.phone_number), personalInformationObject.getString("mPhoneNumber"));
                editor.putString(getString(R.string.date_of_birth), personalInformationObject.getString("mDOB"));

                editor.putLong(getString(R.string.latitude), locationObject.getLong("mLatitude"));
                editor.putLong(getString(R.string.longitude), locationObject.getLong("mLongitude"));
            } catch (JSONException e){
                e.getStackTrace();
            }

            editor.commit();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }
}
