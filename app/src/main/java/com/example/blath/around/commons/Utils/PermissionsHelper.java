package com.example.blath.around.commons.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

/**
 * Created by blath on 11/1/17.
 */

public class PermissionsHelper {

    /**
     * Check if one or more permissions were granted.
     *
     * @param permissions Non-null array of string permissions from Manifest.permission.
     * @return true if all the permissions are granted. false otherwise.
     */
    public static boolean hasPermissions(Context context, @NonNull String... permissions) {
        for (String permission : permissions) {
            if (!hasPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermissionsFromFragment(Fragment fragment, int permissionRequestKey, @NonNull String... permissions) {
        fragment.requestPermissions(permissions,
                permissionRequestKey);
    }

    private static boolean hasPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // some device (such as Lenovo) throw an exception in the ContextCompat.checkSelfPermission method.
            // This is a workaround for it.
            // See this thread: https://github.com/hotchemi/PermissionsDispatcher/issues/107

            // On pre-Marshmallow devices all permissions are granted at install time and cannot be revoked.
            return true;
        }

        boolean tf = (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED);
        Log.d("BhaveshTF", String.valueOf(tf));
        return tf;
    }

    public static void checkRunTimePermissions(Fragment fragment, View view, int permissionCode, String... permissions) {
        Context context = view.getContext();
        boolean noPermissionCamera = !PermissionsHelper.hasPermissions(context, Manifest.permission.CAMERA);
        boolean noPermissionStorage = !PermissionsHelper.hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean noPermissionCameraStorage = noPermissionStorage && noPermissionCamera;
        if (noPermissionCameraStorage) {
            requestPermissionsFromFragment(fragment, permissionCode, permissions);
        }
    }

    public static void requestPermissionsFromFragment(FragmentActivity fragmentActivity, int permissionRequestKey, @NonNull String... permissions) {
        ActivityCompat.requestPermissions(fragmentActivity, permissions, permissionRequestKey);
    }
}
