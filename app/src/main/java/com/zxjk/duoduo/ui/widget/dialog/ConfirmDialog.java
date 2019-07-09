package com.zxjk.duoduo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.zxjk.duoduo.R;


public class ConfirmDialog extends Dialog {

    private TextView tvDialogTitle;
    private TextView tvDialogContent;
    private TextView tvDialogConfirm;
    private TextView tvDialogCancel;
    private String content;
    private String title;
    private View.OnClickListener listener;

    public ConfirmDialog(@NonNull Context context, String title, String content, View.OnClickListener listener) {
        super(context, R.style.CommentStyle);
        this.content = content;
        this.title = title;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_confirm);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        tvDialogTitle = findViewById(R.id.tvDialogTitle);
        tvDialogContent = findViewById(R.id.tvDialogContent);
        tvDialogConfirm = findViewById(R.id.tvDialogConfirm);
        tvDialogCancel = findViewById(R.id.tvDialogCancel);

        findViewById(R.id.tvDialogCancel).setOnClickListener(v -> dismiss());

        tvDialogTitle.setText(title);
        tvDialogContent.setText(content);

        tvDialogConfirm.setOnClickListener(v -> {
            dismiss();
            listener.onClick(v);
        });
        tvDialogCancel.setOnClickListener(v -> dismiss());

    }
}
