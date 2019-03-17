package com.zxjk.duoduo.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.zxjk.duoduo.R;

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

}
