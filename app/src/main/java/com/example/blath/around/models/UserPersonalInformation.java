package com.example.blath.around.models;

/**
 * Created by blath on 6/15/17.
 */

public class UserPersonalInformation {
    private String mFirstName;
    private String mLastName;
    private String mEmailID;
    private String mDOB;
    private String mPhoneNumber;
    private String mPassword;

    public UserPersonalInformation(String firstName, String lastName, String emailID, String DOB, String phoneNumber) {
        mFirstName = firstName;
        mLastName = lastName;
        mEmailID = emailID;
        mDOB = DOB;
        mPhoneNumber = phoneNumber;
    }

    public UserPersonalInformation(String firstName, String lastName, String emailID, String DOB, String phoneNumber, String password) {
        mFirstName = firstName;
        mLastName = lastName;
        mEmailID = emailID;
        mDOB = DOB;
        mPhoneNumber = phoneNumber;
        mPassword = password;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getEmailID() {
        return mEmailID;
    }

    public void setEmailID(String emailID) {
        mEmailID = emailID;
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

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

}
