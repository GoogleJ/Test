package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.zxjk.duoduo.Application;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GroupResponse;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.rx.RxException;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.grouppage.ChatInformationActivity;
import com.zxjk.duoduo.ui.grouppage.GroupChatInformationActivity;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.RedPacketMessage;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.TransferMessage;
import com.zxjk.duoduo.ui.widget.TitleBar;
import com.zxjk.duoduo.ui.widget.dialog.ExpiredEnvelopesDialog;
import com.zxjk.duoduo.ui.widget.dialog.RedEvelopesDialog;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.adapter.MessageListAdapter;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * @author Administrator
 * @// TODO: 2019\4\1 0001 单聊页面
 */
public class ConversationActivity extends AppCompatActivity {

    private final LifecycleProvider<Lifecycle.Event> provider = AndroidLifecycle.createLifecycleProvider(this);

    private TitleBar titleBar;
    private String targetId;
    private UserInfo targetUserInfo;
    private Group targetGroup;
    private GroupResponse groupResponse;

    @SuppressLint({"CheckResult", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        List<String> pathSegments = getIntent().getData().getPathSegments();
        String conversationType = pathSegments.get(pathSegments.size() - 1);
        targetId = getIntent().getData().getQueryParameter("targetId");
        targetUserInfo = RongUserInfoManager.getInstance().getUserInfo(targetId);
        targetGroup = RongUserInfoManager.getInstance().getGroupInfo(targetId);
        titleBar = findViewById(R.id.conversation_title);

        if (conversationType.equals("private")) {
            Application.setMyExtensionModule(false);
        } else {
            Application.setMyExtensionModule(true);
        }

        if (null == targetUserInfo && conversationType.equals("private")) {
            // 私聊且未缓存
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getCustomerInfoById(targetId)
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .compose(provider.bindToLifecycle())
                    .subscribe(loginResponse -> {
                                targetUserInfo = new UserInfo(targetId, loginResponse.getNick(), Uri.parse(loginResponse.getHeadPortrait()));
                                RongUserInfoManager.getInstance().setUserInfo(targetUserInfo);
                                initView();
                            },
                            t -> ToastUtils.showShort(RxException.getMessage(t)));
        } else if (conversationType.equals("group")) {
            // 群聊且未缓存
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getGroupByGroupId(targetId)
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .compose(provider.bindToLifecycle())
                    .subscribe(groupInfo -> {
                                groupResponse = groupInfo;
                                targetGroup = new Group(groupInfo.getGroupInfo().getId(), groupInfo.getGroupInfo().getGroupNikeName(), Uri.parse(groupInfo.getGroupInfo().getGroupHeadPortrait()));
                                RongUserInfoManager.getInstance().setGroupInfo(targetGroup);
                                initView();
                            },
                            t -> ToastUtils.showShort(RxException.getMessage(t)));
        } else {
            // 本地有缓存（私聊、群聊） 直接加载
            initView();
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
                        RedPacketMessage redPacketMessage = (RedPacketMessage) message.getContent();
                        long hour = (System.currentTimeMillis() - message.getSentTime()) / (1000 * 60 * 60);
                        if (hour < 24) {
                            if (message.getSenderUserId().equals(Constant.currentUser.getId())) {
                                //自己发的红包
                                Intent intent1 = new Intent(context, PeopleUnaccalimedActivity.class);
                                intent1.putExtra("id", redPacketMessage.getRedId());
                                startActivity(intent1);
                            } else {
                                //别人发的红包
                                if (TextUtils.isEmpty(message.getExtra())) {
                                    // 红包未被领取，弹出领取的对话框
                                    RedEvelopesDialog dialog = new RedEvelopesDialog(ConversationActivity.this);
                                    dialog.setOnOpenListener(() -> ServiceFactory.getInstance().getBaseService(Api.class)
                                            .receivePersonalRedPackage(redPacketMessage.getRedId())
                                            .compose(provider.bindToLifecycle())
                                            .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ConversationActivity.this)))
                                            .compose(RxSchedulers.normalTrans())
                                            .subscribe(s -> RongIMClient.getInstance().setMessageExtra(message.getMessageId(), "1", new RongIMClient.ResultCallback<Boolean>() {
                                                @Override
                                                public void onSuccess(Boolean aBoolean) {
                                                    Constant.tempMsg = message;
                                                    Intent intent2 = new Intent(ConversationActivity.this, PeopleRedEnvelopesActivity.class);
                                                    intent2.putExtra("msg", message);
                                                    startActivity(intent2);
                                                }

                                                @Override
                                                public void onError(RongIMClient.ErrorCode errorCode) {

                                                }
                                            }), t -> ToastUtils.showShort(RxException.getMessage(t))));
                                    dialog.show(message, targetUserInfo);
                                } else {
                                    // 红包被领取，进入红包详情
                                    Intent intent1 = new Intent(context, PeopleUnaccalimedActivity.class);
                                    intent1.putExtra("id", redPacketMessage.getRedId());
                                    startActivity(intent1);
                                }
                            }
                        } else {
                            //红包已过期
                            ExpiredEnvelopesDialog dialog = new ExpiredEnvelopesDialog(ConversationActivity.this);
                            dialog.show();
                        }
                        break;
                    default:
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


        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        fragment = (ConversationFragment) fragments.get(0);
        checkedMessages = fragment.getCheckedMessages();
        messageAdapter = fragment.getMessageAdapter();

    }

    @Override
    protected void onRestart() {
        if (Constant.tempMsg != null) {
            for (int i = 0; i < checkedMessages.size(); i++) {
                if (checkedMessages.get(i).getMessageId() == Constant.tempMsg.getMessageId()) {
                    MessageContent content = Constant.tempMsg.getContent();
                    if (content instanceof TransferMessage) {
                        //转账
                        Message transferMessage = checkedMessages.get(i);
                        transferMessage.setExtra("1");
                        messageAdapter.notifyDataSetChanged();
                    } else if (content instanceof RedPacketMessage) {
                        //红包
                        Message redMessage = checkedMessages.get(i);
                        redMessage.setExtra("1");
                        RedPacketMessage red = (RedPacketMessage) redMessage.getContent();
                        red.setExtra("1");
                        messageAdapter.notifyDataSetChanged();
                    }
                    Constant.tempMsg = null;
                    break;
                }
            }
        }

        if (null != Constant.changeGroupName) {
            titleBar.setTitle(Constant.changeGroupName + "(" + groupResponse.getCustomers().size() + ")");
            Constant.changeGroupName = null;
        }
        super.onRestart();
    }

    private ConversationFragment fragment;
    private List<Message> checkedMessages;
    private MessageListAdapter messageAdapter;

    private void initView() {
        titleBar = findViewById(R.id.conversation_title);
        titleBar.getLeftImageView().setOnClickListener(v -> finish());
        titleBar.setTitle(targetUserInfo == null ? (targetGroup.getName() + "(" + groupResponse.getCustomers().size() + ")") : targetUserInfo.getName());
        Intent intent = new Intent(ConversationActivity.this, ChatInformationActivity.class);
        intent.putExtra("bean", targetUserInfo);
        titleBar.getRightImageView().setOnClickListener(v -> {
            if (targetUserInfo == null) {
                Intent intent1 = new Intent(this, GroupChatInformationActivity.class);
                intent1.putExtra("id", targetGroup.getId());
                startActivityForResult(intent1, 1);
            } else {
                startActivity(intent);
            }
        });
    }
}
