package com.example.myruns;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/*
Code obtained from https://www.cs.dartmouth.edu/~xingdong/Teaching/CS65/lecture14/lecture14.html
Credit to Prof. Xing Dong.
 */

public class CameraHandler {
    public static void checkPermission(Activity activity){
        if(Build.VERSION.SDK_INT < 23)
            return;
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
    }
}

