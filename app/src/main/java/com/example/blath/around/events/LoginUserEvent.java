package com.example.blath.around.events;

import com.example.blath.around.models.ResponseObject;

/**
 * Created by blath on 7/16/17.
 */

public class LoginUserEvent {
    private ResponseObject mResponseObject;

    public LoginUserEvent(ResponseObject responseObject) {
        mResponseObject = responseObject;
    }

    public ResponseObject getResponseObject() {
        return mResponseObject;
    }
}
