package com.zxjk.duoduo.ui.grouppage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.adapter.ChooseNewOwnerAdapter;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.weight.TitleBar;
import com.zxjk.duoduo.weight.dialog.BaseAddTitleDialog;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\26 0026 选择新群主
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
                .subscribe(allGroupMembersResponses -> mAdapter.setNewData(allGroupMembersResponses), this::handleApiError);
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
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ToastUtils.showShort(ChooseNewOwnerActivity.this.getString(R.string.transfer_group_successful));
                    }
                },this::handleApiError);

    }
}
