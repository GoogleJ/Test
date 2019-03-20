package com.zxjk.duoduo.ui.msgpage.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zxjk.duoduo.R;

import androidx.annotation.NonNull;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeleteFriendDialog extends Dialog implements View.OnClickListener {
    private View view;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        initUI();
    }

    private void initUI() {

    }

    public DeleteFriendDialog(@NonNull Context context) {
        super(context, R.style.dialogstyle);
        this.view = View.inflate(context, R.layout.dialog_delete_friend, null);
        this.context = context;
        ButterKnife.bind(this, view);
    }


    @OnClick({R.id.m_deleter_friend_btn,R.id.m_deleter_friend_cancel})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.m_deleter_friend_btn:
                if (onClickListener!=null){
                    onClickListener.onDelete();
                }
                break;
            case R.id.m_deleter_friend_cancel:
                dismiss();
                break;
                default:
                    dismiss();
                    break;
        }

    }

    public OnClickListener onClickListener;
    public interface OnClickListener {
       void onDelete();

    }
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }


}
