package com.example.blath.around.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.blath.around.R;
import com.example.blath.around.commons.Utils.UIUtils;
import com.example.blath.around.commons.Utils.app.AroundUtils;

public class NewPostTypeFragment extends Fragment implements View.OnClickListener{
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new_post_type, container, false);

        Button sportButton = (Button) mView.findViewById(R.id.sports_btn);
        Button concertButton = (Button) mView.findViewById(R.id.concert_btn);
        Button travelButton = (Button) mView.findViewById(R.id.travel_btn);
        Button studyButton = (Button) mView.findViewById(R.id.study_btn);
        Button otherButton = (Button) mView.findViewById(R.id.other_btn);

        sportButton.setOnClickListener(this);
        concertButton.setOnClickListener(this);
        travelButton.setOnClickListener(this);
        studyButton.setOnClickListener(this);
        otherButton.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sports_btn:
                AroundUtils.setAroundPostRequestType(AroundUtils.AroundPostRequestType.SPORTS);
                startMainFragment();
                break;
            case R.id.concert_btn:
                AroundUtils.setAroundPostRequestType(AroundUtils.AroundPostRequestType.CONCERT);
                startMainFragment();
                break;
            case R.id.travel_btn:
                AroundUtils.setAroundPostRequestType(AroundUtils.AroundPostRequestType.TRAVEL);
                startMainFragment();
                break;
            case R.id.study_btn:
                AroundUtils.setAroundPostRequestType(AroundUtils.AroundPostRequestType.STUDY);
                startMainFragment();
                break;
            case R.id.other_btn:
                AroundUtils.setAroundPostRequestType(AroundUtils.AroundPostRequestType.OTHER);
                startMainFragment();
                break;
        }
    }

    private void startMainFragment(){
        NewPostMainFragment newPostMainFragment = new NewPostMainFragment();
        getFragmentManager().beginTransaction().replace(R.id.new_post_container, newPostMainFragment).addToBackStack("NewPostMain").commit();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView toolbarTitle = (TextView) mView.findViewById(R.id.toolbar_title);
        UIUtils.showToolbar(mView, toolbarTitle, getString(R.string.request_type), getString(R.string.select_post_type), R.drawable.back_icon_white, true, new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        },  R.id.toolbar_title);
        toolbarTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
    }
}
