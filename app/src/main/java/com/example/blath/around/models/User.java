package com.example.blath.around.models;

import com.example.blath.around.commons.Utils.Operations;

import java.io.Serializable;

/**
 * Created by blath on 5/10/17.
 */

public class User implements Serializable{

    public static final String KEY_USER_ID = "_id";
    public static final String KEY_USER_PROFILE_IMAGE = "profile_image";
    public static final String KEY_USER_FIRST_NAME = "first_name";
    public static final String KEY_USER_LAST_NAME = "last_name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_USER_EMAIL = "emailID";
    public static final String KEY_USER_PHONE_NUMBER = "phone_number";
    public static final String KEY_USER_DOB = "dob";
    public static final String KEY_USER_GENDER = "gender";
    public static final String KEY_USER_PASSWORD = "password";
    public static final String KEY_USER_PROFILE_STATUS = "profile_status";
    public static final String KEY_USER_LONGITUTDE = "latitude";
    public static final String KEY_USER_LATITUDE = "longitude";
    public static final String KEY_USER_LOCATION_ADDRESS = "location_address";
    public static final String KEY_USER_LOCATION_POSTALCODE = "location_postal_code";
    public static final String KEY_USER_SEARCH_RADIUS_LENGTH = "search_radius_length";
    public static final String KEY_USER_LOCATION_COUNTRY = "location_country";
    public static final String KEY_DEFAULT_PROFILE_ICON = Operations.BASE_URL + "uploadsss/59c84d47b383a86db532eacf.jpeg";
    public static final Integer KEY_DEFAULT_SEARCH_RADIUS_LENGTH = 25; //miles

    private String _id;
    private String profileImage;
    private UserPersonalInformation userPersonalInformation;
    private AroundLocation lastLocation;
    private String profileStatus;
    private Integer searchRadiusLength;

    public User(){}

    public User(UserPersonalInformation userPersonalInformation, AroundLocation lastLocation, String profileStatus) {
        this.userPersonalInformation = userPersonalInformation;
        this.lastLocation = lastLocation;
        this.profileStatus = profileStatus;
        this.profileImage = KEY_DEFAULT_PROFILE_ICON;
        this.searchRadiusLength = KEY_DEFAULT_SEARCH_RADIUS_LENGTH;
    }

    public User(String userId, UserPersonalInformation userPersonalInformation, AroundLocation lastLocation, String profileStatus, String profileImageURL) {
        this._id = userId;
        this.userPersonalInformation = userPersonalInformation;
        this.lastLocation = lastLocation;
        this.profileStatus = profileStatus;
        this.profileImage = profileImageURL;
    }

    public AroundLocation getLastLocation() {
        return lastLocation;
    }

    public void setUserId(String userId) {
        this._id = userId;
    }

    public String getUserId() {
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Integer getSearchRadiusLength() {
        return searchRadiusLength;
    }

    public void setSearchRadiusLength(Integer searchRadiusLength) {
        this.searchRadiusLength = searchRadiusLength;
    }
}
