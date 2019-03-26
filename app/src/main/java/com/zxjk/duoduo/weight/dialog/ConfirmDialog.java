package com.zxjk.duoduo.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zxjk.duoduo.R;

import androidx.annotation.NonNull;

/**
 * @author Administrator
 * @// TODO: 2019\3\26 0026 购买dialog
 */
public class ConfirmDialog extends Dialog {

    private View contentView;

    private TextView tvDialogTitle;
    private TextView tvDialogContent;
    private TextView tvDialogConfirm;
    private TextView tvDialogCancel;

    public ConfirmDialog(@NonNull Context context, String title, String content, View.OnClickListener listener) {
        super(context);

        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
        tvDialogTitle = contentView.findViewById(R.id.tvDialogTitle);
        tvDialogContent = contentView.findViewById(R.id.tvDialogContent);
        tvDialogConfirm = contentView.findViewById(R.id.tvDialogConfirm);
        tvDialogCancel = contentView.findViewById(R.id.tvDialogCancel);

        contentView.findViewById(R.id.tvDialogCancel).setOnClickListener(v -> dismiss());

        tvDialogTitle.setText(title);
        tvDialogContent.setText(content);

        tvDialogConfirm.setOnClickListener(listener);
        tvDialogCancel.setOnClickListener(v -> dismiss());

        setContentView(contentView);
    }

    public void setPoText(int poText) {
        tvDialogConfirm.setText(poText);
    }

    public void setNegText(int negText) {
        tvDialogCancel.setText(negText);
    }

}
