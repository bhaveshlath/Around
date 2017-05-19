package com.example.blath.around.models;

/**
 * Created by blath on 4/17/17.
 */

public class Location {

    private long mLatitude;
    private long mLongitude;
    private String mCity;
    private String mCountry;

    public Location(long mLatitude, long mLongitude, String mCity, String mCountry) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mCity = mCity;
        this.mCountry = mCountry;
    }

    public long getLatitude() {
        return mLatitude;
    }

    public void setLatitude(long mLatitude) {
        this.mLatitude = mLatitude;
    }

    public long getLongitude() {
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
