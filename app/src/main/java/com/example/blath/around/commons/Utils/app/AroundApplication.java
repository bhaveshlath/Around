package com.example.blath.around.commons.Utils.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by blath on 7/10/17.
 */

public class AroundApplication extends Application {
        private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        AroundAppHandles.createInstance(getApplicationContext());
    }
}
