package com.zxjk.duoduo.ui.grouppage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.AllGroupMembersResponse;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.response.GroupResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.adapter.AllGroupMemebersAdapter1;
import com.zxjk.duoduo.ui.msgpage.AddFriendDetailsActivity;
import com.zxjk.duoduo.ui.msgpage.FriendDetailsActivity;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@SuppressLint("CheckResult")
public class AllGroupMembersActivity extends BaseActivity {
    RecyclerView mRecyclerView;

    AllGroupMemebersAdapter1 mAdapter;

    List<AllGroupMembersResponse> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_group_members);
        TextView tv_title = findViewById(R.id.tv_title);
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        tv_title.setText(getString(R.string.all_group_members_title));
        mRecyclerView = findViewById(R.id.recycler_view);
        initView();
    }

    private void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new AllGroupMemebersAdapter1();
        GroupResponse groupResponse = (GroupResponse) getIntent().getSerializableExtra("allGroupMembers");
        getGroupMemByGroupId(groupResponse.getGroupInfo().getId());
        mRecyclerView.setAdapter(mAdapter);
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

    public void getGroupMemByGroupId(String groupId) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getGroupMemByGroupId(groupId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(allGroupMembersResponses -> {
                    list.addAll(allGroupMembersResponses);
                    mAdapter.setNewData(list);

                    mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            if (Constant.friendsList == null) {
                                ServiceFactory.getInstance().getBaseService(Api.class)
                                        .getFriendListById()
                                        .compose(bindToLifecycle())
                                        .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(AllGroupMembersActivity.this)))
                                        .compose(RxSchedulers.normalTrans())
                                        .subscribe(friendInfoResponses -> {
                                            Constant.friendsList = friendInfoResponses;
                                            handleFriendList(list.get(position).getId());
                                        }, AllGroupMembersActivity.this::handleApiError);
                            } else {
                                handleFriendList(list.get(position).getId());
                            }
                        }
                    });
                }, this::handleApiError);
    }

}
