package com.example.blath.around.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.blath.around.R;
import com.example.blath.around.commons.Utils.UIUtils;
import com.example.blath.around.fragments.NewPostTypeFragment;

public class NewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        UIUtils.animateStatusBarColorTransition(this, R.color.around_background_end_color, R.color.around_background_end_color);
        if (findViewById(R.id.new_post_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            NewPostTypeFragment newPostTypeFragment = new NewPostTypeFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.new_post_container, newPostTypeFragment).commit();
        }
    }
}
