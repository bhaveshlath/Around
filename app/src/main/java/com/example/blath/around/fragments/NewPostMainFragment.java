package com.example.blath.around.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.ActivityCompat;
import android.support.v13.view.ViewCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.blath.around.R;
import com.example.blath.around.adapters.NewPostSportAdapter;
import com.example.blath.around.commons.Utils.UIUTils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;


public class NewPostMainFragment extends Fragment implements View.OnClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = NewPostMainFragment.class.getSimpleName();

    View mView;
    private int[] mSportNames = { R.string.baseball, R.string.cricket, R.string.basketball, R.string.soccer,
            R.string.ping_pong, R.string.tennis, R.string.football, R.string.badminton, R.string.gym,
            R.string.lacrosse, R.string.volleyball, R.string.bowling, R.string.boxing, R.string.ice_hockey};
    private int[] mSportIcons = { R.drawable.baseball, R.drawable.cricket, R.drawable.basketball, R.drawable.soccer,
            R.drawable.ping_pong, R.drawable.tennis, R.drawable.soccer, R.drawable.badminton, R.drawable.gym,
            R.drawable.lacrosse, R.drawable.volleyball, R.drawable.bowling, R.drawable.boxing, R.drawable.ice_hockey};

    private int[] mAgeGroupNames = { R.string.small_ag, R.string.teenage_ag, R.string.adult_ag, R.string.old_ag};
    private int[] mAgeGroupIcons = { R.drawable.small_ag, R.drawable.teenage_ag, R.drawable.adult_ag, R.drawable.old_ag};

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mGoogleMap;
    private CameraPosition mCameraPosition;
    private Marker mMarker;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .enableAutoManage(getActivity(),
                        this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mGoogleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mGoogleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_new_post_main, container, false);

        TextView sportContent = (TextView) mView.findViewById(R.id.sport_name_content);
        GridView sportsGrid = (GridView) mView.findViewById(R.id.sports_grid);
        sportsGrid.setAdapter(new NewPostSportAdapter(getActivity(), mSportNames, mSportIcons, sportContent));
        ViewCompat.setNestedScrollingEnabled(sportsGrid, true);

        PlaceAutocompleteFragment placeAutocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.new_post_map_search_container);
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng latLng = place.getLatLng();
                double lat = latLng.latitude;
                double lng = latLng.longitude;
                gotoLocation(lat, lng, DEFAULT_ZOOM);
                displayMarker(lat, lng);
            }

            @Override
            public void onError(Status status) {

            }
        });
        View mapSearchContainer = mView.findViewById(R.id.new_post_map_search_container);
        mapSearchContainer.setVisibility(View.GONE);

        Switch toggle = (Switch) mView.findViewById(R.id.new_post_map_search_loc_button);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    View mapSearchContainer = mView.findViewById(R.id.new_post_map_search_container);
                    mapSearchContainer.setVisibility(View.GONE);
                    getDeviceLocation();
                } else {
                    View mapSearch = mView.findViewById(R.id.new_post_map_search_container);
                    mapSearch.setVisibility(View.VISIBLE);
                }
            }
        });

        View datePicker = mView.findViewById(R.id.new_post_date_button);
        datePicker.setOnClickListener(this);
        View timePicker = mView.findViewById(R.id.new_post_time_button);
        timePicker.setOnClickListener(this);

        View smallAgeButton = mView.findViewById(R.id.new_post_small_age_button);
        smallAgeButton.setOnClickListener(this);
        View teenageAgeButton = mView.findViewById(R.id.new_post_teenage_age_button);
        teenageAgeButton.setOnClickListener(this);
        View adultAgeButton = mView.findViewById(R.id.new_post_adult_age_button);
        adultAgeButton.setOnClickListener(this);
        View oldAgeButton = mView.findViewById(R.id.new_post_old_age_button);
        oldAgeButton.setOnClickListener(this);


        return mView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UIUTils.showToolbar(mView, (TextView) mView.findViewById(R.id.toolbar_title), getString(R.string.new_post), getString(R.string.please_fill), R.drawable.back_icon_white, true, R.id.toolbar_title);
        UIUTils.animateStatusBarColorTransition(getActivity(), R.color.dropdown_blue, R.color.dropdown_blue);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        final Calendar c = Calendar.getInstance();
        TextView ageGroupContent = (TextView) mView.findViewById(R.id.age_group_content);
        switch (id) {
            case R.id.new_post_date_button:
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                TextView dateText = (TextView) mView.findViewById(R.id.new_post_date_txt);
                                dateText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
                break;

            case R.id.new_post_time_button:
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                TextView timeText = (TextView) mView.findViewById(R.id.new_post_time_txt);
                                timeText.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
                break;

            case R.id.new_post_small_age_button:
                ageGroupContent.setText(getString(R.string.small_ag));
                break;

            case R.id.new_post_teenage_age_button:
                ageGroupContent.setText(getString(R.string.teenage_ag));
                break;

            case R.id.new_post_adult_age_button:
                ageGroupContent.setText(getString(R.string.adult_ag));
                break;

            case R.id.new_post_old_age_button:
                ageGroupContent.setText(getString(R.string.old_ag));
                break;
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
            // Build the map.
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                    .findFragmentById(R.id.new_post_map);
            mapFragment.getMapAsync(this);
    }

    /**
     * Handles failure to connect to the Google Play services client.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Refer to the reference doc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    /**
     * Handles suspension of the connection to the Google Play services client.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Play services connection suspended");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            double lat = mLastKnownLocation.getLatitude();
            double lng = mLastKnownLocation.getLongitude();
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lat, lng), DEFAULT_ZOOM));
            displayMarker(lat, lng);
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            displayMarker(mDefaultLocation.latitude, mDefaultLocation.longitude);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
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
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mGoogleMap == null) {
            return;
        }

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mGoogleMap.setMyLocationEnabled(false);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }

    public void gotoLocation(double lat, double lng, float zoom){
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mGoogleMap.moveCamera(cameraUpdate);
    }

    private void displayMarker(double lat, double lng){
        if(mMarker != null){
            mMarker.remove();
        }

        MarkerOptions options = new MarkerOptions()
                .position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        mMarker = mGoogleMap.addMarker(options);
    }
}
