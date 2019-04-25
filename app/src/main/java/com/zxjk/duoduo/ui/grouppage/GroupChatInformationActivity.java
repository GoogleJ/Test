package com.zxjk.duoduo.ui.grouppage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.suke.widget.SwitchButton;
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
import com.zxjk.duoduo.ui.msgpage.ConversationActivity;
import com.zxjk.duoduo.ui.msgpage.CreateGroupActivity;
import com.zxjk.duoduo.ui.widget.TitleBar;
import com.zxjk.duoduo.ui.widget.dialog.ConfirmDialog;
import com.zxjk.duoduo.utils.CommonUtils;

/**
 * @author Administrator
 */
@SuppressLint("CheckResult")
public class GroupChatInformationActivity extends BaseActivity {
    TitleBar titleBar;
    TextView groupChatName;
    TextView see_more_group_members;
    RecyclerView groupChatRecyclerView;
    SwitchButton topChatSwitch;
    SwitchButton messageAvoidanceSwitch;
    //群公告
    TextView announcement;
    //解散群
    TextView dissolutionGroup;

    AllGroupMemebersAdapter mAdapter;

    Intent intent;

    private GroupResponse group;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_information);
        see_more_group_members = findViewById(R.id.see_more_group_members);

        TextView tvGroupManagement = findViewById(R.id.tvGroupManagement);

        String id = getIntent().getStringExtra("id");
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getGroupByGroupId(id)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(groupResponse -> {
                    group = groupResponse;
                    if (groupResponse.getGroupInfo().getGroupOwnerId().equals(Constant.userId)) {
                        tvGroupManagement.setVisibility(View.VISIBLE);
                        if (Constant.update) {
                            tvGroupManagement.setVisibility(View.GONE);
                        }
                    } else {
                        tvGroupManagement.setVisibility(View.GONE);
                    }
                    initView();
                }, this::handleApiError);
    }

    private void initView() {
        titleBar = findViewById(R.id.title_bar);
        titleBar.setTitle(titleBar.getTitleView().getText().toString().trim()
                + "(" + group.getCustomers().size() + ")");
        groupChatName = findViewById(R.id.group_chat_name);
        groupChatRecyclerView = findViewById(R.id.group_chat_recycler_view);
        topChatSwitch = findViewById(R.id.top_chat_switch);
        messageAvoidanceSwitch = findViewById(R.id.message_avoidance_switch);
        titleBar.getLeftImageView().setOnClickListener(v -> finish());
        announcement = findViewById(R.id.announcement);
        dissolutionGroup = findViewById(R.id.dissolution_group);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        groupChatRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new AllGroupMemebersAdapter();

        initFooterView();

        getGroupMemByGroupId(group.getGroupInfo().getId());

        groupChatName.setText(group.getGroupInfo().getGroupNikeName());
        announcement.setText(group.getGroupInfo().getGroupNotice());
        announcement.setVisibility(View.VISIBLE);
        groupChatRecyclerView.setAdapter(mAdapter);
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
        if (Constant.update) {
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
     *
     * @param view
     */
    public void groupManagement(View view) {
        Intent intent = new Intent(this, GroupManagementActivity.class);
        intent.putExtra("groupId", group.getGroupInfo().getId());
        startActivity(intent);
    }

    /**
     * 解散和退出群聊
     */
    public void dissolutionGroup(View view) {
        ConfirmDialog confirmDialog;
        if (Constant.userId.equals(group.getGroupInfo().getGroupOwnerId())) {
            confirmDialog = new ConfirmDialog(this, "提示", "确定要解散群组么", v -> {
                disBandGroup(group.getGroupInfo().getId(), Constant.userId);
            });
        } else {
            confirmDialog = new ConfirmDialog(this, "提示", "确定要退出该群么", v -> {
                exitGroup(group.getGroupInfo().getId(), Constant.userId);
            });
        }
        confirmDialog.show();
    }

    /**
     * 查询群成员
     *
     * @param groupId
     */
    public void getGroupMemByGroupId(String groupId) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getGroupMemByGroupId(groupId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(allGroupMembersResponses -> {
                    mAdapter.setNewData(allGroupMembersResponses);
                    if (allGroupMembersResponses.size() <= 9) {
                        see_more_group_members.setClickable(false);
                        see_more_group_members.setVisibility(View.INVISIBLE);
                    }
                }, this::handleApiError);
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
                .subscribe(s -> {
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    ToastUtils.showShort(getString(R.string.you_have_disbanded_the_group));
                }, this::handleApiError);
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
                .subscribe(s -> {
                    Intent intent = new Intent(this, ConversationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    ToastUtils.showShort(getString(R.string.you_have_left_the_group_chat));
                }, this::handleApiError);
    }

    public void changeGroupName(View view) {
        Intent intent = new Intent(this, UpdateUserInfoActivity.class);
        intent.putExtra("type", 4);
        intent.putExtra("data", group);
        startActivityForResult(intent, 1);
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
}
