package com.zxjk.duoduo.utils;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;

import com.blankj.utilcode.util.LogUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


/**
 * @author Administrator
 * @// TODO: 2019\3\21 0021 权限获取
 */
public class PermissionUtils {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static String FEATURE_CAMERA="android.permission.CAMERA";



    public static void cameraPremissions(AppCompatActivity activity){
//        verifyStoragePermissions(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
            }
        }else {
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1001);
            }
        }else {
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA
                ,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1001);
            }
        }else {
        }
    }
    public static void constantPermission(AppCompatActivity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_CONTACTS},1001);
            }
        }else {
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_CONTACTS},1001);
            }
        }else {
        }

    }





}
