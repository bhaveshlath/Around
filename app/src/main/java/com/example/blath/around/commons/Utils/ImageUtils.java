package com.example.blath.around.commons.Utils;

/**
 * Created by blath on 11/1/17.
 */

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

import okhttp3.OkHttpClient;

public class ImageUtils {

    private final Picasso mPicasso;

    public ImageUtils(Context context, OkHttpClient client) {
        mPicasso = initPicasso(context, client);
    }

    protected Picasso initPicasso(Context context, OkHttpClient okHttpClient) {
        Picasso.Builder builder = new Picasso.Builder(context);
        OkHttp3Downloader loader = new OkHttp3Downloader(okHttpClient);
        builder.downloader(loader);
        Picasso picasso = builder.build();
        try {
            Picasso.setSingletonInstance(picasso);
        } catch (IllegalStateException e) {
            // If we can't set the singleton instance, it means the singleton instance was already set
            // because someone called Picasso.from before we got here.
            throw new IllegalStateException("ImageLoader should be used for loading images instead of using Picasso directly", e);
        }
        return picasso;
    }

    public void loadImage(Uri uri, ImageView imageView) {
        mPicasso.load(uri).into(imageView);
    }

    public void loadImage(String url, ImageView imageView, Transformation transformation) {
        mPicasso.load(url).transform(transformation).into(imageView);
    }

    public void loadImage(File file, ImageView imageView, Transformation transformation) {
        mPicasso.load(file).transform(transformation).into(imageView);
    }

    public void loadImage(Uri uri, ImageView imageView, Transformation transformation) {
        mPicasso.load(uri).transform(transformation).into(imageView);
    }
}
