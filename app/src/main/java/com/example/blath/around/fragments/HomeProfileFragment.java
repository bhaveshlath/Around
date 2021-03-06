package com.example.blath.around.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blath.around.R;
import com.example.blath.around.activities.LoginActivity;
import com.example.blath.around.activities.ProfileActivity;
import com.example.blath.around.commons.Utils.CircleTransform;
import com.example.blath.around.commons.Utils.app.AroundAppHandles;
import com.example.blath.around.models.User;

public class HomeProfileFragment extends Fragment implements View.OnClickListener, DialogInterface.OnDismissListener{

    public static final String KEY_SELECTED_OPTION = "selected_option";
    private View mView;
    private boolean mLogout;
    private FragmentActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home_profile, container, false);
        View userPosts = mView.findViewById(R.id.profile_user_posts_row);
        userPosts.setOnClickListener(this);
        View editProfile = mView.findViewById(R.id.profile_edit_profile_row);
        editProfile.setOnClickListener(this);
        View feedback = mView.findViewById(R.id.profile_feedback_row);
        feedback.setOnClickListener(this);
        View termsAndService = mView.findViewById(R.id.profile_terms_row);
        termsAndService.setOnClickListener(this);
        View logout = mView.findViewById(R.id.profile_logout_row);
        logout.setOnClickListener(this);

        updateUserProfileItems();
        return mView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent profileIntent = new Intent(mActivity, ProfileActivity.class);
        switch(id){
            case R.id.profile_user_posts_row:
                profileIntent.putExtra(KEY_SELECTED_OPTION, 0);
                mActivity.startActivity(profileIntent);
                break;

            case R.id.profile_edit_profile_row:
                profileIntent.putExtra(KEY_SELECTED_OPTION, 1);
                mActivity.startActivity(profileIntent);
                break;

            case R.id.profile_feedback_row:
                profileIntent.putExtra(KEY_SELECTED_OPTION, 2);
                mActivity.startActivity(profileIntent);
                break;

            case R.id.profile_terms_row:
                profileIntent.putExtra(KEY_SELECTED_OPTION, 3);
                mActivity.startActivity(profileIntent);
                break;

            case R.id.profile_logout_row:
                createImagePickerOptionDialog();
                break;
        }
    }

    public void updateUserProfileItems(){
        SharedPreferences userDetails = mActivity.getSharedPreferences(AroundAppHandles.AROUND_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        String usernameString = userDetails.getString(User.KEY_USERNAME, "Bhavesh Lath");
        String profileImageURL = userDetails.getString(User.KEY_USER_PROFILE_IMAGE, User.KEY_DEFAULT_PROFILE_ICON);
        ImageView userProfileImageView = (ImageView) mView.findViewById(R.id.user_profile_image);
        TextView username = (TextView) mView.findViewById(R.id.user_name);

        AroundAppHandles.getImageUtils().loadImage(profileImageURL, userProfileImageView, new CircleTransform());
        username.setText(usernameString);
    }

    private void createImagePickerOptionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.logout_heading)
                .setMessage(R.string.logout_message)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mLogout = true;
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setOnDismissListener(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(mLogout){
            SharedPreferences sharedPref = mActivity.getSharedPreferences(AroundAppHandles.AROUND_SHARED_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.commit();
            Intent logoutIntent = new Intent(mActivity, LoginActivity.class);
            logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logoutIntent);
        }
    }
}
