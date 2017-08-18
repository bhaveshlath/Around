package com.example.blath.around.commons.Utils;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.EditText;

import com.example.blath.around.R;
import com.example.blath.around.models.AroundLocation;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by blath on 7/6/17.
 */

public class MapUtils implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private String mTAG;
    private GoogleApiClient mGoogleApiClient;
    public GoogleMap mGoogleMap;
    public FragmentActivity mActivity;
    public Fragment mFragment;
    private int mMapResourceId;

    private CameraPosition mCameraPosition;
    private Marker mMarker;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    public Location mLastKnownLocation;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private static final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private AroundLocation mUserLocation = null;

    private AroundLocation mUserLocationData = null;
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static boolean mLocationPermissionGranted;

    public AroundLocation getUserLocation() {
        return mUserLocation;
    }

    public MapUtils(FragmentActivity activity, Fragment fragment, String TAG, Location lastKnownLocation,
                    CameraPosition cameraPosition,
                    int mapSearchContainerResourceId,
                    int mapResourceId) {
        mGoogleApiClient = getGoogleApiClient(activity);
        mActivity = activity;
        mFragment = fragment;
        mTAG = TAG;
        mLastKnownLocation = lastKnownLocation;
        mCameraPosition = cameraPosition;
        mMapResourceId = mapResourceId;
        setPlaceAutocompleteFragmentListener(mapSearchContainerResourceId);
    }

    public GoogleApiClient getGoogleApiClient(FragmentActivity activity) {
        GoogleApiClient googleApiClient = new GoogleApiClient
                .Builder(activity)
                .enableAutoManage(activity, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        googleApiClient.connect();

        return googleApiClient;
    }

    private void setPlaceAutocompleteFragmentListener(int mapSearchContainerResourceId) {
        PlaceAutocompleteFragment placeAutocompleteFragment = (PlaceAutocompleteFragment) mActivity.getFragmentManager().findFragmentById(mapSearchContainerResourceId);
        ((EditText)placeAutocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextColor(Color.parseColor("#FFFFFF"));
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng latLng = place.getLatLng();
                double lat = latLng.latitude;
                double lng = latLng.longitude;
                String[] addressData = getAddress(lat, lng);
                mUserLocation = new AroundLocation(lat, lng, addressData[0], addressData[1], addressData[2]);
                gotoLocation(new LatLng(lat, lng), DEFAULT_ZOOM);
                displayMarker(lat, lng);
            }

            @Override
            public void onError(Status status) {

            }
        });
    }

    public void gotoLocation(LatLng latLng, float zoom) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mGoogleMap.moveCamera(cameraUpdate);
    }

    public void displayMarker(double lat, double lng) {
        if (mMarker != null) {
            mMarker.remove();
        }

        MarkerOptions options = new MarkerOptions()
                .position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        mMarker = mGoogleMap.addMarker(options);
    }

    public void getDeviceLocation() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(mActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(mActivity,
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
            String[] addressData = getAddress(lat, lng);
            mUserLocation = new AroundLocation(lat, lng, addressData[0], addressData[1], addressData[2]);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lat, lng), DEFAULT_ZOOM));
            displayMarker(lat, lng);
        } else {
            Log.d(mTAG, "Current location is null. Using defaults.");
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            displayMarker(mDefaultLocation.latitude, mDefaultLocation.longitude);
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    public void updateLocationUI() {
        if (mGoogleMap == null) {
            return;
        }

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(mActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(mActivity,
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) mFragment.getChildFragmentManager().findFragmentById(mMapResourceId);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(mTAG, "Play services connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(mTAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        updateLocationUI();
        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    public String[] getAddress(double lat, double lng){
        Geocoder geocoder = new Geocoder(mActivity, Locale.getDefault());
        String[] addressData = new String[3];
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address address = addresses.get(0);
            addressData[0] = (address.getAddressLine(0) != null ? address.getAddressLine(0) : "") + " " +
                    (address.getAddressLine(1) != null ? address.getAddressLine(1) : "") + " " +
                    (address.getAddressLine(2) != null ? address.getAddressLine(2) : "");
            addressData[1] = address.getPostalCode();
            addressData[2] = address.getCountryName();

        }catch (IOException e){
            //Exception Handling
        }
        return addressData;
    }
}
