package com.example.blath.around.events;

import android.graphics.Bitmap;

import com.example.blath.around.models.ProfileItemType;

/**
 * Created by blath on 11/1/17.
 */

public class ProfileUpdateEvent {
    public boolean isError;
    public ProfileItemType profileItemType;
    public Bitmap mProfileImage;
    public String message;

    public ProfileUpdateEvent(ProfileItemType profileItemType) {
        this.profileItemType = profileItemType;
    }

    public ProfileUpdateEvent(ProfileItemType profileItemType, String message) {
        isError = true;
        this.profileItemType = profileItemType;
        this.message = message;
    }

    public ProfileUpdateEvent(ProfileItemType profileItemType, Bitmap profileImage) {
        isError = false;
        this.profileItemType = profileItemType;
        this.mProfileImage = profileImage;
    }
}
