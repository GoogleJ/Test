package com.zxjk.duoduo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zxjk.duoduo.R;

import androidx.annotation.NonNull;

public class ExpiredEnvelopesDialog extends Dialog implements View.OnClickListener {

    ImageView close_red_packet;


    private View view;
    private Context context;
    public ExpiredEnvelopesDialog(@NonNull Context context) {
        super(context, R.style.dialogstyle);
        this.view = View.inflate(context, R.layout.dialog_expired_envelopes, null);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        initUI();
    }

    private void initUI() {
        close_red_packet=view.findViewById(R.id.close_red_packet);
        close_red_packet.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close_red_packet:
                dismiss();
                break;
                default:
                    dismiss();
                    break;
        }
    }
}
