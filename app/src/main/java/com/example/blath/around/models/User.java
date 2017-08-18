package com.example.blath.around.models;

/**
 * Created by blath on 5/10/17.
 */

public class User {

    public static final String KEY_USER_FIRST_NAME = "first_name";
    public static final String KEY_USER_LAST_NAME = "last_name";
    public static final String KEY_USER_EMAIL = "emailID";
    public static final String KEY_USER_PHONE_NUMBER = "phone_number";
    public static final String KEY_USER_DOB = "dob";
    public static final String KEY_USER_PASSWORD = "password";
    public static final String KEY_USER_PROFILE_STATUS = "profile_status";
    public static final String KEY_USER_LONGITUTDE = "latitude";
    public static final String KEY_USER_LATITUDE = "longitude";
    public static final String KEY_USER_LOCATION_ADDRESS = "location_address";
    public static final String KEY_USER_LOCATION_POSTALCODE = "location_postal_code";
    public static final String KEY_USER_LOCATION_COUNTRY = "location_country";

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
