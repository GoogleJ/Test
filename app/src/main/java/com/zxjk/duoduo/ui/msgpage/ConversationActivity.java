package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.BaseResponse;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.response.GroupResponse;
import com.zxjk.duoduo.network.rx.RxException;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.ChatInformationActivity;
import com.zxjk.duoduo.ui.grouppage.GroupChatInformationActivity;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.BusinessCardPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.PhotoSelectorPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.RedPacketMessage;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.RedPacketPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.TransferMessage;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.TransferPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin.GameDownScorePlugin;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin.GameJiaoYiPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin.GamePopupWindow;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin.GameRecordPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin.GameRulesPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin.GameStartPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin.GameUpScorePlugin;
import com.zxjk.duoduo.ui.widget.dialog.ExpiredEnvelopesDialog;
import com.zxjk.duoduo.ui.widget.dialog.RedEvelopesDialog;
import com.zxjk.duoduo.utils.CommonUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.adapter.MessageListAdapter;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.imlib.typingmessage.TypingStatus;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * author L
 * create at 2019/5/7
 * description: 游戏下注
 */
@SuppressLint("CheckResult")
public class ConversationActivity extends BaseActivity implements RongIMClient.OnReceiveMessageListener {
    private Disposable gameWindowDisposable;
    private final LifecycleProvider<Lifecycle.Event> provider = AndroidLifecycle.createLifecycleProvider(this);

    private TextView tvTitle;
    private String targetId;
    private UserInfo targetUserInfo;
    private GroupResponse groupResponse;
    private RongIM.OnSendMessageListener onSendMessageListener;
    private RongExtension extension;

    //游戏popwindow跳转计时器
    private long timeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String conversationType = resolvePlugin();

        setContentView(R.layout.activity_conversation);

        extension = findViewById(io.rong.imkit.R.id.rc_extension);

        RongIM.getInstance().setOnReceiveMessageListener(this);

        onSendMessageListener = new RongIM.OnSendMessageListener() {
            @Override
            public Message onSend(Message message) {
                MessageContent content = message.getContent();
                if (content instanceof TextMessage) {
                    if (!TextUtils.isEmpty(((TextMessage) message.getContent()).getExtra()) &&
                            ((TextMessage) message.getContent()).getExtra().equals("pass")
                            && ((TextMessage) content).getContent().equals("开始下注")) {
                        //开始下注
                        runOnUiThread(() -> ServiceFactory.getInstance().getBaseService(Api.class)
                                .beforeBet(groupResponse.getGroupInfo().getId())
                                .compose(bindToLifecycle())
                                .compose(RxSchedulers.normalTrans())
                                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ConversationActivity.this)))
                                .subscribe(s -> {
                                    TextMessage myTextMessage = TextMessage.obtain("开始下注");
                                    myTextMessage.setExtra("start");
                                    Message myMessage = Message.obtain(groupResponse.getGroupInfo().getId(), Conversation.ConversationType.GROUP, myTextMessage);
                                    RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
                                        @Override
                                        public void onAttached(Message message1) {
                                        }

                                        @Override
                                        public void onSuccess(Message message1) {
                                            //发送完"开始下注" 计时20S
                                            Observable.timer(20, TimeUnit.SECONDS, Schedulers.io())
                                                    .flatMap(aLong -> ServiceFactory.getInstance().getBaseService(Api.class)
                                                            .getBetConutBygroupId(groupResponse.getGroupInfo().getId()))
                                                    .compose(bindToLifecycle())
                                                    .compose(RxSchedulers.normalTrans())
                                                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ConversationActivity.this)))
                                                    .subscribe(response -> {
                                                        if (response.getCount() == 0) {
                                                            ToastUtils.showShort(R.string.noxiazhu);
                                                            return;
                                                        }
                                                        Intent intent = new Intent(ConversationActivity.this, GroupRedPacketActivity.class);
                                                        intent.putExtra("isGame", "0");
                                                        intent.putExtra("groupId", groupResponse.getGroupInfo().getId());
                                                        intent.putExtra("fromeGame", response);
                                                        ConversationActivity.this.startActivityForResult(intent, 1);
                                                    }, ConversationActivity.this::handleApiError);
                                        }

                                        @Override
                                        public void onError(Message message1, RongIMClient.ErrorCode errorCode) {
                                            ToastUtils.showShort(R.string.xiazhushibai);
                                        }
                                    });
                                }, ConversationActivity.this::handleApiError));
                        return null;
                    }
                }
                return message;
            }

            @Override
            public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
                return false;
            }
        };

        RongIM.getInstance().setSendMessageListener(onSendMessageListener);

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
        return pathSegments.get(pathSegments.size() - 1);
    }

    private void handleBean(String conversationType) {
        targetId = getIntent().getData().getQueryParameter("targetId");

        if (conversationType.equals("private")) {
            targetUserInfo = RongUserInfoManager.getInstance().getUserInfo(targetId);
            if (null == targetUserInfo) {
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
                            if (targetId.equals(Constant.userId)) {
                                List<IPluginModule> pluginModules = extension.getPluginModules();
                                Iterator<IPluginModule> iterator = pluginModules.iterator();
                                while (iterator.hasNext()) {
                                    IPluginModule next = iterator.next();
                                    if (next instanceof TransferPlugin || next instanceof RedPacketPlugin) {
                                        iterator.remove();
                                        extension.removePlugin(next);
                                    }
                                }
                            }
                        }, ConversationActivity.this::handleApiError);
            } else {
                // 本地有缓存（私聊） 直接加载
                initView();
                if (targetId.equals(Constant.userId)) {
                    List<IPluginModule> pluginModules = extension.getPluginModules();
                    Iterator<IPluginModule> iterator = pluginModules.iterator();
                    while (iterator.hasNext()) {
                        IPluginModule next = iterator.next();
                        if (next instanceof TransferPlugin || next instanceof RedPacketPlugin) {
                            iterator.remove();
                            extension.removePlugin(next);
                        }
                    }
                }
            }
        } else if (conversationType.equals("group")) {
            // 群聊必须每次请求
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getGroupByGroupId(targetId)
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .compose(provider.bindToLifecycle())
                    .subscribe(groupInfo -> {
                        List<IPluginModule> pluginModules = extension.getPluginModules();
                        if (groupInfo.getGroupInfo().getGroupType().equals("1")) {
                            //游戏plugin
                            Iterator<IPluginModule> iterator = pluginModules.iterator();
                            while (iterator.hasNext()) {
                                IPluginModule next = iterator.next();
                                iterator.remove();
                                extension.removePlugin(next);
                            }
                            extension.addPlugin(new PhotoSelectorPlugin());
                            extension.addPlugin(new RedPacketPlugin());
                            extension.addPlugin(new GameUpScorePlugin());
                            extension.addPlugin(new GameRecordPlugin());
                            extension.addPlugin(new GameDownScorePlugin());
//                            extension.addPlugin(new GameDuobaoPlugin());
                            if (groupInfo.getGroupInfo().getGroupOwnerId().equals(Constant.userId)) {
                                //只有群主才能开始下注
                                extension.addPlugin(new GameStartPlugin());
                            }
                            extension.addPlugin(new GameJiaoYiPlugin());
                            extension.addPlugin(new GameRulesPlugin());
                            Constant.ownerIdForGameChat = groupInfo.getGroupInfo().getGroupOwnerId();
                        } else {
                            //群组plugin
                            Iterator<IPluginModule> iterator = pluginModules.iterator();
                            while (iterator.hasNext()) {
                                IPluginModule next = iterator.next();
                                if (next instanceof TransferPlugin || next instanceof BusinessCardPlugin) {
                                    iterator.remove();
                                    extension.removePlugin(next);
                                }
                            }
                        }
                        groupResponse = groupInfo;
                        initView();
                    }, ConversationActivity.this::handleApiError);
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
                                }, ConversationActivity.this::handleApiError);
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
                                .getRedPackageStatus(redPacketMessage.getRedId(), redPacketMessage.getIsGame())
                                .compose(provider.bindToLifecycle())
                                .compose(RxSchedulers.normalTrans())
                                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ConversationActivity.this)))
                                .subscribe(s -> {
                                    if (TextUtils.isEmpty(message.getExtra())) {
                                        Constant.tempMsg = message;
                                    }
                                    if (s.getRedPackageState().equals("1")) {
                                        //红包已过期
                                        ExpiredEnvelopesDialog dialog = new ExpiredEnvelopesDialog(ConversationActivity.this);
                                        dialog.show(RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId()),
                                                true, redPacketMessage.getRedId());
                                    }
                                    if (s.getRedPackageState().equals("3")) {
                                        Intent intent1 = new Intent(context, PeopleUnaccalimedActivity.class);
                                        intent1.putExtra("isGame", redPacketMessage.getIsGame());
                                        intent1.putExtra("id", redPacketMessage.getRedId());
                                        startActivity(intent1);
                                    }
                                    if (s.getRedPackageState().equals("2")) {
                                        if (message.getConversationType().equals(Conversation.ConversationType.PRIVATE)) {
                                            Intent intent1 = new Intent(context, PeopleUnaccalimedActivity.class);
                                            intent1.putExtra("id", redPacketMessage.getRedId());
                                            if (message.getSenderUserId().equals(Constant.userId)) {
                                                intent1.putExtra("isShow", false);
                                            }
                                            startActivity(intent1);
                                        } else {
                                            //手慢了，已被领完
                                            ExpiredEnvelopesDialog dialog = new ExpiredEnvelopesDialog(ConversationActivity.this);
                                            dialog.show(RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId()),
                                                    false, redPacketMessage.getRedId());
                                        }
                                    }
                                    if (s.getRedPackageState().equals("0")) {
                                        //可领取
                                        RedEvelopesDialog dialog = new RedEvelopesDialog(ConversationActivity.this);
                                        if (message.getConversationType().equals(Conversation.ConversationType.PRIVATE) && message.getSenderUserId().equals(Constant.userId)) {
                                            Intent intent1 = new Intent(context, PeopleUnaccalimedActivity.class);
                                            intent1.putExtra("isShow", false);
                                            intent1.putExtra("id", redPacketMessage.getRedId());
                                            startActivity(intent1);
                                        } else if (message.getConversationType().equals(Conversation.ConversationType.GROUP)) {
                                            dialog.setOnOpenListener(() -> ServiceFactory.getInstance().getBaseService(Api.class)
                                                    .receiveGroupRedPackage(redPacketMessage.getRedId(), redPacketMessage.getIsGame())
                                                    .compose(provider.bindToLifecycle())
                                                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ConversationActivity.this)))
                                                    .compose(RxSchedulers.normalTrans())
                                                    .subscribe(s2 -> {
                                                        Intent intent1 = new Intent(context, PeopleUnaccalimedActivity.class);
                                                        intent1.putExtra("id", redPacketMessage.getRedId());
                                                        intent1.putExtra("isGame", redPacketMessage.getIsGame());
                                                        if (redPacketMessage.getIsGame().equals("0")) {
                                                            //如果是游戏，20S内不允许查看红包记录页
                                                            Constant.canCheckRedRecord += 1;
                                                            Observable.timer(20, TimeUnit.SECONDS, Schedulers.io())
                                                                    .subscribe(a -> Constant.canCheckRedRecord -= 1);
                                                        }
                                                        startActivity(intent1);
                                                    }, ConversationActivity.this::handleApiError));
                                            dialog.show(message, RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId()));
                                        } else {
                                            dialog.setOnOpenListener(() -> ServiceFactory.getInstance().getBaseService(Api.class)
                                                    .receivePersonalRedPackage(redPacketMessage.getRedId())
                                                    .compose(provider.bindToLifecycle())
                                                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ConversationActivity.this)))
                                                    .compose(RxSchedulers.normalTrans())
                                                    .subscribe(s1 -> {
                                                        Intent intent2 = new Intent(ConversationActivity.this, PeopleRedEnvelopesActivity.class);
                                                        intent2.putExtra("msg", message);
                                                        startActivity(intent2);
                                                    }, ConversationActivity.this::handleApiError));
                                            dialog.show(message, RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId()));
                                        }
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
                    RongIM.getInstance().setMessageExtra(Constant.tempMsg.getMessageId(), "1", null);
                    messageAdapter.getItem(i).setExtra("1");
                    messageAdapter.notifyDataSetInvalidated();
                    Constant.tempMsg = null;
                    break;
                }
            }
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
        List<String> pathSegments = getIntent().getData().getPathSegments();
        String conversationType = pathSegments.get(pathSegments.size() - 1);

        Intent intent = new Intent(ConversationActivity.this, ChatInformationActivity.class);
        intent.putExtra("bean", targetUserInfo);

        if (conversationType.equals("private")) {
            startActivity(intent);
        } else {
            Intent intent1 = new Intent(this, GroupChatInformationActivity.class);
            intent1.putExtra("id", groupResponse.getGroupInfo().getId());
            startActivityForResult(intent1, 1);
        }
    }

    private void initView() {
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(targetUserInfo == null ? (groupResponse.getGroupInfo().getGroupNikeName() + "(" + groupResponse.getCustomers().size() + ")") : targetUserInfo.getName());
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
                                        messageAdapter.notifyDataSetInvalidated();
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {

                                    }
                                });
                    }
                }
            }
        } else if (message.getContent() instanceof TextMessage) {
            //收到 "开始下注" 消息 并且不是群主（群主无法参与）
            if (!TextUtils.isEmpty(((TextMessage) message.getContent()).getExtra())
                    && ((TextMessage) message.getContent()).getExtra().equals("start")
                    && !Constant.userId.equals(groupResponse.getGroupInfo().getGroupOwnerId()))
                runOnUiThread(() -> ServiceFactory.getInstance().getBaseService(Api.class)
                        .getGroupGameParameter(groupResponse.getGroupInfo().getId())
                        .compose(bindToLifecycle())
                        .compose(RxSchedulers.normalTrans())
                        .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ConversationActivity.this)))
                        .subscribe(getGroupGameParameterResponse -> {
                            GamePopupWindow gamePopupWindow = new GamePopupWindow(ConversationActivity.this);
                            gamePopupWindow.setGroupId(groupResponse.getGroupInfo().getId());
                            gamePopupWindow.setOnCommit((data, time) -> {
                                gamePopupWindow.dismiss();
                                NiceDialog.init().setLayoutId(R.layout.layout_dialog_fragment)
                                        .setConvertListener(new ViewConvertListener() {
                                            @Override
                                            protected void convertView(ViewHolder viewHolder, BaseNiceDialog baseNiceDialog) {
                                                TextView tv_type = viewHolder.getView(R.id.tv_type);
                                                TextView tv_bet = viewHolder.getView(R.id.tv_bet);
                                                TextView tv_theOdds = viewHolder.getView(R.id.tv_theOdds);
                                                TextView tvGameCountDown = viewHolder.getView(R.id.tvGameCountDown);
                                                GroupGamebettingRequeust requeust = GsonUtils.fromJson(data, GroupGamebettingRequeust.class);
                                                tv_type.setText(requeust.getPlayName());
                                                tv_bet.setText(requeust.getBetMoneny());
                                                if (requeust.getPlayName().equals("牛牛")) {
                                                    tv_theOdds.setVisibility(View.GONE);
                                                } else {
                                                    tv_theOdds.setVisibility(View.VISIBLE);
                                                    tv_theOdds.setText(requeust.getBetCardType());
                                                }
                                                Disposable subscribe = Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                                                        .take(time)
                                                        .subscribe(l -> {
                                                            timeLeft = time - l;
                                                            tvGameCountDown.setText((time - l) + "");
                                                            if (l == time - 1) {
                                                                ToastUtils.showShort(R.string.timeout_game);
                                                                baseNiceDialog.dismiss();
                                                            }
                                                        }, t -> {
                                                        });

                                                viewHolder.getView(R.id.tv_cancel).setOnClickListener(v -> {
                                                    baseNiceDialog.dismiss();
                                                    subscribe.dispose();
                                                });

                                                viewHolder.getView(R.id.tv_determine).setOnClickListener(v -> {
                                                    ServiceFactory.getInstance().getBaseService(Api.class)
                                                            .groupGamebetting(data)
                                                            .compose(RxSchedulers.normalTrans())
                                                            .compose(RxSchedulers.ioObserver())
                                                            .compose(bindToLifecycle())
                                                            .subscribe(s -> {
                                                                subscribe.dispose();
                                                                baseNiceDialog.dismiss();
                                                                ToastUtils.showShort(R.string.xiazhuchenggong);
                                                            }, t -> {
                                                                subscribe.dispose();
                                                                baseNiceDialog.dismiss();
                                                                gameWindowDisposable = gamePopupWindow.show(getGroupGameParameterResponse, timeLeft);
                                                                handleApiError(t);
                                                            });
                                                });

                                            }
                                        })
                                        .setOutCancel(false)
                                        .show(getSupportFragmentManager());
                            });

                            gameWindowDisposable = gamePopupWindow.show(getGroupGameParameterResponse, 20);
                        }, ConversationActivity.this::handleApiError));
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            String groupId = data.getStringExtra("groupId");
            String redId = data.getStringExtra("redId");
            Observable.timer(20, TimeUnit.SECONDS)
                    .flatMap((Function<Long, ObservableSource<BaseResponse<String>>>) aLong -> ServiceFactory.getInstance().getBaseService(Api.class)
                            .settlementGame(redId, groupId))
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver())
                    .subscribe(s -> {
                    }, this::handleApiError);
        }
    }

    @Override
    protected void onDestroy() {
        onSendMessageListener = null;
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if (gameWindowDisposable != null && !gameWindowDisposable.isDisposed()) {
            gameWindowDisposable.dispose();
        }
        super.onStop();
    }
}
