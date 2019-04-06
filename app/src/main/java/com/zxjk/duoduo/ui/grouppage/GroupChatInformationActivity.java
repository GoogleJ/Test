package com.zxjk.duoduo.ui.grouppage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.suke.widget.SwitchButton;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GroupChatResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.adapter.AllGroupMemebersAdapter;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.weight.TitleBar;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\26 0026 群组--聊天信息页面
 */
@SuppressLint("CheckResult")
public class GroupChatInformationActivity extends BaseActivity {
    TitleBar titleBar;
    TextView groupChatName;
    RecyclerView groupChatRecyclerView;
    SwitchButton topChatSwitch;
    SwitchButton messageAvoidanceSwitch;
    //群公告
    TextView announcement;
    //解散群
    TextView dissolutionGroup;

    AllGroupMemebersAdapter mAdapter;
    GroupChatResponse group;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_information);
        initView();

    }

    private void initView() {
        titleBar = findViewById(R.id.title_bar);
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
        group = (GroupChatResponse) getIntent().getSerializableExtra("groupChatInformation");
        getGroupMemByGroupId(group.getId());
        getGroupByGroupId(group.getId());
    }

    private void initFooterView() {
        View footerView = LayoutInflater.from(this).inflate(R.layout.view_bottom_del, null);
        ImageView delMembers = footerView.findViewById(R.id.delete_members);
        ImageView addMembers = footerView.findViewById(R.id.add_members);
        delMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(GroupChatInformationActivity.this, RemoveGroupChatActivity.class);
                startActivity(intent);
            }
        });
        addMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(GroupChatInformationActivity.this, SelectContactActivity.class);
                intent.putExtra("addGroupType", 1);
                startActivity(intent);
            }
        });
        mAdapter.addFooterView(footerView);
    }


    /**
     * 跳转群公告
     */
    public void announcement(View view) {
        startActivity(new Intent(this, GroupAnnouncementActivity.class));
    }

    /**
     * 查看全部群成员
     *
     * @param view
     */
    public void groupAllMembers(View view) {
        Intent intent=new Intent(this,AllGroupMembersActivity.class);
        intent.putExtra("allGroupMembers",group);
        startActivity(intent);
    }

    /**
     * 管理群
     *
     * @param view
     */
    public void groupManagement(View view) {
        startActivity(new Intent(this, GroupManagementActivity.class));

    }

    /**
     * 解散和退出群聊
     */
    public void dissolutionGroup(View view) {
        if (Constant.userId.equals(group.getGroupOwnerId())) {
            disBandGroup(group.getId(), Constant.userId);
        } else {
            exitGroup(group.getId(), Constant.userId);
        }
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
                .subscribe(allGroupMembersResponses -> mAdapter.setNewData(allGroupMembersResponses), this::handleApiError);
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
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        finish();
                        ToastUtils.showShort(getString(R.string.you_have_disbanded_the_group));
                    }
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
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        finish();
                        ToastUtils.showShort(getString(R.string.you_have_left_the_group_chat));
                    }
                }, this::handleApiError);
    }

    /**
     * 查询群信息
     *
     * @param groupId
     */

    public void getGroupByGroupId(String groupId) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getGroupByGroupId(groupId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(groupResponse -> {
                    groupChatName.setText(groupResponse.getGroupNikeName());

                    announcement.setText(groupResponse.getGroupSign());
//                        announcement.setVisibility(View.VISIBLE);

                    groupChatRecyclerView.setAdapter(mAdapter);
                    if (Constant.userId.equals(groupResponse.getGroupOwnerId())) {
                        dissolutionGroup.setText(getString(R.string.dissolution_group));
                    } else {
                        dissolutionGroup.setText(getString(R.string.exit_group));
                    }


                }, this::handleApiError);
    }

}
