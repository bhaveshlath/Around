package com.example.blath.around.models;

/**
 * Created by blath on 5/10/17.
 */

public class User {
    private String mUsername;
    private String mDOB;
    private String mPhoneNumber;

    public User(String username, String DOB, String phoneNumber) {
        mUsername = username;
        mDOB = DOB;
        mPhoneNumber = phoneNumber;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getDOB() {
        return mDOB;
    }

    public void setDOB(String DOB) {
        mDOB = DOB;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }
}
