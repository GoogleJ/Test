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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Administrator
 * @// TODO: 2019\3\21 0021 删除好友的dialog
 */
public class DeleteFriendInformationDialog extends Dialog implements View.OnClickListener {
    @BindView(R.id.m_cancel_btn)
    TextView cancelBtn;
    @BindView(R.id.m_delete_btn)
    TextView delBtn;

    private View view;
    private Context context;

    public DeleteFriendInformationDialog(@NonNull Context context) {
        super(context, R.style.dialogstyle);
        this.view = View.inflate(context, R.layout.dialog_delete_friend_information, null);
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
        ButterKnife.bind(this, view);
        initUI();
    }

    private void initUI() {
    }

    @OnClick({R.id.m_cancel_btn,R.id.m_delete_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_cancel_btn:
                dismiss();
                break;
            case R.id.m_delete_btn:
                if (onClickListener!=null){
                    onClickListener.onDel();
                }
                break;
            default:
                dismiss();
                break;
        }
    }
    public interface OnClickListener {
        void onDel();
    }

    public OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
