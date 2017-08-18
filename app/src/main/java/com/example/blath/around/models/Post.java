package com.example.blath.around.models;

import java.io.Serializable;

/**
 * Created by blath on 4/17/17.
 */

public class Post implements Serializable {

    public static final String KEY_TYPE_SPORTS = "sports";

    private User mUser;
    private String mType;
    private String mSubType;
    private AroundLocation mLocation;
    private AgeRange mAgeRange;
    private String mGenderPreference;
    private String mDescription;
    private DateRange mDates;
    private String mTime;

    public Post(User user, String type, String subtype, AroundLocation location, AgeRange ageRange, String genderPreference, String description, DateRange dates, String time) {
        mUser = user;
        mType = type;
        mSubType = subtype;
        mLocation = location;
        mAgeRange = ageRange;
        mGenderPreference = genderPreference;
        mDescription = description;
        mDates = dates;
        mTime = time;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getSubType() {
        return mSubType;
    }

    public void setSubType(String subType) {
        mSubType = subType;
    }

    public DateRange getDates() {
        return mDates;
    }

    public void setDates(DateRange dates) {
        mDates = dates;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public AroundLocation getLocation() {
        return mLocation;
    }

    public void setLocation(AroundLocation location) {
        mLocation = location;
    }

    public AgeRange getAgeRange() {
        return mAgeRange;
    }

    public void setAgeRange(AgeRange ageRange) {
        mAgeRange = ageRange;
    }

    public String getGenderPreference() {
        return mGenderPreference;
    }

    public void setGenderPreference(String genderPreference) {
        mGenderPreference = genderPreference;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
