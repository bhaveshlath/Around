package com.example.blath.around.commons.Utils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.blath.around.commons.Utils.app.AroundApplication;
import com.example.blath.around.events.LoginUserEvent;
import com.example.blath.around.events.RegisterUserEvent;
import com.example.blath.around.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by blath on 7/6/17.
 */

public class Operations {

    private static final String BASE_URL = "http://ec2-13-59-88-123.us-east-2.compute.amazonaws.com/";

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

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "register/loginUser", loginJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (ResponseOperations.isError(response)) {
                            EventBus.getDefault().post(new LoginUserEvent(true, response));
                        } else {
                            EventBus.getDefault().post(new LoginUserEvent(false, response));
                        }
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

        JsonObjectRequest registerRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "register/registerUser", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (ResponseOperations.isError(response)) {
                            EventBus.getDefault().post(new RegisterUserEvent(true, response));
                        } else {
                            EventBus.getDefault().post(new RegisterUserEvent(false, response));
                        }
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

    public static JSONObject getJsonMapperObject(String jsonString) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.getStackTrace();
        }
        return jsonObject;
    }
}
