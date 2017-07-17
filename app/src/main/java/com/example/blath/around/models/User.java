package com.example.blath.around.models;

/**
 * Created by blath on 5/10/17.
 */

public class User {
    private UserPersonalInformation mUserPersonalInformation;
    private AroundLocation mLastLocation;
    private String mProfileStatus;

    public User(){}

    public User(UserPersonalInformation userPersonalInformation, AroundLocation lastLocation, String profileStatus) {
        mUserPersonalInformation = userPersonalInformation;
        mLastLocation = lastLocation;
        mProfileStatus = profileStatus;
    }

    public AroundLocation getLastLocation() {
        return mLastLocation;
    }

    public void setLastLocation(AroundLocation lastLocation) {
        mLastLocation = lastLocation;
    }

    public String getProfileStatus() {
        return mProfileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        mProfileStatus = profileStatus;
    }

    public UserPersonalInformation getUserPersonalInformation() {
        return mUserPersonalInformation;
    }

    public void setUserPersonalInformation(UserPersonalInformation userPersonalInformation) {
        mUserPersonalInformation = userPersonalInformation;
    }
}
