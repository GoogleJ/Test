package com.zxjk.duoduo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class SharedPreManagerUtils extends BaseSharedPreUtils {
    private static volatile SharedPreManagerUtils sharedPreImp;

    private SharedPreManagerUtils(@NonNull SharedPreferences sharedPreferences) {
        super(sharedPreferences);
    }

    public static SharedPreManagerUtils getInstance(Context context) {
        if (sharedPreImp == null) {
            synchronized (BaseSharedPreUtils.class) {
                if (sharedPreImp == null) {
                    SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
                    sharedPreImp = new SharedPreManagerUtils(sharedPreferences);
                }
            }
        }
        return sharedPreImp;
    }
}