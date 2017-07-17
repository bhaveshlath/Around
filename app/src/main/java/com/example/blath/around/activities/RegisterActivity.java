package com.example.blath.around.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.blath.around.R;
import com.example.blath.around.fragments.RegisterMainFragment;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (findViewById(R.id.register_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            RegisterMainFragment registerMainFragment = new RegisterMainFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.register_container, registerMainFragment).commit();

        }
    }
}
