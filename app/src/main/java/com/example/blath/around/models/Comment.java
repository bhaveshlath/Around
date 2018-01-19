package com.example.blath.around.models;

import java.io.Serializable;

/**
 * Created by blath on 11/11/17.
 */

public class Comment implements Serializable {
    private String _id;
    private String displayName;
    private String userID;
    private String content;

    public Comment(String displayName, String userID, String content) {
        this.displayName = displayName;
        this.userID = userID;
        this.content = content;
    }

    public Comment(String _id, String displayName, String userID, String content) {
        this._id = _id;
        this.displayName = displayName;
        this.userID = userID;
        this.content = content;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
