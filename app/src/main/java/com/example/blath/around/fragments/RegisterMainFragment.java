package com.example.blath.around.fragments;

import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import com.example.blath.around.R;
import com.example.blath.around.activities.RegisterActivity;
import com.example.blath.around.commons.Utils.MapUtils;
import com.example.blath.around.commons.Utils.Operations;
import com.example.blath.around.commons.Utils.ResponseOperations;
import com.example.blath.around.commons.Utils.UIUTils;
import com.example.blath.around.events.RegisterUserEvent;
import com.example.blath.around.models.AroundLocation;
import com.example.blath.around.models.User;
import com.example.blath.around.models.UserPersonalInformation;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

import de.greenrobot.event.EventBus;

public class RegisterMainFragment extends Fragment implements
        View.OnClickListener {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private CameraPosition mCameraPosition;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private LatLng mUserLocation = null;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private View mView;
    private FragmentActivity mActivity;

    private MapUtils mMapUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_register_main, container, false);
        mActivity = getActivity();


        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        mMapUtils = new MapUtils(getActivity(), this, TAG, mLastKnownLocation, mCameraPosition, R.id.register_map_search_container, R.id.register_map);

        Switch toggle = (Switch) mView.findViewById(R.id.register_map_loc_button);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View mapSearchContainer = mView.findViewById(R.id.map_search_bar_container);
                if (isChecked) {
                    mapSearchContainer.setVisibility(View.GONE);
                    mMapUtils.getDeviceLocation();
                } else {
                    mapSearchContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        ImageButton datePickerButton = (ImageButton) mView.findViewById(R.id.register_date_picker_button);
        datePickerButton.setOnClickListener(this);

        Button registerButton = (Button) mView.findViewById(R.id.register_button);
        registerButton.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    public void onEvent(RegisterUserEvent result) {
        if (!result.mIsError) {
            UIUTils.showLongToast("Successfully created!!", getActivity());
            RegisterVerificationFragment registerVerificationFragment = new RegisterVerificationFragment();
            this.getFragmentManager().beginTransaction()
                    .replace(R.id.register_container, registerVerificationFragment, TAG)
                    .addToBackStack(null)
                    .commit();
        } else {
            UIUTils.showLongToast(ResponseOperations.getErrorMessage(result.mResponseObject), getActivity());
        }
    }

    private void submitUserDetails(String firstName, String lastName, String emailID, String dateOfBirth, String phoneNumber,
                                   String password, LatLng userLocation) {

        if (firstName.equals("")
                || lastName.equals("")
                || emailID.equals("")
                || dateOfBirth.equals("")
                || phoneNumber.equals("")
                || password.equals("")) {
            UIUTils.showLongToast(getString(R.string.all_details_alert), mActivity);
        } else if (userLocation == null) {
            UIUTils.showLongToast(getString(R.string.location_alert), mActivity);
        } else {
            User newUser = new User(new UserPersonalInformation(firstName, lastName, emailID, dateOfBirth, phoneNumber, password), new AroundLocation(userLocation.latitude, userLocation.longitude), "Pending-Verification");
            Operations.registerUserOperation(newUser);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMapUtils.mGoogleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMapUtils.mGoogleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mMapUtils.mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        mMapUtils.updateLocationUI();
        mMapUtils.getDeviceLocation();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.register_button:
                EditText firstName = (EditText) mView.findViewById(R.id.register_first_name);
                EditText lastName = (EditText) mView.findViewById(R.id.register_last_name);
                EditText emailID = (EditText) mView.findViewById(R.id.register_email);
                EditText phoneNumber = (EditText) mView.findViewById(R.id.register_phone_number);
                EditText password = (EditText) mView.findViewById(R.id.register_password);
                EditText dateOfBirth = (EditText) mView.findViewById(R.id.register_DOB);

                submitUserDetails(firstName.getText().toString(),
                        lastName.getText().toString(),
                        emailID.getText().toString(),
                        dateOfBirth.getText().toString(),
                        phoneNumber.getText().toString(),
                        password.getText().toString(),
                        mMapUtils.getUserLocation());
                break;

            case R.id.register_date_picker_button:
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                EditText dateText = (EditText) mView.findViewById(R.id.register_DOB);
                                dateText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
                break;
        }
    }
}
