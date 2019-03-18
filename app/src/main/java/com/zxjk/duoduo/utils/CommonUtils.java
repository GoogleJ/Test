package com.zxjk.duoduo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.TextView;
import com.zxjk.duoduo.R;

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
    public static int getWindowWidth(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    // 屏幕高度（像素）
    public static int getWindowHeight(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

}
