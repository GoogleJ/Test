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


/**
 * @author Administrator\
 * todo 关于用户冻结的dialog
 *
 */
public class AccountFreezeDialog extends Dialog implements View.OnClickListener {
    private View view;
    private Context context;
    //这里的view其实可以替换直接传layout过来的 因为各种原因没传(lan)
    public AccountFreezeDialog(Context context) {
        super(context, R.style.dialogstyle);
        this.view = View.inflate(context, R.layout.dialog_account_freeze, null);
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
        ImageView closeBtn=view.findViewById(R.id.m_dialog_account_freeze_close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.m_dialog_account_freeze_close_btn:
                dismiss();
                break;
                default:
                    dismiss();
                    break;

        }
    }
}
