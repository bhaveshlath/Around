package com.example.blath.around.models;

import java.io.Serializable;

/**
 * Created by blath on 6/15/17.
 */

public class UserPersonalInformation implements Serializable{
    private String firstName;
    private String lastName;
    private String emailID;
    private String dob;
    private String phoneNumber;
    private String password;

    public UserPersonalInformation(String firstName, String lastName, String emailID, String dob, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailID = emailID;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
    }

    public UserPersonalInformation(String firstName, String lastName, String emailID, String dob, String phoneNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailID = emailID;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getDOB() {
        return dob;
    }

    public void setDOB(String dob) {
        this.dob = dob;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
