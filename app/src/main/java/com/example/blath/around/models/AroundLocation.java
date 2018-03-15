package com.example.blath.around.models;

import java.io.Serializable;

/**
 * Created by blath on 4/17/17.
 */

public class AroundLocation implements Serializable{

    private Location loc;
    private String address;
    private String postalCode;
    private String country;

    public AroundLocation(double latitude, double longitude, String address, String postalCode, String country) {
        this.loc = new Location(new Double[]{longitude, latitude});
        this.address = address;
        this.postalCode = postalCode;
        this.country = country;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
