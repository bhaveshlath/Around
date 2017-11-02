package com.example.blath.around.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.example.blath.around.R;
import com.example.blath.around.commons.Utils.CircleTransform;
import com.example.blath.around.commons.Utils.app.AroundAppHandles;
import com.example.blath.around.fragments.ProfileEditProfileFragment;
import com.example.blath.around.fragments.ProfileFeedbackFragment;
import com.example.blath.around.fragments.ProfileTermsFragment;
import com.example.blath.around.fragments.ProfileUserPostsFragment;

import java.io.File;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
    public static final int REQUEST_PICK_FROM_CAMERA = 1; // choose profile photo from camera
    public static final int REQUEST_PICK_FROM_EXISTING = 2; // choose profile photo from gallery
    public static final int REQUEST_CROPPED_IMAGE = 3; // request cropped image
    private static final String PROFILE_IMAGE_PREFIX = "PROFILE_IMAGE";
    public static final String INTENT_EXTRA_FRAGMENT_ARGS = "fragment_args";

    private File mProfilePhotoFile;
    ProfileEditProfileFragment mProfileEditProfileFragment;

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
                mProfileEditProfileFragment = new ProfileEditProfileFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.profile_container, mProfileEditProfileFragment).commit();
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


    // When user takes a photo, it will be saved in the File specified here
    public File createProfileImageFile() throws IOException {
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        mProfilePhotoFile = File.createTempFile(
                PROFILE_IMAGE_PREFIX,
                ".jpg",
                storageDir
        );
        //noinspection ResultOfMethodCallIgnored
        mProfilePhotoFile.delete(); // we just need a temporary name
        return mProfilePhotoFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        Uri photoUri = null;
        switch (requestCode) {
            case REQUEST_PICK_FROM_CAMERA:
                if (mProfilePhotoFile != null) {
                    photoUri = Uri.fromFile(mProfilePhotoFile);
                }
                break;
            case REQUEST_PICK_FROM_EXISTING:
                if (data != null) {
                    photoUri = data.getData();
                }
                break;
            case REQUEST_CROPPED_IMAGE:
               Uri croppedImageUri = data.getExtras().getParcelable(INTENT_EXTRA_FRAGMENT_ARGS);
                if(croppedImageUri != null) {
                    AroundAppHandles.getImageUtils().loadImage(croppedImageUri, mProfileEditProfileFragment.getProfileImageView(), new CircleTransform());
                }
            default:
                break;
        }
        if (photoUri != null) {
            doCropImage(photoUri);
        }
    }

    public boolean doCropImage(Uri uri) {
        Intent intent = new Intent(ProfileActivity.this, PhotoCropActivity.class);
        intent.putExtra(INTENT_EXTRA_FRAGMENT_ARGS, uri);
        startActivityForResult(intent, REQUEST_CROPPED_IMAGE);
        return true;
    }
}
