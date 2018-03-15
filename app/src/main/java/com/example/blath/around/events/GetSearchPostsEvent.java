package com.example.blath.around.events;

import com.example.blath.around.models.ResponseObject;

/**
 * Created by blath on 1/30/18.
 */

public class GetSearchPostsEvent {
    private ResponseObject mResponseObject;

    public GetSearchPostsEvent(ResponseObject responseObject) {
        mResponseObject = responseObject;
    }

    public ResponseObject getResponseObject() {
        return mResponseObject;
    }
}