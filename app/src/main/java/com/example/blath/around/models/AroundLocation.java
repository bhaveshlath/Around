package com.example.blath.around.models;

/**
 * Created by blath on 4/17/17.
 */

public class AroundLocation {

    private double mLatitude;
    private double mLongitude;
    private String mAddress;
    private String mPostalCode;
    private String mCountry;

    public AroundLocation(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public AroundLocation(double latitude, double longitude, String address, String postalCode, String country) {
        mLatitude = latitude;
        mLongitude = longitude;
        mAddress = address;
        mPostalCode = postalCode;
        mCountry = country;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getPostalCode() {
        return mPostalCode;
    }

    public void setPostalCode(String postalCode) {
        mPostalCode = postalCode;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }
}
