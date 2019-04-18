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
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.response.GroupResponse;
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
import io.reactivex.functions.Consumer;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.adapter.MessageListAdapter;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

public class ConversationActivity extends AppCompatActivity implements RongIMClient.OnReceiveMessageListener {

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

        //获取聊天类型（单聊、群聊）
        List<String> pathSegments = getIntent().getData().getPathSegments();
        String conversationType = pathSegments.get(pathSegments.size() - 1);

        //修改底部plugin（群聊无转账）
        if (conversationType.equals("private")) {
            Application.setMyExtensionModule(false);
        } else {
            Application.setMyExtensionModule(true);
        }

        setContentView(R.layout.activity_conversation);

        RongIM.getInstance().setOnReceiveMessageListener(this);

        targetId = getIntent().getData().getQueryParameter("targetId");
        targetUserInfo = RongUserInfoManager.getInstance().getUserInfo(targetId);
        targetGroup = RongUserInfoManager.getInstance().getGroupInfo(targetId);
        titleBar = findViewById(R.id.conversation_title);

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
                if (conversationType == Conversation.ConversationType.GROUP) {
                    if (Constant.friendsList == null) {
                        ServiceFactory.getInstance().getBaseService(Api.class)
                                .getFriendListById()
                                .compose(provider.bindToLifecycle())
                                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ConversationActivity.this)))
                                .compose(RxSchedulers.normalTrans())
                                .subscribe(friendInfoResponses -> {
                                    Constant.friendsList = friendInfoResponses;
                                    handleFriendList(userInfo.getUserId());
                                }, t -> ToastUtils.showShort(RxException.getMessage(t)));
                    } else {
                        handleFriendList(userInfo.getUserId());
                    }
                } else {
                    Intent intent = new Intent(ConversationActivity.this, FriendDetailsActivity.class);
                    intent.putExtra("friendId", userInfo.getUserId());
                    startActivity(intent);
                }
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
        messageAdapter = fragment.getMessageAdapter();
    }

    private void handleFriendList(String userId) {
        if (userId.equals(Constant.userId)) {
            //扫到了自己
            Intent intent = new Intent(this, FriendDetailsActivity.class);
            intent.putExtra("friendId", userId);
            startActivity(intent);
            return;
        }
        for (FriendInfoResponse f : Constant.friendsList) {
            if (f.getId().equals(userId)) {
                //自己的好友，进入详情页（可聊天）
                Intent intent = new Intent(this, FriendDetailsActivity.class);
                intent.putExtra("searchFriendDetails", f);
                startActivity(intent);
                finish();
                return;
            }
        }

        //陌生人，进入加好友页面
        Intent intent = new Intent(this, AddFriendDetailsActivity.class);
        intent.putExtra("newFriendId", userId);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onRestart() {
        if (Constant.tempMsg != null) {
            for (int i = 0; i < messageAdapter.getCount(); i++) {
                if (messageAdapter.getItem(i).getMessageId() == Constant.tempMsg.getMessageId()) {
                    MessageContent content = Constant.tempMsg.getContent();
                    if (content instanceof TransferMessage) {
                        //转账
                        UIMessage transferMessage = messageAdapter.getItem(i);
                        transferMessage.setExtra("1");
                        messageAdapter.notifyDataSetChanged();
                    } else if (content instanceof RedPacketMessage) {
                        //红包
                        UIMessage redMessage = messageAdapter.getItem(i);
                        redMessage.setExtra("1");
                        messageAdapter.notifyDataSetChanged();
                    }
                    break;
                }
            }
            Constant.tempMsg = null;
        }

        if (null != Constant.changeGroupName) {
            titleBar.setTitle(Constant.changeGroupName + "(" + groupResponse.getCustomers().size() + ")");
            Constant.changeGroupName = null;
        }
        super.onRestart();
    }

    private ConversationFragment fragment;
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

    //融云会话页面收到消息回调
    @Override
    public boolean onReceived(Message message, int i) {

        if (message.getContent() instanceof TransferMessage) {
            //收到一条转账消息
            for (int j = 0; j < messageAdapter.getCount(); j++) {
                TransferMessage t = (TransferMessage) messageAdapter.getItem(j).getContent();
                if (t.getFromCustomerId().equals(Constant.userId)) {
                    int finalJ = j;
                    RongIM.getInstance().setMessageExtra(messageAdapter.getItem(j).getMessageId()
                            , "1", new RongIMClient.ResultCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                    messageAdapter.getItem(finalJ).setExtra("1");
                                    messageAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                    break;
                }
            }
        }
        return false;
    }
}
