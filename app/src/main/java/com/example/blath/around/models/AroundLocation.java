package com.example.blath.around.models;

/**
 * Created by blath on 4/17/17.
 */

public class AroundLocation {

    private double mLatitude;
    private double mLongitude;
    private String mCity;
    private String mCountry;

    public AroundLocation(double latitude, double longitude){
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public AroundLocation(double latitude, double longitude, String city, String country) {
        mLatitude = latitude;
        mLongitude = longitude;
        mCity = city;
        mCountry = country;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(long mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(long mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String mCountry) {
        this.mCountry = mCountry;
    }
}
