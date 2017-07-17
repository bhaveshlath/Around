package com.example.blath.around.events;

import org.json.JSONObject;

/**
 * Created by blath on 7/7/17.
 */

public class RegisterUserEvent {
    public boolean mIsError;
    public JSONObject mResponseObject;

    public RegisterUserEvent(boolean isError, JSONObject jsonObject) {
        mIsError = isError;
        mResponseObject = jsonObject;
    }
}
