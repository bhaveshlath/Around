package com.example.blath.around.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.blath.around.R;
import com.example.blath.around.fragments.NewPostMainFragment;
import com.example.blath.around.fragments.ProfileEditProfileFragment;
import com.example.blath.around.fragments.ProfileFeedbackFragment;
import com.example.blath.around.fragments.ProfileTermsFragment;
import com.example.blath.around.fragments.ProfileUserPostsFragment;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        switch (getIntent().getExtras().getInt("selectedOption")){
            case 0:
                ProfileUserPostsFragment profileUserPostsFragment = new ProfileUserPostsFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.profile_container, profileUserPostsFragment).commit();
                break;

            case 1:
                ProfileEditProfileFragment profileEditProfileFragment = new ProfileEditProfileFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.profile_container, profileEditProfileFragment).commit();
                break;

            case 2:
                ProfileFeedbackFragment profileFeedbackFragment = new ProfileFeedbackFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.profile_container, profileFeedbackFragment).commit();
                break;

            case 3:
                ProfileTermsFragment profileTermsFragment = new ProfileTermsFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.profile_container, profileTermsFragment).commit();
                break;
        }
    }
}
