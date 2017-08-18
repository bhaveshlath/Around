package com.example.blath.around.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.view.ViewCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.blath.around.R;
import com.example.blath.around.adapters.NewPostSportAdapter;
import com.example.blath.around.commons.Utils.DateUtils;
import com.example.blath.around.commons.Utils.MapUtils;
import com.example.blath.around.commons.Utils.RequestOperations;
import com.example.blath.around.commons.Utils.UIUtils;
import com.example.blath.around.models.AgeRange;
import com.example.blath.around.models.Post;
import com.google.android.gms.maps.model.CameraPosition;

import java.util.Calendar;


public class NewPostMainFragment extends Fragment implements View.OnClickListener {

    public static final String KEY_NEW_POST = "key_new_post";
    private static final String TAG = NewPostMainFragment.class.getSimpleName();

    private int[] mSportNames = { R.string.baseball, R.string.cricket, R.string.basketball, R.string.soccer,
            R.string.ping_pong, R.string.tennis, R.string.football, R.string.badminton, R.string.gym,
            R.string.lacrosse, R.string.volleyball, R.string.bowling, R.string.boxing, R.string.ice_hockey };
    private int[] mSportIcons = { R.drawable.baseball, R.drawable.cricket, R.drawable.basketball, R.drawable.soccer,
            R.drawable.ping_pong, R.drawable.tennis, R.drawable.soccer, R.drawable.badminton, R.drawable.gym,
            R.drawable.lacrosse, R.drawable.volleyball, R.drawable.bowling, R.drawable.boxing, R.drawable.ice_hockey };

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

    private TextView mSportName, mDate, mTime;
    private EditText mAgeRangeMin, mAgeRangeMax;
    private String mGenderPreference;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMapUtils.mGoogleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMapUtils.mGoogleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mMapUtils.mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_new_post_main, container, false);
        mActivity = getActivity();

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        mSportName = (TextView) mView.findViewById(R.id.sport_name_content);
        GridView sportsGrid = (GridView) mView.findViewById(R.id.sports_grid);
        sportsGrid.setAdapter(new NewPostSportAdapter(mActivity, mSportNames, mSportIcons, mSportName));
        ViewCompat.setNestedScrollingEnabled(sportsGrid, true);

        mMapUtils = new MapUtils(mActivity, this, TAG, mLastKnownLocation, mCameraPosition, R.id.new_post_place_auto_complete, R.id.new_post_map);

        Switch toggle = (Switch) mView.findViewById(R.id.new_post_map_switch);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View mapSearchContainer = mView.findViewById(R.id.new_post_map_search_bar);
                if (isChecked) {
                    mapSearchContainer.setVisibility(View.GONE);
                    mMapUtils.getDeviceLocation();
                } else {
                    mapSearchContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        View datePicker = mView.findViewById(R.id.new_post_date_button);
        datePicker.setOnClickListener(this);
        View timePicker = mView.findViewById(R.id.new_post_time_button);
        timePicker.setOnClickListener(this);
        mDate = (TextView) mView.findViewById(R.id.new_post_date_txt);
        mTime = (TextView) mView.findViewById(R.id.new_post_time_txt);
        mAgeRangeMin = (EditText) mView.findViewById(R.id.age_range_min_years);
        mAgeRangeMin.setTransformationMethod(null);
        mAgeRangeMax = (EditText) mView.findViewById(R.id.age_range_max_years);
        mAgeRangeMax.setTransformationMethod(null);

        Spinner genderSelectionSpinner = (Spinner) mView.findViewById(R.id.gender_selection_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mActivity,
                R.array.gender_preference_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSelectionSpinner.setAdapter(adapter);

        genderSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGenderPreference = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGenderPreference = "";
            }
        });

        UIUtils.hideKeyboard(getActivity());
        View nextButton = mView.findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UIUtils.showToolbar(mView, (TextView) mView.findViewById(R.id.toolbar_title), getString(R.string.new_post), getString(R.string.please_fill), R.drawable.back_icon_white, true, R.id.toolbar_title);
        UIUtils.animateStatusBarColorTransition(mActivity, R.color.dropdown_blue, R.color.dropdown_blue);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        final Calendar c = Calendar.getInstance();
        switch (id) {
            case R.id.new_post_date_button:
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, android.R.style.Theme_DeviceDefault_Light_Dialog,
                        dateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
                break;

            case R.id.new_post_time_button:
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(mActivity, android.R.style.Theme_DeviceDefault_Light_Dialog,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mTime.setText(DateUtils.timeFormatter(hourOfDay + ":" + minute));
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
                break;

            case R.id.next_button:
                EditText descriptionText = (EditText) mView.findViewById(R.id.new_post_description);
                String verifyResultString = RequestOperations.verifyPostDetails(getActivity(), mSportName.getText().toString(), mDate.getText().toString(), mTime.getText().toString(),
                        mAgeRangeMin.getText().toString(), mAgeRangeMax.getText().toString(), mGenderPreference, descriptionText.getText().toString(), mMapUtils.getUserLocation());
                if(verifyResultString.equals(getString(R.string.success))){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(KEY_NEW_POST, new Post(RequestOperations.getUserObject(getActivity()),
                            Post.KEY_TYPE_SPORTS, mSportName.getText().toString(),
                            mMapUtils.getUserLocation(),
                            new AgeRange(Integer.parseInt(mAgeRangeMin.getText().toString()), Integer.parseInt(mAgeRangeMax.getText().toString())),
                            mGenderPreference, descriptionText.getText().toString(), DateUtils.getDateRange(mDate.getText().toString()), mTime.getText().toString()));
                    NewPostReviewFragment newPostReviewFragment = new NewPostReviewFragment();
                    newPostReviewFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.new_post_container, newPostReviewFragment).addToBackStack(null).commit();
                }else{
                    UIUtils.showAlertDialog(getActivity(), "", verifyResultString);
                }
                break;
        }
    }

    final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year,
                              int monthOfYear, int dayOfMonth) {
            view.setMinDate(System.currentTimeMillis() - 1000);
            mDate.setText(DateUtils.dateFormatter(monthOfYear, dayOfMonth, year));
        }
    };

    /**
     * Handles the result of the request for location permissions.
     */
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
}
