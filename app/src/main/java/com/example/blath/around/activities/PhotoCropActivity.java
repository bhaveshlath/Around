package com.example.blath.around.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.blath.around.R;
import com.example.blath.around.commons.Utils.UIUtils;
import com.example.blath.around.fragments.PhotoCropFragment;

public class PhotoCropActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_crop);

        UIUtils.animateStatusBarColorTransition(this, R.color.around_background_end_color, R.color.around_background_end_color);
        if (findViewById(R.id.photo_crop_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            PhotoCropFragment photoCropFragment = new PhotoCropFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.photo_crop_container, photoCropFragment).commit();
        }
    }
}
