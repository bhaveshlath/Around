package com.example.blath.around.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by blath on 4/17/17.
 */

public class Post implements Serializable {

    public static final String KEY_TYPE_SPORTS = "sports";
    public static final String KEY_TYPE_STUDY = "study";
    public static final String KEY_TYPE_TRAVEL = "travel";
    public static final String KEY_TYPE_CONCERT = "concert";
    public static final String KEY_TYPE_OTHER = "other";
    public static final String KEY_POST = "key_post";
    public static final String KEY_POST_ACTION = "key_post_action";
    public static final String KEY_POST_DETAIL = "key_post_detail";
    public static final String KEY_POST_COMMENTS = "key_post_comments";
    public static final String KEY_POST_REPLY = "key_post_reply";

    private String _id;
    private User user;
    private String type;
    private String title;
    private String subtitle;
    private AroundLocation location;
    private AgeRange ageRange;
    private String genderPreference;
    private String description;
    private DateRange dates;
    private String time;
    private int commentsCount;
    private ArrayList<Comment> comments;
    private Date postedDate;
    private PostStatus status;

    public Post(User user, String type, String title, String subtitle, AroundLocation location, AgeRange ageRange, String genderPreference, String description, DateRange dates, String time, int commentsCount, ArrayList<Comment> comments, PostStatus postStatus) {
        this.user = user;
        this.type = type;
        this.title = title;
        this.subtitle = subtitle;
        this.location = location;
        this.ageRange = ageRange;
        this.genderPreference = genderPreference;
        this.description = description;
        this.dates = dates;
        this.time = time;
        this.commentsCount = commentsCount;
        this.comments = comments;
        Calendar calendar = Calendar.getInstance();
        this.postedDate = calendar.getTime();
        this.status = postStatus;
    }

    public Post(String _id, User user, String type, String title, String subtitle, AroundLocation location, AgeRange ageRange, String genderPreference, String description, DateRange dates, String time, int commentsCount, ArrayList<Comment> comments, Date postedDate, PostStatus status) {
        this._id = _id;
        this.user = user;
        this.type = type;
        this.title = title;
        this.subtitle = subtitle;
        this.location = location;
        this.ageRange = ageRange;
        this.genderPreference = genderPreference;
        this.description = description;
        this.dates = dates;
        this.time = time;
        this.commentsCount = commentsCount;
        this.comments = comments;
        this.postedDate = postedDate;
        this.status = status;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
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

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getStatus() {
        return status.getStatusString();
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }
}
