package com.zxjk.duoduo.ui.grouppage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GroupResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.skin.SkinReportActivity;
import com.zxjk.duoduo.ui.HomeActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.adapter.AllGroupMemebersAdapter;
import com.zxjk.duoduo.ui.minepage.UpdateUserInfoActivity;
import com.zxjk.duoduo.ui.msgpage.CreateGroupActivity;
import com.zxjk.duoduo.ui.msgpage.GroupQRActivity;
import com.zxjk.duoduo.ui.widget.dialog.ConfirmDialog;
import com.zxjk.duoduo.utils.CommonUtils;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * author L
 * create at 2019/5/8
 * description: 群聊天信息  讨论组
 */
@SuppressLint("CheckResult")
public class GroupChatInformationActivity extends BaseActivity {

    TextView groupChatName;
    TextView see_more_group_members;
    RecyclerView groupChatRecyclerView;

    //群公告
    TextView announcement;
    //解散群
    TextView dissolutionGroup;

    AllGroupMemebersAdapter mAdapter;

    Intent intent;
    TextView tv_title;
    private GroupResponse group;

    private boolean isGameGroup;

    private Switch switch1;
    private Switch switch2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_information);
        tv_title = findViewById(R.id.tv_title);

        switch1 = findViewById(R.id.switch1);
        switch2 = findViewById(R.id.switch2);

        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        see_more_group_members = findViewById(R.id.see_more_group_members);

        RelativeLayout rl_groupManage = findViewById(R.id.rl_groupManage);

        group = (GroupResponse) getIntent().getSerializableExtra("group");

        if (group.getMaxNumber().equals("")) {
            isGameGroup = false;
        } else {
            isGameGroup = true;
        }
        if (group.getGroupInfo().getGroupOwnerId().equals(Constant.userId)) {
            rl_groupManage.setVisibility(View.VISIBLE);
            if (Constant.isVerifyVerision) {
                rl_groupManage.setVisibility(View.GONE);
            }
        } else {
            rl_groupManage.setVisibility(View.GONE);
        }

        initView();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.GROUP, group.getGroupInfo().getId(), new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
            @Override
            public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                if (conversationNotificationStatus == Conversation.ConversationNotificationStatus.NOTIFY) {
                    switch2.setChecked(false);
                } else {
                    switch2.setChecked(true);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
        switch2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Conversation.ConversationNotificationStatus status;
            if (isChecked) {
                status = Conversation.ConversationNotificationStatus.DO_NOT_DISTURB;
            } else {
                status = Conversation.ConversationNotificationStatus.NOTIFY;
            }
            RongIM.getInstance().setConversationNotificationStatus(Conversation.ConversationType.GROUP, group.getGroupInfo().getId(), status, null);
        });

        RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, group.getGroupInfo().getId(), new RongIMClient.ResultCallback<Conversation>() {
            @Override
            public void onSuccess(Conversation conversation) {
                if (conversation != null) switch1.setChecked(conversation.isTop());
                else switch1.setEnabled(false);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> RongIM.getInstance().setConversationToTop(Conversation.ConversationType.GROUP, group.getGroupInfo().getId(), isChecked, null));
        tv_title.setText(getString(R.string.chat_message) + "(" + group.getCustomers().size() + ")");
        groupChatName = findViewById(R.id.group_chat_name);
        groupChatRecyclerView = findViewById(R.id.group_chat_recycler_view);

        announcement = findViewById(R.id.announcement);
        dissolutionGroup = findViewById(R.id.dissolution_group);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        groupChatRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new AllGroupMemebersAdapter();
        groupChatRecyclerView.setAdapter(mAdapter);
        if (group.getCustomers().size() <= 15) {
            see_more_group_members.setVisibility(View.GONE);
            mAdapter.setNewData(group.getCustomers());
        } else {
            see_more_group_members.setVisibility(View.VISIBLE);
            mAdapter.setNewData(group.getCustomers().subList(0, 15));
        }
        mAdapter.setOnItemClickListener((adapter, view, position) -> CommonUtils.resolveFriendList(this, group.getCustomers().get(position).getId()));

        initFooterView();

        groupChatName.setText(group.getGroupInfo().getGroupNikeName());
        announcement.setText(group.getGroupInfo().getGroupNotice());
        announcement.setVisibility(View.VISIBLE);

        if (Constant.userId.equals(group.getGroupInfo().getGroupOwnerId())) {
            dissolutionGroup.setText(getString(R.string.dissolution_group));
        } else {
            dissolutionGroup.setText(getString(R.string.exit_group));
        }
    }

    private void initFooterView() {
        View footerView = LayoutInflater.from(this).inflate(R.layout.view_bottom_del, null);
        ImageView delMembers = footerView.findViewById(R.id.delete_members);
        ImageView addMembers = footerView.findViewById(R.id.add_members);
        if (Constant.isVerifyVerision) {
            addMembers.setVisibility(View.GONE);
        }
        if (group.getGroupInfo().getGroupOwnerId().equals(Constant.userId)) {
            //群主才能踢人
            delMembers.setVisibility(View.VISIBLE);
            delMembers.setOnClickListener(v -> {
                intent = new Intent(GroupChatInformationActivity.this, CreateGroupActivity.class);
                intent.putExtra("eventType", 3);
                intent.putExtra("members", group);
                startActivity(intent);
            });
        }

        addMembers.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(group.getMaxNumber())) {
                if (group.getCustomers().size() >= Integer.parseInt(group.getMaxNumber())) {
                    ToastUtils.showShort(getString(R.string.group_max_number));
                    return;
                }
            }
            intent = new Intent(GroupChatInformationActivity.this, CreateGroupActivity.class);
            intent.putExtra("eventType", 2);
            intent.putExtra("members", group);
            startActivity(intent);
        });
        mAdapter.addFooterView(footerView);
    }

    /**
     * 跳转群公告
     */
    public void announcement(View view) {
        if (!group.getGroupInfo().getGroupOwnerId().equals(Constant.userId)) {
            return;
        }
        Intent intent = new Intent(this, GroupAnnouncementActivity.class);
        intent.putExtra("groupId", group.getGroupInfo().getId());
        startActivityForResult(intent, 1);
    }

    /**
     * 查看全部群成员
     *
     * @param view
     */
    public void groupAllMembers(View view) {
        Intent intent = new Intent(this, AllGroupMembersActivity.class);
        intent.putExtra("groupId", group.getGroupInfo().getId());
        intent.putExtra("allGroupMembers", group);
        startActivity(intent);
    }

    /**
     * 管理群
     * 群转让
     *
     * @param view
     */
    public void groupManagement(View view) {
        if (!isGameGroup) {
            Intent intent = new Intent(this, ChooseNewOwnerActivity.class);
            intent.putExtra("groupId", group.getGroupInfo().getId());
            startActivity(intent);
        } else {
            ToastUtils.showShort(getString(R.string.non_transferable));
        }
    }

    /**
     * 解散和退出群聊
     */
    public void dissolutionGroup(View view) {
        ConfirmDialog confirmDialog;
        if (Constant.userId.equals(group.getGroupInfo().getGroupOwnerId())) {
            confirmDialog = new ConfirmDialog(this, "提示", "确定要解散群组么", v -> disBandGroup(group.getGroupInfo().getId(), Constant.userId));
        } else {
            confirmDialog = new ConfirmDialog(this, "提示", "确定要退出该群么", v -> exitGroup(group.getGroupInfo().getId(), Constant.userId));
        }
        confirmDialog.show();
    }

    /**
     * 解散群
     *
     * @param groupId
     * @param groupOwnerId
     */
    public void disBandGroup(String groupId, String groupOwnerId) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .disBandGroup(groupId, groupOwnerId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, groupId
                        , new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                Intent intent = new Intent(GroupChatInformationActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                ToastUtils.showShort(getString(R.string.you_have_disbanded_the_group));
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        }), this::handleApiError);
    }

    /**
     * 退出群
     *
     * @param groupId
     * @param customerId
     */
    public void exitGroup(String groupId, String customerId) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .exitGroup(groupId, customerId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, groupId,
                        new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                Intent intent = new Intent(GroupChatInformationActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                ToastUtils.showShort(getString(R.string.you_have_left_the_group_chat));
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        }), this::handleApiError);
    }

    //清理历史记录
    public void clearHistory(View view) {
        NiceDialog.init().setLayoutId(R.layout.layout_general_dialog).setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                holder.setText(R.id.tv_title, "提示");
                holder.setText(R.id.tv_content, "确定要清空当前聊天记录吗？");
                holder.setText(R.id.tv_cancel, "取消");
                holder.setText(R.id.tv_notarize, "确认");
                holder.setOnClickListener(R.id.tv_cancel, v1 -> dialog.dismiss());
                holder.setOnClickListener(R.id.tv_notarize, v1 -> RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, group.getGroupInfo().getId(), new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        dialog.dismiss();
                        Intent intent = new Intent(GroupChatInformationActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        dialog.dismiss();
                        ToastUtils.showShort(R.string.function_fail);
                    }
                }));
            }
        }).setDimAmount(0.5f).setOutCancel(false).show(getSupportFragmentManager());
    }

    public void groupChat(View view) {
        if (group.getGroupInfo().getGroupOwnerId().equals(Constant.currentUser.getId())) {
            Intent intent = new Intent(this, UpdateUserInfoActivity.class);
            intent.putExtra("type", 4);
            intent.putExtra("data", group);
            startActivityForResult(intent, 1);
        } else {
            ToastUtils.showShort(getString(R.string.no_update_nick));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 2) {
            groupChatName.setText(data.getStringExtra("result"));
        }
        if (requestCode == 1 && resultCode == 3) {
            announcement.setText(data.getStringExtra("result"));
        }
    }

    public void report(View view) {
        startActivity(new Intent(this, SkinReportActivity.class));
    }

    //群二维码
    public void groupQR(View view) {
        Intent intent = new Intent(this, GroupQRActivity.class);
        intent.putExtra("data", group);
        startActivity(intent);
    }
}
