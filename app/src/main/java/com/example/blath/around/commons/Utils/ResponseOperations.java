package com.example.blath.around.commons.Utils;

import com.example.blath.around.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by blath on 7/16/17.
 */

public class ResponseOperations {

    //returns the response object from the response JSON
    private static JSONObject getResponseObject(JSONObject jsonObject) {
        try{
            JSONObject responseObject = jsonObject.getJSONObject("ASResponse");
            return responseObject;
        }catch(JSONException e){
            e.getStackTrace();
        }
        return null;
    }

    //returns whether request has been successful or not

    public static boolean isError(JSONObject response){
        try{
            if(getResponseObject(response).get("status").equals("error")){
                return true;
            }
        }catch(JSONException e){
            e.getStackTrace();
            return true;
        }

        return false;
    }

    //returns the string message attached in case of error in response.

    public static String getErrorMessage(JSONObject response){

        try{
            JSONObject responseObject = getResponseObject(response);
            if(getResponseObject(response).get("status").equals("error")){
                return responseObject.getString("message");
            }
        }catch(JSONException e){
            e.getStackTrace();
        }

        return null;
    }
}
