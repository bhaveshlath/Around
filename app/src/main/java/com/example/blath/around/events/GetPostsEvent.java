package com.example.blath.around.events;

import org.json.JSONObject;

/**
 * Created by blath on 8/7/17.
 */

public class GetPostsEvent {
    public boolean mIsError;
    public JSONObject mResponseObject;

    public GetPostsEvent(boolean isError, JSONObject jsonObject) {
        mIsError = isError;
        mResponseObject = jsonObject;
    }
}
