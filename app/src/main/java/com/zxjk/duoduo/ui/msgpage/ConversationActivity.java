package com.zxjk.duoduo.ui.msgpage;

import androidx.fragment.app.FragmentActivity;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.grouppage.ChatInformationActivity;
import com.zxjk.duoduo.weight.TitleBar;

/**
 * @author Administrator
 * @// TODO: 2019\4\1 0001 单聊页面
 */
public class ConversationActivity extends FragmentActivity {

    TitleBar titleBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        titleBar = findViewById(R.id.conversation_title);
        initRongIM();
        initView();

    }


    private void initView() {
        titleBar.getLeftImageView().setOnClickListener(v -> finish());
        titleBar.setTitle(Constant.friendInfoResponse.getNick());
        titleBar.getRightImageView().setOnClickListener(v -> startActivity(new Intent(ConversationActivity.this, ChatInformationActivity.class)));
    }

    public void initRongIM() {
        RongIM.setConversationClickListener(new RongIM.ConversationClickListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {

                if (conversationType.equals(Conversation.ConversationType.PRIVATE)) {
                    if (userInfo.getUserId().equals(Constant.userId)) {
                        Intent intent = new Intent(ConversationActivity.this, ConversationDetailsActivity.class);
                        intent.putExtra("ConstantUserId", 0);
                        intent.putExtra("UserId",Constant.userId);
                        startActivity(intent);


                    } else {
                        titleBar.setTitle(userInfo.getName());
                        Intent intent = new Intent(ConversationActivity.this, ConversationDetailsActivity.class);
                        intent.putExtra("ConstantUserId", 1);
                        intent.putExtra("UserInfo", userInfo.getUserId());
                        startActivity(intent);
                    }
                } else {
                    ToastUtils.showShort("群组暂未设置");
                }

                return true;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });


    }


}
