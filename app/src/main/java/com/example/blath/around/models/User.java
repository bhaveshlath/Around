package com.example.blath.around.models;

import java.io.Serializable;

/**
 * Created by blath on 5/10/17.
 */

public class User implements Serializable{

    public static final String KEY_USER_ID = "_id";
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

    private String _id;
    private UserPersonalInformation userPersonalInformation;
    private AroundLocation lastLocation;
    private String profileStatus;

    public User(){}

    public User(UserPersonalInformation userPersonalInformation, AroundLocation lastLocation, String profileStatus) {
        this.userPersonalInformation = userPersonalInformation;
        this.lastLocation = lastLocation;
        this.profileStatus = profileStatus;
    }

    public User(String userId, UserPersonalInformation userPersonalInformation, AroundLocation lastLocation, String profileStatus) {
        this._id = userId;
        this.userPersonalInformation = userPersonalInformation;
        this.lastLocation = lastLocation;
        this.profileStatus = profileStatus;
    }

    public AroundLocation getLastLocation() {
        return lastLocation;
    }

    public void setUserId(String userId) {
        this._id = userId;
    }

    public String getKeyUserId() {
        return _id;
    }

    public void setLastLocation(AroundLocation lastLocation) {
        this.lastLocation = lastLocation;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }

    public UserPersonalInformation getUserPersonalInformation() {
        return userPersonalInformation;
    }

    public void setUserPersonalInformation(UserPersonalInformation userPersonalInformation) {
        this.userPersonalInformation = userPersonalInformation;
    }
}
