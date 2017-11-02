package com.example.blath.around.commons.Utils;

import com.example.blath.around.commons.Utils.app.AroundAppHandles;
import com.example.blath.around.events.GetPostsEvent;
import com.example.blath.around.events.LoginUserEvent;
import com.example.blath.around.events.RegisterUserEvent;
import com.example.blath.around.events.SubmitPostEvent;
import com.example.blath.around.models.LoginCredentials;
import com.example.blath.around.models.Post;
import com.example.blath.around.models.User;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    //to login in the application with user credentials
    public static void loginUserOperation(final String username, final String password) {
        LoginCredentials loginCredentials = new LoginCredentials(username, password);
        String loginCredentialJsonString = AroundAppHandles.getGsonInstance().toJson(loginCredentials);
        RequestBody requestBody = RequestBody.create(JSON, loginCredentialJsonString);
        Request request = new Request.Builder()
                .url(BASE_URL + KEY_REGISTER_LOGIN_USER)
                .post(requestBody)
                .build();

        AroundAppHandles.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                EventBus.getDefault().post(new LoginUserEvent(ResponseOperations.getResponseObject(response.body().string(), KEY_USER)));
            }
        });
    }

    //register new user
    public static void registerUserOperation(User userDetail) {
        String userJsonString = AroundAppHandles.getGsonInstance().toJson(userDetail);
        RequestBody requestBody = RequestBody.create(JSON, userJsonString);
        Request request = new Request.Builder()
                .url(BASE_URL + KEY_REGISTER_USER)
                .post(requestBody)
                .build();

        AroundAppHandles.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                EventBus.getDefault().post(new RegisterUserEvent(ResponseOperations.getResponseObject(response.body().string(), null)));
            }
        });
    }

    //Submit the users request and handles the server response
    public static void submitPost(final Post post) {
        String userJsonString = AroundAppHandles.getGsonInstance().toJson(post);
        RequestBody requestBody = RequestBody.create(JSON, userJsonString);
        Request request = new Request.Builder()
                .url(BASE_URL + KEY_POSTS_SUBMIT)
                .post(requestBody)
                .build();

        AroundAppHandles.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                EventBus.getDefault().post(new SubmitPostEvent(ResponseOperations.getResponseObject(response.body().string(), null)));
            }
        });
    }

    //Fetches the posts near user's current location and handles the server response
    public static void getPosts(LatLng latLng) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + KEY_POSTS_GET).newBuilder();
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        AroundAppHandles.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                EventBus.getDefault().post(new GetPostsEvent(ResponseOperations.getResponseObject(response.body().string(), KEY_POSTS)));
            }
        });
    }
}
