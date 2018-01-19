package com.example.blath.around.events;

import com.example.blath.around.models.ResponseObject;

/**
 * Created by blath on 11/12/17.
 */

public class PostCommentEvent {
    private ResponseObject mResponseObject;

    public PostCommentEvent(ResponseObject responseObject) {
        mResponseObject = responseObject;
    }

    public ResponseObject getResponseObject() {
        return mResponseObject;
    }
}
