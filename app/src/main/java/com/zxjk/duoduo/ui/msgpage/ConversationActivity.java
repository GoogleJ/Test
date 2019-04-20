package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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
import com.zxjk.duoduo.ui.widget.dialog.ExpiredEnvelopesDialog;
import com.zxjk.duoduo.ui.widget.dialog.RedEvelopesDialog;
import com.zxjk.duoduo.utils.CommonUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.adapter.MessageListAdapter;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.imlib.typingmessage.TypingStatus;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

@SuppressLint("CheckResult")
public class ConversationActivity extends AppCompatActivity implements RongIMClient.OnReceiveMessageListener {
    private final LifecycleProvider<Lifecycle.Event> provider = AndroidLifecycle.createLifecycleProvider(this);

    private TextView tvTitle;
    private String targetId;
    private UserInfo targetUserInfo;
    private Group targetGroup;
    private GroupResponse groupResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String conversationType = resolvePlugin();

        setContentView(R.layout.activity_conversation);

        RongIM.getInstance().setOnReceiveMessageListener(this);

        handleBean(conversationType);

        handleClickMsg();

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        fragment = (ConversationFragment) fragments.get(0);
        messageAdapter = fragment.getMessageAdapter();
    }

    private void registerOnTitleChange() {
        RongIMClient.setTypingStatusListener((type, targetId, typingStatusSet) -> {
            //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
            if (type.equals(Conversation.ConversationType.PRIVATE) && targetId.equals(getIntent().getData().getQueryParameter("targetId"))) {
                //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
                int count = typingStatusSet.size();
                if (count > 0) {
                    Iterator iterator = typingStatusSet.iterator();
                    TypingStatus status = (TypingStatus) iterator.next();
                    String objectName = status.getTypingContentType();

                    MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                    MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                    if (objectName.equals(textTag.value())) {
                        //显示“对方正在输入”
                        runOnUiThread(() -> tvTitle.setText(R.string.conversation_inputing));
                    } else if (objectName.equals(voiceTag.value())) {
                        //显示"对方正在讲话"
                        runOnUiThread(() -> tvTitle.setText(R.string.conversation_speaking));
                    }
                } else {
                    //当前会话没有用户正在输入，标题栏仍显示原来标题
                    runOnUiThread(() -> tvTitle.setText(targetUserInfo.getName()));
                }
            }
        });
    }

    @NotNull
    private String resolvePlugin() {
        //获取聊天类型（单聊、群聊）
        List<String> pathSegments = getIntent().getData().getPathSegments();
        String conversationType = pathSegments.get(pathSegments.size() - 1);

        //特殊类型会话页（如游戏），特殊定义底部plugin
        int custmoerType = getIntent().getIntExtra("custmoerType", -1);
        if (custmoerType != -1) {
            Application.setMyExtensionModule(custmoerType);
            return conversationType;
        }

        //修改底部plugin（群聊无转账）
        if (conversationType.equals("private")) {
            Application.setMyExtensionModule(1);
        } else {
            Application.setMyExtensionModule(3);
        }
        return conversationType;
    }

    private void handleBean(String conversationType) {
        targetId = getIntent().getData().getQueryParameter("targetId");
        targetUserInfo = RongUserInfoManager.getInstance().getUserInfo(targetId);
        targetGroup = RongUserInfoManager.getInstance().getGroupInfo(targetId);
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
    }

    private void handleClickMsg() {
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
                        //获取红包状态
                        ServiceFactory.getInstance().getBaseService(Api.class)
                                .getRedPackageStatus(redPacketMessage.getRedId())
                                .compose(provider.bindToLifecycle())
                                .compose(RxSchedulers.normalTrans())
                                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ConversationActivity.this)))
                                .subscribe(s -> {
                                    if (TextUtils.isEmpty(message.getExtra())) {
                                        Constant.tempMsg = message;
                                    }
                                    if (message.getSenderUserId().equals(Constant.currentUser.getId()) && message.getConversationType().equals(Conversation.ConversationType.PRIVATE)) {
                                        //个人发的红包，直接进入详情页
                                        Intent intent1 = new Intent(context, PeopleUnaccalimedActivity.class);
                                        intent1.putExtra("id", redPacketMessage.getRedId());
                                        startActivity(intent1);
                                        return;
                                    }
                                    if (s.getRedPackageState().equals("1")) {
                                        //红包已过期
                                        ExpiredEnvelopesDialog dialog = new ExpiredEnvelopesDialog(ConversationActivity.this);
                                        dialog.show(RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId()),
                                                true, redPacketMessage.getRedId());
                                    } else if (s.getRedPackageState().equals("2")) {
                                        //手慢了，已被领完
                                        ExpiredEnvelopesDialog dialog = new ExpiredEnvelopesDialog(ConversationActivity.this);
                                        dialog.show(RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId()),
                                                false, redPacketMessage.getRedId());
                                    } else if (s.getRedPackageState().equals("0")) {
                                        //可领取
                                        RedEvelopesDialog dialog = new RedEvelopesDialog(ConversationActivity.this);
                                        if (message.getConversationType().equals(Conversation.ConversationType.PRIVATE)) {
                                            dialog.setOnOpenListener(() -> ServiceFactory.getInstance().getBaseService(Api.class)
                                                    .receivePersonalRedPackage(redPacketMessage.getRedId())
                                                    .compose(provider.bindToLifecycle())
                                                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ConversationActivity.this)))
                                                    .compose(RxSchedulers.normalTrans())
                                                    .subscribe(s1 -> {
                                                        Intent intent2 = new Intent(ConversationActivity.this, PeopleRedEnvelopesActivity.class);
                                                        intent2.putExtra("msg", message);
                                                        startActivity(intent2);
                                                    }, t -> ToastUtils.showShort(RxException.getMessage(t))));
                                        } else if (message.getConversationType().equals(Conversation.ConversationType.GROUP)) {
                                            dialog.setOnOpenListener(() -> ServiceFactory.getInstance().getBaseService(Api.class)
                                                    .receiveGroupRedPackage(redPacketMessage.getRedId())
                                                    .compose(provider.bindToLifecycle())
                                                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ConversationActivity.this)))
                                                    .compose(RxSchedulers.normalTrans())
                                                    .subscribe(s2 -> {
                                                        Intent intent2 = new Intent(ConversationActivity.this, PeopleRedEnvelopesActivity.class);
                                                        intent2.putExtra("msg", message);
                                                        intent2.putExtra("fromGroup", true);
                                                        startActivity(intent2);
                                                    }, t -> ToastUtils.showShort(RxException.getMessage(t))));
                                        }
                                        dialog.show(message, RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId()));
                                    }
                                }, t -> ToastUtils.showShort(RxException.getMessage(t)));
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
    protected void onResume() {
        super.onResume();
        if (Constant.tempMsg != null) {
            for (int i = 0; i < messageAdapter.getCount(); i++) {
                if (messageAdapter.getItem(i).getMessageId() == Constant.tempMsg.getMessageId()) {
                    MessageContent content = Constant.tempMsg.getContent();
                    if (content instanceof TransferMessage) {
                        //转账
                        UIMessage transferMessage = messageAdapter.getItem(i);
                        RongIM.getInstance().setMessageExtra(transferMessage.getMessageId(), "1", null);
                        transferMessage.setExtra("1");
                        messageAdapter.notifyDataSetChanged();
                    } else if (content instanceof RedPacketMessage) {
                        //红包
                        UIMessage redMessage = messageAdapter.getItem(i);
                        RongIM.getInstance().setMessageExtra(redMessage.getMessageId(), "1", null);
                        redMessage.setExtra("1");
                        messageAdapter.notifyDataSetChanged();
                    }
                    break;
                }
            }
            Constant.tempMsg = null;
        }

        if (null != Constant.changeGroupName) {
            tvTitle.setText(Constant.changeGroupName + "(" + groupResponse.getCustomers().size() + ")");
            Constant.changeGroupName = null;
        }
    }

    private ConversationFragment fragment;
    private MessageListAdapter messageAdapter;

    public void back(View view) {
        finish();
    }

    public void detail(View view) {
        Intent intent = new Intent(ConversationActivity.this, ChatInformationActivity.class);
        intent.putExtra("bean", targetUserInfo);
        if (targetUserInfo == null) {
            Intent intent1 = new Intent(this, GroupChatInformationActivity.class);
            intent1.putExtra("id", targetGroup.getId());
            startActivityForResult(intent1, 1);
        } else {
            startActivity(intent);
        }
    }

    private void initView() {
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(targetUserInfo == null ? (targetGroup.getName() + "(" + groupResponse.getCustomers().size() + ")") : targetUserInfo.getName());
        registerOnTitleChange();
    }

    //收到转账消息（已领取），更新上一条状态（改为已领取）
    @Override
    public boolean onReceived(Message message, int i) {
        if (message.getContent() instanceof TransferMessage) {
            //收到一条转账消息(已领取)
            for (int j = 0; j < messageAdapter.getCount(); j++) {
                MessageContent content = messageAdapter.getItem(j).getContent();
                if (content instanceof TransferMessage) {
                    TransferMessage t = (TransferMessage) content;
                    if (t.getTransferId().equals(((TransferMessage) message.getContent()).getTransferId())) {
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
                    }
                }
            }
        }
        return false;
    }
}
