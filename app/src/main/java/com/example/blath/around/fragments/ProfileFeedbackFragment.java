package com.example.blath.around.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blath.around.R;
import com.example.blath.around.commons.Utils.UIUtils;


public class ProfileFeedbackFragment extends Fragment {
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile_feedback, container, false);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UIUtils.showToolbar(mView, (TextView) mView.findViewById(R.id.toolbar_title), getString(R.string.feedback), null, R.drawable.back_icon_white, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        }, R.id.toolbar_title);
        UIUtils.animateStatusBarColorTransition(getActivity(), R.color.around_background_end_color, R.color.around_background_end_color);
    }
}
