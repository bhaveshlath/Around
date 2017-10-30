package com.example.blath.around.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.blath.around.R;
import com.example.blath.around.models.Post;

public class HomeSearchFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private String mSearchPostType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home_search, container, false);
        Button sportsButton = (Button) mView.findViewById(R.id.sports_type);
        Button studyButton = (Button) mView.findViewById(R.id.study_type);
        Button travelButton = (Button) mView.findViewById(R.id.travel_type);
        Button concertButton = (Button) mView.findViewById(R.id.concert_type);
        Button otherButton = (Button) mView.findViewById(R.id.other_type);
        sportsButton.setOnClickListener(this);
        studyButton.setOnClickListener(this);
        travelButton.setOnClickListener(this);
        concertButton.setOnClickListener(this);
        otherButton.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View v) {
        selectAndUpdateBackground(v, mSearchPostType);
        switch (v.getId()) {
            case R.id.sports_type:
                mSearchPostType = Post.KEY_TYPE_SPORTS;
                break;
            case R.id.study_type:
                mSearchPostType = Post.KEY_TYPE_STUDY;
                break;
            case R.id.travel_type:
                mSearchPostType = Post.KEY_TYPE_TRAVEL;
                break;
            case R.id.concert_type:
                mSearchPostType = Post.KEY_TYPE_CONCERT;
                break;
            case R.id.other_type:
                mSearchPostType = Post.KEY_TYPE_OTHER;
                break;
        }
    }

    private void selectAndUpdateBackground(View view, String prevSearchPostType) {
        if (prevSearchPostType != null && !prevSearchPostType.isEmpty()) {
            Button prevSearchPostTypeButton = (Button) mView.findViewById(R.id.sports_type);
            switch (prevSearchPostType) {
                case Post.KEY_TYPE_SPORTS:
                    prevSearchPostTypeButton = (Button) mView.findViewById(R.id.sports_type);
                    break;
                case Post.KEY_TYPE_STUDY:
                    prevSearchPostTypeButton = (Button) mView.findViewById(R.id.study_type);
                    break;
                case Post.KEY_TYPE_TRAVEL:
                    prevSearchPostTypeButton = (Button) mView.findViewById(R.id.travel_type);
                    break;
                case Post.KEY_TYPE_CONCERT:
                    prevSearchPostTypeButton = (Button) mView.findViewById(R.id.concert_type);
                    break;
                case Post.KEY_TYPE_OTHER:
                    prevSearchPostTypeButton = (Button) mView.findViewById(R.id.other_type);
                    break;
            }
            prevSearchPostTypeButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
        }
        view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.post_other_background_start_color));
    }
}
