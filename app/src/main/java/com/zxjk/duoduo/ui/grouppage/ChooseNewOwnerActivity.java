package com.zxjk.duoduo.ui.grouppage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.adapter.ChooseNewOwnerAdapter;
import com.zxjk.duoduo.ui.msgpage.ConversationActivity;
import com.zxjk.duoduo.ui.widget.TitleBar;
import com.zxjk.duoduo.ui.widget.dialog.BaseAddTitleDialog;
import com.zxjk.duoduo.utils.CommonUtils;

/**
 * @author Administrator
 */
@SuppressLint("CheckResult")
public class ChooseNewOwnerActivity extends BaseActivity {
    TitleBar titleBar;
    RecyclerView mRecyclerView;
    ChooseNewOwnerAdapter mAdapter;

    BaseAddTitleDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_new_owner);
        String groupId = getIntent().getStringExtra("groupId");

        titleBar = findViewById(R.id.title_bar);
        titleBar.setLeftBack(this);
        mRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new ChooseNewOwnerAdapter();
        getGroupMemByGroupId(groupId);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            dialog = new BaseAddTitleDialog(ChooseNewOwnerActivity.this);
            dialog.setOnClickListener(() -> {
              updateGroupOwner(groupId, mAdapter.getData().get(position).getId());
              dialog.dismiss();
            });
            dialog.show(getString(R.string.determine_select_group));
        });
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
                    allGroupMembersResponses.remove(0);
                    mAdapter.setNewData(allGroupMembersResponses);
                }, this::handleApiError);
    }

    /**
     * 准让群主
     *
     * @param groupId
     * @param customerId
     */
    public void updateGroupOwner(String groupId, String customerId) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .updateGroupOwner(groupId, customerId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    ToastUtils.showShort(ChooseNewOwnerActivity.this.getString(R.string.transfer_group_successful));
                    Intent intent = new Intent(ChooseNewOwnerActivity.this, ConversationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                },this::handleApiError);

    }
}
