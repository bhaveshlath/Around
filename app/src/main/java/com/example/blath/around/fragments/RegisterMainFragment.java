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
import com.example.blath.around.commons.Utils.DateUtils;
import com.example.blath.around.commons.Utils.MapUtils;
import com.example.blath.around.commons.Utils.Operations;
import com.example.blath.around.commons.Utils.RequestOperations;
import com.example.blath.around.commons.Utils.ResponseOperations;
import com.example.blath.around.commons.Utils.UIUtils;
import com.example.blath.around.events.RegisterUserEvent;
import com.example.blath.around.models.AroundLocation;
import com.example.blath.around.models.User;
import com.example.blath.around.models.UserPersonalInformation;
import com.google.android.gms.maps.model.CameraPosition;

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
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private View mView;
    private FragmentActivity mActivity;

    private MapUtils mMapUtils;
    private static int mInvalidField;

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

        mMapUtils = new MapUtils(mActivity, this, TAG, mLastKnownLocation, mCameraPosition, R.id.register_place_auto_complete, R.id.register_map);

        Switch toggle = (Switch) mView.findViewById(R.id.register_map_switch);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View mapSearchContainer = mView.findViewById(R.id.register_map_search_bar);
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
        UIUtils.hideKeyboard(getActivity());

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

    public void onEventMainThread(RegisterUserEvent result) {
        if (!result.mIsError) {
            UIUtils.showLongToast("Successfully created!!", mActivity);
            RegisterVerificationFragment registerVerificationFragment = new RegisterVerificationFragment();
            this.getFragmentManager().beginTransaction()
                    .replace(R.id.register_container, registerVerificationFragment, TAG)
                    .addToBackStack(null)
                    .commit();
        } else {
            UIUtils.showLongToast(ResponseOperations.getErrorMessage(result.mResponseObject), mActivity);
        }
    }

    private void submitUserDetails(String firstName, String lastName, String emailID, String dateOfBirth, String phoneNumber,
                                   String password, AroundLocation userLocation) {

        if (firstName.equals("")
                || lastName.equals("")
                || emailID.equals("")
                || dateOfBirth.equals("")
                || phoneNumber.equals("")
                || password.equals("")) {
            UIUtils.showLongToast(getString(R.string.all_details_alert), mActivity);
        } else if (userLocation == null) {
            UIUtils.showLongToast(getString(R.string.location_alert), mActivity);
        } else if (isRegisterDataValid(emailID, phoneNumber, dateOfBirth.substring(dateOfBirth.lastIndexOf(',') + 2)) != 0) {
            switch (mInvalidField) {
                case 1:
                    UIUtils.showLongToast(getString(R.string.invalid_email_message), mActivity);
                    break;

                case 2:
                    UIUtils.showLongToast(getString(R.string.invalid_dob_message), mActivity);
                    break;

                case 3:
                    UIUtils.showLongToast(getString(R.string.invalid_phone_number_message), mActivity);
                    break;
            }
        } else {
            User newUser = new User(new UserPersonalInformation(firstName, lastName, emailID, dateOfBirth, phoneNumber, password), userLocation, "Pending-Verification");
            Operations.registerUserOperation(newUser);
        }
    }

    private static int isRegisterDataValid(String emailID, String phoneNumber, String dateOfBirth) {
        mInvalidField = RequestOperations.isRegisterDataValid(emailID, phoneNumber, dateOfBirth);
        return mInvalidField;
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
        mMapUtils.mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMapUtils.mLocationPermissionGranted = true;
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
                        new AroundLocation(mMapUtils.getUserLocation().getLatitude(),
                                mMapUtils.getUserLocation().getLongitude(),
                                mMapUtils.getUserLocation().getAddress() == null ? "" : mMapUtils.getUserLocation().getAddress(),
                                mMapUtils.getUserLocation().getPostalCode() == null ? "" : mMapUtils.getUserLocation().getPostalCode(),
                                mMapUtils.getUserLocation().getCountry() == null ? "" : mMapUtils.getUserLocation().getCountry()));
                break;

            case R.id.register_date_picker_button:
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, android.R.style.Theme_DeviceDefault_Light_Dialog,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                EditText dateText = (EditText) mView.findViewById(R.id.register_DOB);
                                dateText.setText(DateUtils.dateFormatter(monthOfYear, dayOfMonth, year));
                            }
                        }, year, month, day);
                datePickerDialog.show();
                break;
        }
    }
}
