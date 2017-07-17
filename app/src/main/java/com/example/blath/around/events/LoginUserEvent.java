package com.example.blath.around.events;

import org.json.JSONObject;

/**
 * Created by blath on 7/16/17.
 */

public class LoginUserEvent {
    public boolean mIsError;
    public JSONObject mResponseObject;

    public LoginUserEvent(boolean isError, JSONObject jsonObject) {
        mIsError = isError;
        mResponseObject = jsonObject;
    }
}
