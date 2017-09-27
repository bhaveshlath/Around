package com.example.blath.around.models;

import com.google.gson.JsonElement;

/**
 * Created by blath on 9/24/17.
 */

public class ResponseObject <T>{
    String status;
    String message;
    T body;

    public ResponseObject(String status, String message, T body) {
        this.status = status;
        this.message = message;
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
