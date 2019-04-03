package com.zxjk.duoduo.weight.dialog;

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

/**
 * @author Administrator
 * @// TODO: 2019\4\3 0003 领取红包的弹窗
 */
public class RedEvelopesDialog extends Dialog implements View.OnClickListener {
    ImageView redOpenBtn;
    ImageView redCloseBtn;

    private View view;
    private Context context;

    public RedEvelopesDialog(@NonNull Context context) {
        super(context, R.style.dialogstyle);
        this.view = View.inflate(context, R.layout.dialog_red_evelopes, null);
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
        initView();
    }

    private void initView() {
        redOpenBtn = view.findViewById(R.id.m_red_envelopes_open);
        redCloseBtn = view.findViewById(R.id.m_red_evelopes_close);
        redOpenBtn.setOnClickListener(this);
        redCloseBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_red_envelopes_open:
                if (onClickListener!=null){
                    onClickListener.onOpen();
                }
                break;
            case R.id.m_red_evelopes_close:
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }

    public OnClickListener onClickListener;

    public interface OnClickListener{
        void onOpen();
    }
    public void setOnClickListener(OnClickListener onClicklistener){
        this.onClickListener=onClicklistener;
    }
}
