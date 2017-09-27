package com.example.blath.around.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by blath on 4/17/17.
 */

public class Post implements Serializable {

    public static final String KEY_TYPE_SPORTS = "sports";

    private User user;
    private String type;
    private String subType;
    private AroundLocation location;
    private AgeRange ageRange;
    private String genderPreference;
    private String description;
    private DateRange dates;
    private String time;
    private int commentsCount;
    private ArrayList<String> comments;

    public Post(User user, String type, String subType, AroundLocation location, AgeRange ageRange, String genderPreference, String description, DateRange dates, String time, int commentsCount, ArrayList<String> comments) {
        this.user = user;
        this.type = type;
        this.subType = subType;
        this.location = location;
        this.ageRange = ageRange;
        this.genderPreference = genderPreference;
        this.description = description;
        this.dates = dates;
        this.time = time;
        this.commentsCount = commentsCount;
        this.comments = comments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public AroundLocation getLocation() {
        return location;
    }

    public void setLocation(AroundLocation location) {
        this.location = location;
    }

    public AgeRange getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(AgeRange ageRange) {
        this.ageRange = ageRange;
    }

    public String getGenderPreference() {
        return genderPreference;
    }

    public void setGenderPreference(String genderPreference) {
        this.genderPreference = genderPreference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateRange getDates() {
        return dates;
    }

    public void setDates(DateRange dates) {
        this.dates = dates;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCommentCount() {
        return commentsCount;
    }

    public void setCommentCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }
}
