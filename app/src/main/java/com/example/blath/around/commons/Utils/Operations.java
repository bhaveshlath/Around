package com.example.blath.around.commons.Utils;

import com.example.blath.around.commons.Utils.app.AroundAppHandles;
import com.example.blath.around.events.GetChatUsersListEvent;
import com.example.blath.around.events.GetPostsEvent;
import com.example.blath.around.events.GetUserPostsEvent;
import com.example.blath.around.events.LoginUserEvent;
import com.example.blath.around.events.PostCommentEvent;
import com.example.blath.around.events.RegisterUserEvent;
import com.example.blath.around.events.SubmitPostEvent;
import com.example.blath.around.models.Comment;
import com.example.blath.around.models.LoginCredentials;
import com.example.blath.around.models.Post;
import com.example.blath.around.models.User;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by blath on 7/6/17.
 */

public class Operations {

    private static final String KEY_POSTS_SUBMIT = "posts/submitPost";
    private static final String KEY_POSTS_GET = "posts/getPosts";
    private static final String KEY_POSTS_SUBMIT_COMMENT = "posts/submitComment";
    private static final String KEY_REGISTER_USER = "register/registerUser";
    private static final String KEY_REGISTER_LOGIN_USER = "register/loginUser";
    private static final String KEY_PROFILE_USER_POSTS = "profile/userPosts";
    private static final String KEY_PROFILE_UPLOAD_IMAGE = "profile/uploadProfileImage";
    public static final String CHAT_BASE_URL = "https://around-165219.firebaseio.com/";
    public static final String BASE_URL = "http://ec2-13-59-88-123.us-east-2.compute.amazonaws.com/";
    public static final String KEY_POSTS = "Posts";
    public static final String KEY_USER = "User";
    public static final String KEY_CHAT_USERS = "users";
    public static final String KEY_EMAILID = "emailID";
    public static final String KEY_POST_ID = "post_id";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    //to login in the application with user credentials
    public static void loginUserOperation(final String emailID, final String password) {
        LoginCredentials loginCredentials = new LoginCredentials(emailID, password);
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
                RegisterUserEvent registerUserEvent = new RegisterUserEvent(ResponseOperations.getResponseObject(response.body().string(), null));
                EventBus.getDefault().post(registerUserEvent);
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
    public static void getPosts(String userID, LatLng latLng, int searchRadiusLength) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + KEY_POSTS_GET).newBuilder();
        urlBuilder.addQueryParameter("userID", userID);
        urlBuilder.addQueryParameter("latitude", String.valueOf(latLng.latitude));
        urlBuilder.addQueryParameter("longitude", String.valueOf(latLng.longitude));
        urlBuilder.addQueryParameter("searchRadiusLength", String.valueOf(searchRadiusLength));
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

    //Fetches all the particular user's posts
    public static void getUserPosts(String userEmailID) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + KEY_PROFILE_USER_POSTS)
                .newBuilder();
        String url = urlBuilder.addQueryParameter(KEY_EMAILID, userEmailID).build().toString();

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
                EventBus.getDefault().post(new GetUserPostsEvent(ResponseOperations.getResponseObject(response.body().string(), KEY_POSTS)));
            }
        });
    }

    //Add a comment to post
    public static void postCommentOfPost(Comment comment, String postId) {

        String commentJsonString = AroundAppHandles.getGsonInstance().toJson(comment);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + KEY_POSTS_SUBMIT_COMMENT)
                .newBuilder();
        String url = urlBuilder.addQueryParameter(KEY_POST_ID, postId).build().toString();

        RequestBody requestBody = RequestBody.create(JSON, commentJsonString);
        Request request = new Request.Builder()
                .url(url)
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
                EventBus.getDefault().post(new PostCommentEvent(ResponseOperations.getResponseObject(response.body().string(), null)));
            }
        });
    }

    //Add a comment to post
    public static void uploadProfileImage(String userID, File profileImageFile) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + KEY_PROFILE_UPLOAD_IMAGE)
                .newBuilder();
        String url = urlBuilder.build().toString();

        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");
        RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("user_id", userID)
                .addFormDataPart("profile_image",profileImageFile.getName().replaceFirst("[.][^.]+$", "") + ".jpeg", RequestBody.create(MEDIA_TYPE_PNG, profileImageFile)).build();
        Request request = new Request.Builder()
                .url(url)
                .post(req)
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
//                EventBus.getDefault().post(new UploadProfileImageEvent( ResponseOperations.getResponseObject(response.body().string(), null)));
            }
        });
    }

    //Fetches the posts near user's current location and handles the server response
    public static void getChatUsersList(String userID) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(CHAT_BASE_URL + KEY_CHAT_USERS + "/" + userID + "/chat_users.json").newBuilder();
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
                EventBus.getDefault().post(new GetChatUsersListEvent(response.body().string()));
            }
        });
    }
}
