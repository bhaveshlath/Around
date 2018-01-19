package com.example.blath.around.events;

import com.example.blath.around.models.ResponseObject;

/**
 * Created by blath on 11/10/17.
 */

public class GetUserPostsEvent {

    private ResponseObject mResponseObject;

    public GetUserPostsEvent(ResponseObject responseObject) {
        mResponseObject = responseObject;
    }

    public ResponseObject getResponseObject() {
        return mResponseObject;
    }
}
