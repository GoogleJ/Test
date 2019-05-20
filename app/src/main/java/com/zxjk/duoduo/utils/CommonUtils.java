package com.zxjk.duoduo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.zxjk.duoduo.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;

@SuppressLint("CheckResult")
public class CommonUtils {
    private static Dialog dialog;

    public static Dialog initDialog(Context context) {
        return initDialog(context, null);
    }

    public static Dialog initDialog(Context context, String loadText) {
        dialog = new Dialog(context) {
            @Override
            public void onWindowFocusChanged(boolean hasFocus) {
                super.onWindowFocusChanged(hasFocus);
                if (hasFocus) {
                    ImageView iv = dialog.findViewById(R.id.iv);
                    Glide.with(context).asGif().load(R.drawable.loading).into(iv);
                }
            }
        };
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);
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
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
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

    public static Uri getMediaUriFromPath(Context context, String path) {
        Uri mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(mediaUri,
                null,
                MediaStore.Images.Media.DISPLAY_NAME + "= ?",
                new String[]{path.substring(path.lastIndexOf("/") + 1)},
                null);

        Uri uri = null;
        if (cursor.moveToFirst()) {
            uri = ContentUris.withAppendedId(mediaUri,
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
        }
        cursor.close();
        return uri;
    }

    /**
     * 键盘隐藏的方法
     *
     * @param activity
     */
    public static void hideInputMethod(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }


    public static String timeStamp2Date(String time) {
        Long timeLong = Long.parseLong(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//要转换的时间格式
        Date date;
        try {
            date = sdf.parse(sdf.format(timeLong));
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * double乘法
     *
     * @param value1
     * @param value2
     * @return
     */
    public static double mul(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(value1));
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));
        return b1.multiply(b2).doubleValue();
    }


    public static String getVersionName(Context context) {
        String name = "";
        try {
            name = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 金额
     * 元 =====》 保留两位小数
     *
     * @param string 判断的string
     * @return 空:"0.00" 不为空:string
     */
    public static String getTwoDecimals(@Nullable String string) {
        if (string != null && !TextUtils.isEmpty(string) && !string.equalsIgnoreCase("null")
                && !("").equals(string)) {
            Double d = Double.parseDouble(string);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat.format(d);
        }
        return "0.00";
    }


}
