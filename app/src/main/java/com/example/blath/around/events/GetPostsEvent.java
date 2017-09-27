package com.example.blath.around.events;

import com.example.blath.around.models.ResponseObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by blath on 8/7/17.
 */

public class GetPostsEvent {
    private ResponseObject mResponseObject;

    public GetPostsEvent(ResponseObject responseObject) {
        mResponseObject = responseObject;
    }

    public ResponseObject getResponseObject() {
        return mResponseObject;
    }
}
