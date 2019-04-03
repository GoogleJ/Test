package com.zxjk.duoduo.ui.msgpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.RedPacketMessage;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.TransferMessage;
import com.zxjk.duoduo.weight.TitleBar;

import androidx.annotation.Nullable;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * @author Administrator
 * @// TODO: 2019\4\3 0003 转账的activity 
 */
public class TransferActivity extends BaseActivity {

    TitleBar titleBar;
    TextView commitBtn;

    public static void start(Activity activity){
        Intent intent=new Intent(activity,TransferActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        titleBar=findViewById(R.id.m_transfer_title_bar);
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        commitBtn=findViewById(R.id.m_transfer_commit_btn);
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferMessage message = new TransferMessage();
               message.setRemarks("向某人转账");
               message.setPayPwd("123465");
               message.setHk("500HK");
                Message message1 = Message.obtain("55", Conversation.ConversationType.PRIVATE, message);
                RongIM.getInstance().sendMessage(message1, null, null, new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        LogUtils.d("DEBUG", "转账失败");
                    }

                    @Override
                    public void onSuccess(Integer integer) {

                        LogUtils.d("DEBUG", "转账成功");

                        finish();
                    }
                });
            }
        });

    }
}
