package com.zxjk.duoduo.ui.msgpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.MyCustomizeMessageItemProvider;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.MyCustomizeMessage;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;

import static com.zxjk.duoduo.Constant.userId;

/**
 * @author Administrator
 * @// TODO: 2019\4\2 0002  跳转到发送红包的界面
 */
public class PrivacyRedPacketActivity extends BaseActivity {
    TextView sendMessageBtn;

    public static void start(Activity activity, String id) {
        Intent intent = new Intent(activity, PrivacyRedPacketActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_envelopes);
        sendMessageBtn = findViewById(R.id.m_red_envelopes_commit_btn);
        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                MyCustomizeMessage message = new MyCustomizeMessage();
                message.setPaypwd("123456");
                message.setMessage("123456");
                message.setMoney(132456.00);
                Message message1 = Message.obtain("55", Conversation.ConversationType.PRIVATE, message);
                RongIM.getInstance().sendMessage(message1, null, null, new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        LogUtils.d("DEBUG", "红包发送失败");
                    }

                    @Override
                    public void onSuccess(Integer integer) {

                        LogUtils.d("DEBUG", "红包发送成功");


                        finish();
                    }
                });

            }
        });
    }
}
