package com.example.blath.around.commons.Utils.app;

import android.content.Context;

import com.example.blath.around.commons.Utils.ImageUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;

/**
 * Created by blath on 10/30/17.
 */

public class AroundAppHandles {
    public static final String TAG = AroundAppHandles.class.getSimpleName();
    public static final String AROUND_SHARED_PREFERENCE = "around_shared_preference";
    private static AroundAppHandles sInstance;
    private static Gson sGson;
    private static final OkHttpClient sOkHttpClient = new OkHttpClient();
    private ImageUtils mImageUtils;

//    private RequestQueue mRequestQueue;

    AroundAppHandles() {
    }

    public static AroundAppHandles createInstance(Context context) {
        AroundAppHandles handles;
        synchronized (AroundAppHandles.class) {
            if ((handles = getInstance()) == null) {
                handles = new AroundAppHandles();
                handles.mImageUtils = new ImageUtils(context, sOkHttpClient);
                setInstance(handles);
            }
        }
        return handles;
    }

    public static void setInstance(AroundAppHandles instance) {
        sInstance = instance;
    }

    protected static AroundAppHandles getInstance() {
        return sInstance;
    }


    public static OkHttpClient getOkHttpClient() {
        return sInstance.sOkHttpClient;
    }

    public static Gson getGsonInstance(){
        if(sGson == null){
            sGson = new GsonBuilder().create();
        }
        return sGson;
    }

    public static ImageUtils getImageUtils() {
        return sInstance.mImageUtils;
    }
}
