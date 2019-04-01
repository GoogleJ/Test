package com.zxjk.duoduo.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.zxjk.duoduo.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CheckResult")
public class CommonUtils {
    private static Dialog dialog;

    public static Dialog getDialog() {
        return dialog;
    }

    public static Dialog initDialog(Context context) {
        return initDialog(context, null);
    }

    public static Dialog initDialog(Context context, String loadText) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (!TextUtils.isEmpty(loadText)) {
            TextView tips = dialog.findViewById(R.id.tv_dialog_content);
            tips.setText(loadText);
        }
        return dialog;
    }

    public static void destoryDialog() {
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 屏幕宽度（像素）
    public static int getWindowWidth(AppCompatActivity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    // 屏幕高度（像素）
    public static int getWindowHeight(AppCompatActivity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    public static int getSex(String sex) {
        if (TextUtils.isEmpty(sex)) {
            return R.string.male;
        }
        return sex.equals("0") ? R.string.male : R.string.female;
    }

    public static int getAuthenticate(String authenticate) {
        if (TextUtils.isEmpty(authenticate)) {
            return R.string.authen_false;
        }
        return authenticate.equals("0") ? R.string.authen_true : ((authenticate.equals("1") ? R.string.authen_false : R.string.verifing));
    }

    public static String formatTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(time);
    }
}
