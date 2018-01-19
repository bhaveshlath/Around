package com.example.blath.around.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.blath.around.R;
import com.example.blath.around.activities.ProfileActivity;
import com.example.blath.around.commons.Utils.PermissionsHelper;
import com.example.blath.around.commons.Utils.UIUtils;

import java.io.File;
import java.io.IOException;


public class ProfileEditProfileFragment extends Fragment implements View.OnClickListener{
    public static final int PROFILE_CAMERA_PERMISSION_GROUP_ID = 2;

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile_edit_profile, container, false);
        mView.findViewById(R.id.profile_user_image).setOnClickListener(this);

//        updateUserProfileItems();
        return  mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UIUtils.animateStatusBarColorTransition(getActivity(), R.color.dropdown_blue, R.color.dropdown_blue);
    }

    private void createImagePickerOptionDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.image_picker_options);

        LinearLayout optionCamera = (LinearLayout) dialog.findViewById(R.id.option_camera);
        LinearLayout optionGallery = (LinearLayout) dialog.findViewById(R.id.option_gallery);
        // if button is clicked, close the custom dialog
        optionCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhotoFromCamera();
                dialog.dismiss();
            }
        });

        optionGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showShortToast("Gallery", getActivity());
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void takePhotoFromCamera() {
        requestRuntimePermissions(PROFILE_CAMERA_PERMISSION_GROUP_ID, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    public ImageView getProfileImageView(){
        return (ImageView)mView.findViewById(R.id.profile_user_image);
    }

    public void requestRuntimePermissions(int permissionCode, String... permissions) {
        if (PermissionsHelper.hasPermissions(getActivity(), android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            launchCamera();
        } else {
            View view = getView();
            if (null != view) {

                PermissionsHelper.checkRunTimePermissions(ProfileEditProfileFragment.this, getView(), permissionCode, permissions);
            }
        }
    }

    private void launchCamera() {
        final String FRONT_CAMERA = "android.intent.extras.CAMERA_FACING";
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File photoFile = ((ProfileActivity) getActivity()).createProfileImageFile();
            // Need to use FileProvider after updating targetSdkVersion to 24
            // Reference https://inthecheesefactory.com/blog/how-to-share-access-to-file-with-fileprovider-on-android-nougat/en
            Uri photoURI = FileProvider.getUriForFile(getContext(), "com.example.blath.around.fileprovider", photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            // preferably use front camera for taking the profile photo.
            // Note:
            //     this approach is not working on all android devices.
            //     if the oem doesn't support this, the below parameter will be ignored.
            cameraIntent.putExtra(FRONT_CAMERA, android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
            getActivity().startActivityForResult(cameraIntent, ProfileActivity.REQUEST_PICK_FROM_CAMERA);
        } catch (IOException ex) {
            // Handle I/O exception
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile_user_image:
                createImagePickerOptionDialog();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PROFILE_CAMERA_PERMISSION_GROUP_ID:
                if (PermissionsHelper.hasPermissions(getActivity(), permissions)) {
                    launchCamera();
                }
                break;
        }
    }
}
