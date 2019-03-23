package com.zxjk.duoduo.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Switch;

import com.zxjk.duoduo.R;

import androidx.annotation.NonNull;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Administrator
 * @// TODO: 2019\3\23 0023 交易状态 
 */
public class TradingRemindDialog extends Dialog implements View.OnClickListener {

    private View view;
    Context context;

    public TradingRemindDialog(@NonNull Context context) {
        super(context, R.style.dialogstyle);
        this.context=context;
        view= LayoutInflater.from(context).inflate(R.layout.dialog_order_being_traded,null);
        ButterKnife.bind(this,view);
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
    }

    @OnClick({R.id.cancel_btn,})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_btn:
                dismiss();
                break;
            case R.id.determine_btn:
                if (onClickListener!=null){
                    onClickListener.determine();
                }
                break;
                default:
                    dismiss();
                    break;

        }

    }
    public OnClickListener onClickListener;
    public interface OnClickListener{
        /**
         * 确定按钮的实现功能
         */
        void determine();
    }
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }


}
