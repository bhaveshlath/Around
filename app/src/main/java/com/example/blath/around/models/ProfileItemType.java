package com.example.blath.around.models;

/**
 * Created by blath on 11/1/17.
 */

public enum ProfileItemType {
    EMAIL("email"),
    PHONE("phone"),
    PHOTO("photo"),
    ADDRESS("address"),
    UNKNOWN("unknown");

    private String value;

    ProfileItemType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}