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

    public static void verifyStoragePermissions(AppCompatActivity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cameraPremissions(AppCompatActivity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA},1001);
            }
        }else {
        }
    }

    public static boolean havaReadContacts(Context context) {

        boolean have = false;
        ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS );
        if (Build.VERSION.SDK_INT >= 23) {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int checkOp = appOpsManager.checkOp(AppOpsManager.OPSTR_READ_CONTACTS, Process.myUid(), context.getPackageName());
            LogUtils.e("Permission", "checkOp:" + checkOp);
            switch (checkOp) {
                case AppOpsManager.MODE_ALLOWED:
                    LogUtils.e("Permission", "AppOpsManager.MODE_ALLOWED ：有权限");
                    have = true;
                    break;
                case AppOpsManager.MODE_IGNORED:
                    LogUtils.e("Permission", "AppOpsManager.MODE_IGNORED：被禁止了");
                    have = false;
                    break;
                case AppOpsManager.MODE_DEFAULT:
                    LogUtils.e("Permission", "AppOpsManager.MODE_DEFAULT");

                    break;
                case AppOpsManager.MODE_ERRORED:
                    LogUtils.e("Permission", "AppOpsManager.MODE_ERRORED：出错了");
                    have = false;
                    break;
                case 4:
                    LogUtils.e("Permission", "AppOpsManager.OTHER：权限需要询问");
                    have = false;
                    break;
            }
        } else {
            have = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return have;
    }
    public static boolean havaWriteContacts(Context context) {

        boolean have = false;
        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS );
        if (Build.VERSION.SDK_INT >= 23) {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int checkOp = appOpsManager.checkOp(AppOpsManager.OPSTR_WRITE_CONTACTS, Process.myUid(), context.getPackageName());
            LogUtils.e("Permission", "checkOp:" + checkOp);
            switch (checkOp) {
                case AppOpsManager.MODE_ALLOWED:
                    LogUtils.e("Permission", "AppOpsManager.MODE_ALLOWED ：有权限");
                    have = true;
                    break;
                case AppOpsManager.MODE_IGNORED:
                    LogUtils.e("Permission", "AppOpsManager.MODE_IGNORED：被禁止了");
                    have = false;
                    break;
                case AppOpsManager.MODE_DEFAULT:
                    LogUtils.e("Permission", "AppOpsManager.MODE_DEFAULT");

                    break;
                case AppOpsManager.MODE_ERRORED:
                    LogUtils.e("Permission", "AppOpsManager.MODE_ERRORED：出错了");
                    have = false;
                    break;
                case 4:
                    LogUtils.e("Permission", "AppOpsManager.OTHER：权限需要询问");
                    have = false;
                    break;
            }
        } else {
            have = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return have;
    }




}
