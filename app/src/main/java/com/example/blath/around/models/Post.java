package com.example.blath.around.models;

/**
 * Created by blath on 4/17/17.
 */

public class Post {

    private User mUser;
    private Sport mSport;
    private DateRange mDates;
    private AroundLocation mLocation;
    private String mDescription;

    public Post(Sport sport, DateRange dates, AroundLocation location, User user, String description ) {
        mSport = sport;
        mDates = dates;
        mLocation = location;
        mUser = user;
        mDescription = description;
    }

    public Sport getSport() {
        return mSport;
    }

    public void setSport(Sport sport) {
        mSport = sport;
    }

    public DateRange getDates() {
        return mDates;
    }

    public void setDates(DateRange dates) {
        mDates = dates;
    }

    public AroundLocation getLocation() {
        return mLocation;
    }

    public void setLocation(AroundLocation location) {
        mLocation = location;
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
