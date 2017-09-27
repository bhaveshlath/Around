package com.example.blath.around.commons.Utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.blath.around.commons.Utils.app.AroundApplication;
import com.example.blath.around.events.GetPostsEvent;
import com.example.blath.around.events.LoginUserEvent;
import com.example.blath.around.events.RegisterUserEvent;
import com.example.blath.around.events.SubmitPostEvent;
import com.example.blath.around.models.Post;
import com.example.blath.around.models.User;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by blath on 7/6/17.
 */

public class Operations {

    private static final String BASE_URL = "http://ec2-13-59-88-123.us-east-2.compute.amazonaws.com/";
    private static final String KEY_POSTS_SUBMIT = "posts/submitPost";
    private static final String KEY_POSTS_GET = "posts/getPosts";
    private static final String KEY_REGISTER_USER = "register/registerUser";
    private static final String KEY_REGISTER_LOGIN_USER = "register/loginUser";
    public static final String KEY_POSTS = "Posts";
    public static final String KEY_USER = "User";

    public static JSONObject getJsonMapperObject(String jsonString) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.getStackTrace();
        }
        return jsonObject;
    }

    public static void loginUserOperation(final String username, final String password) {
        JSONObject loginJsonObject = new JSONObject();
        try {
            JSONObject credentialsJsonObject = new JSONObject();
            credentialsJsonObject.put("emailID", username);
            credentialsJsonObject.put("password", password);
            loginJsonObject.put("userCredentials", credentialsJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + KEY_REGISTER_LOGIN_USER, loginJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        EventBus.getDefault().post(new LoginUserEvent(ResponseOperations.getResponseObject(response.toString(), KEY_USER)));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        loginRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AroundApplication.getInstance().addToRequestQueue(loginRequest);
    }

    public static void registerUserOperation(final User userDetail) {

        JSONObject jsonObject = getJsonMapperObject(AroundApplication.getGsonInstance().toJson(userDetail));

        JsonObjectRequest registerRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + KEY_REGISTER_USER, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        EventBus.getDefault().post(new RegisterUserEvent(ResponseOperations.getResponseObject(response.toString(), null)));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        registerRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AroundApplication.getInstance().addToRequestQueue(registerRequest);
    }

    //Submit the users request and handles the server response
    public static void submitPost(final Post post) {

        JSONObject jsonObject = getJsonMapperObject(AroundApplication.getGsonInstance().toJson(post));

        JsonObjectRequest submitPostRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + KEY_POSTS_SUBMIT, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        EventBus.getDefault().post(new SubmitPostEvent(ResponseOperations.getResponseObject(response.toString(), null)));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        submitPostRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AroundApplication.getInstance().addToRequestQueue(submitPostRequest);
    }

    //Fetches the posts near user's current location and handles the server response
    public static void getPosts(LatLng latLng) {

        JSONObject jsonObject = getJsonMapperObject(AroundApplication.getGsonInstance().toJson(latLng));

        JsonObjectRequest getPosts = new JsonObjectRequest(Request.Method.GET, BASE_URL + KEY_POSTS_GET, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        EventBus.getDefault().post(new GetPostsEvent(ResponseOperations.getResponseObject(response.toString(), KEY_POSTS)));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        getPosts.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AroundApplication.getInstance().addToRequestQueue(getPosts);
    }
}
