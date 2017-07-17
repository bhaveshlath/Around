package com.example.blath.around.fragments;

import com.example.blath.around.R;
import com.example.blath.around.activities.ProfileActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeProfileFragment extends Fragment implements View.OnClickListener{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_profile, container, false);
        View userPosts = view.findViewById(R.id.profile_user_posts_row);
        userPosts.setOnClickListener(this);
        View editProfile = view.findViewById(R.id.profile_edit_profile_row);
        editProfile.setOnClickListener(this);
        View feedback = view.findViewById(R.id.profile_feedback_row);
        feedback.setOnClickListener(this);
        View termsAndService = view.findViewById(R.id.profile_terms_row);
        termsAndService.setOnClickListener(this);
        View logout = view.findViewById(R.id.profile_logout_row);
        logout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
        switch(id){

            case R.id.profile_user_posts_row:
                profileIntent.putExtra("selectedOption", 0);
                getActivity().startActivity(profileIntent);
                break;

            case R.id.profile_edit_profile_row:
                profileIntent.putExtra("selectedOption", 1);
                getActivity().startActivity(profileIntent);
                break;

            case R.id.profile_feedback_row:
                profileIntent.putExtra("selectedOption", 2);
                getActivity().startActivity(profileIntent);
                break;

            case R.id.profile_terms_row:
                profileIntent.putExtra("selectedOption", 3);
                getActivity().startActivity(profileIntent);
                break;

            case R.id.profile_logout_row:
                break;
        }
    }
}
