package com.zxjk.duoduo.ui.msgpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxException;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.grouppage.ChatInformationActivity;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.RedPacketMessage;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.weight.TitleBar;
import com.zxjk.duoduo.weight.dialog.ExpiredEnvelopesDialog;
import com.zxjk.duoduo.weight.dialog.RedEvelopesDialog;

import java.util.List;

import static com.zxjk.duoduo.utils.PermissionUtils.cameraPremissions;

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

    @SuppressLint({"CheckResult", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_conversation);
        cameraPremissions(this);

        List<String> pathSegments = getIntent().getData().getPathSegments();
        String conversationType = pathSegments.get(pathSegments.size() - 1);
        targetId = getIntent().getData().getQueryParameter("targetId");
        targetUserInfo = RongUserInfoManager.getInstance().getUserInfo(targetId);
        targetGroup = RongUserInfoManager.getInstance().getGroupInfo(targetId);
        titleBar = findViewById(R.id.conversation_title);
        titleBar.getLeftImageView().setOnClickListener(v -> finish());

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
        } else if (null == targetGroup && conversationType.equals("group")) {
            // 群聊且未缓存
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getGroupByGroupId(targetId)
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .compose(provider.bindToLifecycle())
                    .subscribe(groupInfo -> {
                                targetGroup = new Group(groupInfo.getId(), groupInfo.getGroupNikeName(), Uri.parse(groupInfo.getGroupHeadPortrait()));
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
                                if (conversationType.equals("private")) {
                                    intent1.putExtra("type", "private");
                                } else {
                                    intent1.putExtra("type", "group");
                                }
                                intent1.putExtra("msg", message);
                                startActivity(intent1);
                            } else {
                                //别人发的红包
                                if (redPacketMessage.getExtra().equals("0")) {
                                    RedEvelopesDialog dialog = new RedEvelopesDialog(ConversationActivity.this);
                                    dialog.setOnOpenListener(() -> ServiceFactory.getInstance().getBaseService(Api.class)
                                            .receivePersonalRedPackage(redPacketMessage.getRedId())
                                            .compose(provider.bindToLifecycle())
                                            .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ConversationActivity.this)))
                                            .compose(RxSchedulers.normalTrans())
                                            .subscribe(s -> {
                                                message.setExtra("1");
                                                redPacketMessage.setExtra("1");
                                                RongIMClient.getInstance().setMessageExtra(message.getMessageId(), "1", null);
                                                Intent intent2 = new Intent(ConversationActivity.this, PeopleRedEnvelopesActivity.class);
                                                intent2.putExtra("msg", message);
                                                startActivity(intent2);
                                            }, t -> ToastUtils.showShort(RxException.getMessage(t))));
                                    dialog.show(message, targetUserInfo);
                                } else {
                                    Intent intent1 = new Intent(context, PeopleUnaccalimedActivity.class);
                                    if (conversationType.equals("private")) {
                                        intent1.putExtra("type", "private");
                                    } else {
                                        intent1.putExtra("type", "group");
                                    }
                                    intent1.putExtra("msg", message);
                                    startActivity(intent1);
                                }
                            }
                        } else {
                            //已过期的红包
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
    }

    private void initView() {
        titleBar = findViewById(R.id.conversation_title);
        titleBar.getLeftImageView().setOnClickListener(v -> finish());
        titleBar.setTitle(targetUserInfo == null ? targetGroup.getName() : targetUserInfo.getName());
        titleBar.getRightImageView().setOnClickListener(v -> startActivity(new Intent(ConversationActivity.this, ChatInformationActivity.class)));
    }
}
