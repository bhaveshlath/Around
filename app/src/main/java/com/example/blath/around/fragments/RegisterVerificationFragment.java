package com.example.blath.around.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.blath.around.R;
import com.example.blath.around.commons.Utils.UIUtils;

public class RegisterVerificationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_verification, container, false);

        Button gotItButton = (Button)view.findViewById(R.id.verification_button);
        gotItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        UIUtils.hideKeyboard(getActivity());
        return view;
    }

}
