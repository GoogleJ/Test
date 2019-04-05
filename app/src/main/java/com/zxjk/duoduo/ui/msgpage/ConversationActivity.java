package com.zxjk.duoduo.ui.msgpage;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import io.reactivex.functions.Consumer;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.adapter.MessageListAdapter;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.RxLifecycle;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.rx.RxException;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.grouppage.ChatInformationActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.weight.TitleBar;
import com.zxjk.duoduo.weight.dialog.RedEvelopesDialog;

import java.util.List;

/**
 * @author Administrator
 * @// TODO: 2019\4\1 0001 单聊页面
 */
public class ConversationActivity extends FragmentActivity {

    private final LifecycleProvider<Lifecycle.Event> provider = AndroidLifecycle.createLifecycleProvider(this);

    TitleBar titleBar;

    private String targetId;
    private UserInfo targetUserInfo;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        initView();
        targetId = getIntent().getData().getQueryParameter("targetId");
        targetUserInfo = RongUserInfoManager.getInstance().getUserInfo(targetId);
        if (null == targetUserInfo) {
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getCustomerInfoById(targetId)
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .compose(provider.bindToLifecycle())
                    .subscribe(loginResponse -> {
                                targetUserInfo = new UserInfo(targetId, loginResponse.getNick(), Uri.parse(loginResponse.getHeadPortrait()));
                                RongUserInfoManager.getInstance().setUserInfo(targetUserInfo);
                            },
                            t -> ToastUtils.showShort(RxException.getMessage(t)));
        }

        RongIM.setConversationClickListener(new RongIM.ConversationClickListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
                return false;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                switch (message.getObjectName()) {
                    case "app:transfer":
                        //转账
                        Intent intent = new Intent(context, TransferInfoActivity.class);
                        intent.putExtra("msg", message);
                        intent.putExtra("targetUserInfo", targetUserInfo);
                        startActivity(intent);
                        break;
                    case "MRedPackageMsg":
                        //红包
                        long hour = (System.currentTimeMillis() - message.getSentTime()) / (1000 * 60 * 60);
                        if (hour < 24) {
                            if (message.getSenderUserId().equals(Constant.currentUser.getId())) {
                                //自己发的
                                Intent intent1 = new Intent(context, PeopleRedEnvelopesActivity.class);
                                intent1.putExtra("msg", message);
                                startActivity(intent1);
                            } else {
                                //别人发的
                                RedEvelopesDialog dialog = new RedEvelopesDialog(ConversationActivity.this);
                                dialog.show(message, targetUserInfo);
                            }
                        } else {
                            //已过期


                        }
                        break;
                    case "app:red2":
                        break;
                }
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


    private void initView() {
        titleBar = findViewById(R.id.conversation_title);
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
                        intent.putExtra("UserId", Constant.userId);
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
