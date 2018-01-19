package com.example.blath.around.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.blath.around.R;
import com.example.blath.around.commons.Utils.CircleTransform;
import com.example.blath.around.commons.Utils.app.AroundAppHandles;
import com.example.blath.around.fragments.HomeProfileFragment;
import com.example.blath.around.fragments.ProfileEditProfileFragment;
import com.example.blath.around.fragments.ProfileFeedbackFragment;
import com.example.blath.around.fragments.ProfileTermsFragment;
import com.example.blath.around.fragments.ProfileUserPostsFragment;
import com.example.blath.around.models.User;

import java.io.File;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
    public static final int REQUEST_PICK_FROM_CAMERA = 1; // choose profile photo from camera
    public static final int REQUEST_PICK_FROM_EXISTING = 2; // choose profile photo from gallery
    public static final int REQUEST_CROPPED_IMAGE = 3; // request cropped image
    public static final String INTENT_EXTRA_FRAGMENT_ARGS = "fragment_args";
    private static final String IMAGE_FILE_TYPE = ".jpeg";
    private static final String AROUND_PATH = "/Around";
    private static final String AROUND_PROFILE_IMAGE_PATH = "/Profile Images";

    private File mProfilePhotoFile;
    ProfileEditProfileFragment mProfileEditProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        switch (getIntent().getExtras().getInt(HomeProfileFragment.KEY_SELECTED_OPTION)){
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
        String picturesStorageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        File aroundPictureDirectory = new File(picturesStorageDir + AROUND_PATH + AROUND_PROFILE_IMAGE_PATH);
        aroundPictureDirectory.mkdirs();
        SharedPreferences userDetails = this.getSharedPreferences(AroundAppHandles.AROUND_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        String userId = userDetails.getString(User.KEY_USER_ID, "001100110011");
        mProfilePhotoFile = File.createTempFile(
                userId + "_",
                IMAGE_FILE_TYPE,
                aroundPictureDirectory
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
               byte[] croppedImageByteArray = data.getByteArrayExtra(INTENT_EXTRA_FRAGMENT_ARGS);
                if(croppedImageByteArray != null) {
                    Bitmap croppedImageBitmap = BitmapFactory.decodeByteArray(croppedImageByteArray, 0, croppedImageByteArray.length);
                    CircleTransform circleTransform =  new CircleTransform();
                    croppedImageBitmap = circleTransform.transform(croppedImageBitmap);
                    mProfileEditProfileFragment.getProfileImageView().setImageBitmap(croppedImageBitmap);
                }
                break;
            default:
                break;
        }
        if (photoUri != null) {
            Log.d("Photo Path1:", photoUri.toString());
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
