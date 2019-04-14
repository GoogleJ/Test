package com.zxjk.duoduo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.zxjk.duoduo.R;

import androidx.annotation.NonNull;

public class ReLoginDialog extends Dialog {

    private View content;
    private LinearLayout llConfirm;

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onclick();
    }

    public ReLoginDialog(@NonNull Context context) {
        super(context);
        content = LayoutInflater.from(context).inflate(R.layout.dialog_relogin, null);
        llConfirm = content.findViewById(R.id.llConfirm);

        llConfirm.setOnClickListener(v -> {
            if (onClickListener != null) {
                dismiss();
                onClickListener.onclick();
            }
        });

        setContentView(content);
    }
}
