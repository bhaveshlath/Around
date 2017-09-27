package com.example.blath.around.events;

import com.example.blath.around.models.ResponseObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by blath on 7/7/17.
 */

public class RegisterUserEvent {
    private ResponseObject mResponseObject;

    public RegisterUserEvent(ResponseObject responseObject) {
        mResponseObject = responseObject;
    }

    public ResponseObject getResponseObject() {
        return mResponseObject;
    }
}
