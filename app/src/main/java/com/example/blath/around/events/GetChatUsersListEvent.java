package com.example.blath.around.events;

/**
 * Created by blath on 1/18/18.
 */

public class GetChatUsersListEvent {
    private String mResponseObject;

    public GetChatUsersListEvent(String responseObject) {
        mResponseObject = responseObject;
    }

    public String getResponseObject() {
        return mResponseObject;
    }
}
