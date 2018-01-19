package com.example.blath.around.events;

import com.example.blath.around.models.ResponseObject;

/**
 * Created by blath on 11/17/17.
 */

public class UploadProfileImageEvent {
    private ResponseObject mResponseObject;

    public UploadProfileImageEvent(ResponseObject responseObject) {
        mResponseObject = responseObject;
    }

    public ResponseObject getResponseObject() {
        return mResponseObject;
    }
}
