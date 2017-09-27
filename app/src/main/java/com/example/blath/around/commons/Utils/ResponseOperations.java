package com.example.blath.around.commons.Utils;

import android.support.annotation.Nullable;

import com.example.blath.around.commons.Utils.app.AroundApplication;
import com.example.blath.around.models.Post;
import com.example.blath.around.models.ResponseObject;
import com.example.blath.around.models.User;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by blath on 7/16/17.
 */

public class ResponseOperations {

    //returns whether request has been successful or not
    public static boolean isError(ResponseObject responseObject) {
        if (responseObject.getStatus().equals("error")) {
            return true;
        }

        return false;
    }

    public static ResponseObject getResponseObject(String responseString, @Nullable String responseBodyType) {
        ResponseObject responseObject = null;
        if (responseBodyType != null) {
            switch (responseBodyType) {
                case Operations.KEY_USER:
                    responseObject = AroundApplication.getGsonInstance().fromJson(responseString, new TypeToken<ResponseObject<User>>() {
                    }.getType());
                    break;
                case Operations.KEY_POSTS:
                    responseObject = AroundApplication.getGsonInstance().fromJson(responseString, new TypeToken<ResponseObject<List<Post>>>() {
                    }.getType());
                    break;
            }
        } else {
            responseObject = AroundApplication.getGsonInstance().fromJson(responseString, new TypeToken<ResponseObject>() {
            }.getType());
        }
        return responseObject;
    }
}
