package com.example.blath.around.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
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
import android.widget.LinearLayout;
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
import com.example.blath.around.commons.Utils.app.AroundUtils;
import com.example.blath.around.models.AgeRange;
import com.example.blath.around.models.AroundLocation;
import com.example.blath.around.models.Post;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.blath.around.R.id.post_subtitle_edit;
import static com.example.blath.around.R.id.sports_grid;


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

    private TextView mDate, mTime;
    private EditText mAgeRangeMin, mAgeRangeMax;
    private String mGenderPreference;
    private String mPostType;
    private AroundUtils.AroundPostRequestType mAroundPostRequestType;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMapUtils.mGoogleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMapUtils.mGoogleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mMapUtils.mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAroundPostRequestType = AroundUtils.getAroundPostRequestType();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_new_post_main, container, false);
        }

        mActivity = getActivity();

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        //To handle already created GoogleApiClient in case user comes back from review screen
        if (mMapUtils == null) {
            mMapUtils = new MapUtils(mActivity, this, TAG, mLastKnownLocation, mCameraPosition, R.id.new_post_place_auto_complete, R.id.new_post_map);
        }

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

        updateTitle();
        setPlaceAutocompleteFragmentListener();

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

        UIUtils.showToolbar(mView, (TextView) mView.findViewById(R.id.toolbar_title), getString(R.string.new_post), null, R.drawable.back_icon_white, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        }, R.id.toolbar_title);
        UIUtils.animateStatusBarColorTransition(mActivity, R.color.around_background_end_color, R.color.around_background_end_color);
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
                String verifyResultString = RequestOperations.verifyPostDetails(getActivity(), getTitleContent(), getSubTitleContent(), mDate.getText().toString(), mTime.getText().toString(),
                        mAgeRangeMin.getText().toString(), mAgeRangeMax.getText().toString(), mGenderPreference, descriptionText.getText().toString(), mMapUtils.getUserLocation(), mAroundPostRequestType);
                if (verifyResultString.equals(getString(R.string.success))) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(KEY_NEW_POST, new Post(RequestOperations.getUserObject(getActivity()),
                            mPostType,
                            getTitleContent(),
                            getSubTitleContent(),
                            mMapUtils.getUserLocation(),
                            new AgeRange(Integer.parseInt(mAgeRangeMin.getText().toString()), Integer.parseInt(mAgeRangeMax.getText().toString())),
                            mGenderPreference, descriptionText.getText().toString(), DateUtils.getDateRange(mDate.getText().toString()), mTime.getText().toString(), 0, new ArrayList<String>()));
                    NewPostReviewFragment newPostReviewFragment = new NewPostReviewFragment();
                    newPostReviewFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.new_post_container, newPostReviewFragment).addToBackStack(null).commit();
                } else {
                    UIUtils.showAlertDialogNeutral(getActivity(), getString(R.string.incomplete), verifyResultString);
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

    private void updateTitle() {
        TextView postTitle = (TextView) mView.findViewById(R.id.post_title);
        LinearLayout postSubtitleContainer = (LinearLayout) mView.findViewById(R.id.post_subtitle_container);
        TextView postSubtitle = (TextView) mView.findViewById(R.id.post_subtitle);
        Resources resources = getResources();

        switch (mAroundPostRequestType) {
            case SPORTS:
                postTitle.setText(resources.getString(R.string.sport_name));
                TextView postTitleContent = (TextView) mView.findViewById(R.id.post_title_content);
                postTitleContent.setVisibility(View.VISIBLE);
                GridView sportsGrid = (GridView) mView.findViewById(sports_grid);
                sportsGrid.setAdapter(new NewPostSportAdapter(mActivity, mSportNames, mSportIcons, postTitleContent));
                ViewCompat.setNestedScrollingEnabled(sportsGrid, true);
                sportsGrid.setVisibility(View.VISIBLE);
                EditText postTitleEditText = (EditText) mView.findViewById(R.id.post_title_edit);
                postTitleEditText.setVisibility(View.GONE);
                mPostType = Post.KEY_TYPE_SPORTS;
                break;
            case STUDY:
                postTitle.setText(resources.getString(R.string.study_subject));
                postSubtitleContainer.setVisibility(View.VISIBLE);
                postSubtitle.setText(resources.getString(R.string.study_topic));
                mPostType = Post.KEY_TYPE_STUDY;
                break;
            case CONCERT:
                postTitle.setText(resources.getString(R.string.concert_band));
                mPostType = Post.KEY_TYPE_CONCERT;
                break;
            case TRAVEL:
                postTitle.setText(resources.getString(R.string.travel_from));
                postSubtitleContainer.setVisibility(View.VISIBLE);
                postSubtitle.setText(resources.getString(R.string.travel_to));
                mPostType = Post.KEY_TYPE_TRAVEL;
                break;
            case OTHER:
                postTitle.setText(resources.getString(R.string.other_post_for));
                mPostType = Post.KEY_TYPE_OTHER;
                break;
        }
    }

    private String getTitleContent() {
        String title = "";
        if (AroundUtils.AroundPostRequestType.SPORTS.equals(mAroundPostRequestType)) {
            TextView postTitle = (TextView) mView.findViewById(R.id.post_title_content);
            title = postTitle.getText().toString();
        } else {
            EditText postTitleContent = (EditText) mView.findViewById(R.id.post_title_edit);
            title = postTitleContent.getText().toString();
        }
        return title;
    }

    private String getSubTitleContent() {
        String subtitle = "";
        if (AroundUtils.AroundPostRequestType.SPORTS.equals(mAroundPostRequestType)) {
            TextView postTitle = (TextView) mView.findViewById(R.id.post_title_content);
            subtitle = postTitle.getText().toString();
        } else {
            EditText postSubtitleContent = (EditText) mView.findViewById(post_subtitle_edit);
            subtitle = postSubtitleContent.getText().toString();
        }
        return subtitle;
    }

    private void setPlaceAutocompleteFragmentListener() {
        PlaceAutocompleteFragment supportPlaceAutocompleteFragment = (PlaceAutocompleteFragment) mActivity.getFragmentManager().findFragmentById(R.id.new_post_place_auto_complete);
        ((EditText) supportPlaceAutocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextColor(Color.parseColor("#FFFFFF"));
        supportPlaceAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng latLng = place.getLatLng();
                double lat = latLng.latitude;
                double lng = latLng.longitude;
                String[] addressData = mMapUtils.getAddress(lat, lng);
                mMapUtils.mUserLocation = new AroundLocation(lat, lng, addressData[0], addressData[1], addressData[2]);
                mMapUtils.gotoLocation(new LatLng(lat, lng), mMapUtils.DEFAULT_ZOOM);
                mMapUtils.displayMarker(lat, lng);
            }

            @Override
            public void onError(Status status) {
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PlaceAutocompleteFragment f = (PlaceAutocompleteFragment) mActivity.getFragmentManager()
                .findFragmentById(R.id.new_post_place_auto_complete);
        if (f != null) {
            mActivity.getFragmentManager().beginTransaction().remove(f).commit();
        }
        mMapUtils.stopDestroyGoogleClient();
    }
}
