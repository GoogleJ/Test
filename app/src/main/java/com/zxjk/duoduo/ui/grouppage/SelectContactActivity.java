package com.zxjk.duoduo.ui.grouppage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.adapter.AddGroupTopAdapter;
import com.zxjk.duoduo.ui.grouppage.adapter.SelectContactAdapter;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author L
 * create at 2019/5/13
 * description: 选择联系人
 */
@SuppressLint("CheckResult")
public class SelectContactActivity extends BaseActivity {

    /**
     * 选中后在上方展示的RecyclerView
     */
    RecyclerView selectRecycler;
    /**
     * 选中添加的群成员
     */
    RecyclerView recyclerView;
    /**
     * 搜索好友
     *
     * @param savedInstanceState
     */
    EditText searchEdit;
    SelectContactAdapter mAdapter;
    AddGroupTopAdapter topAdapter;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_commit)
    TextView tvCommit;

    private boolean fromZhuanChu;

    int position;
    List<FriendInfoResponse> lists = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        ButterKnife.bind(this);
        tvTitle.setText(getString(R.string.select_contact));

        fromZhuanChu = getIntent().getBooleanExtra("fromZhuanChu", false);
        initView();
    }

    List<FriendInfoResponse> list = new ArrayList<>();

    private void initView() {
        tvCommit.setVisibility(View.VISIBLE);
        tvCommit.setText(getString(R.string.commit));
        selectRecycler = findViewById(R.id.recycler_view_select);
        recyclerView = findViewById(R.id.all_members_recycler_view);
        searchEdit = findViewById(R.id.search_select_contact);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        mAdapter = new SelectContactAdapter();
        getFriendListById();
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (fromZhuanChu) {
                String walletAddress = list.get(position).getWalletAddress();
                Intent intent = new Intent();
                intent.putExtra("walletAddress", walletAddress);
                setResult(3, intent);
                finish();
                return;
            }
            selectRecycler.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SelectContactActivity.this);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            selectRecycler.setLayoutManager(linearLayoutManager);
            topAdapter = new AddGroupTopAdapter();
            this.position = position;
            FriendInfoResponse response;
            for (FriendInfoResponse friendInfoResponse : list) {
                if (list.get(position).getId().equals(friendInfoResponse.getId())) {
                    response = new FriendInfoResponse();
                    response.setId(friendInfoResponse.getId());
                    response.setHeadPortrait(friendInfoResponse.getHeadPortrait());
                    lists.add(response);
                }
            }
            topAdapter.setNewData(lists);

            selectRecycler.setAdapter(topAdapter);

            topAdapter.setOnItemChildClickListener((adapter1, view1, position1) -> {
                topAdapter.getData().remove(position1);
                mAdapter.notifyDataSetChanged();
                topAdapter.notifyDataSetChanged();
            });
        });
    }


    /**
     * 获取好友列表
     */
    public void getFriendListById() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendListById()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendInfoResponses -> {
                    mAdapter.setNewData(friendInfoResponses);
                    list = friendInfoResponses;
                }, this::handleApiError);
    }

    /**
     * 创建群
     */
    public void makeGroup(String groupOwnerId, String customerIds) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .makeGroup(groupOwnerId, customerIds)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    SelectContactActivity.this.finish();
                    Intent intent = new Intent(SelectContactActivity.this, AgreeGroupChatActivity.class);
                    intent.putExtra("groupId", s);
                    startActivity(intent);
                }, this::handleApiError);
    }

    /**
     * 关于同意添加群的实现
     *
     * @param groupId
     * @param inviterId
     * @param customerIds
     */
    public void enterGroup(String groupId, String inviterId, String customerIds) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .enterGroup(groupId, inviterId, customerIds)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> ToastUtils.showShort(getString(R.string.add_group_chat)), this::handleApiError);

    }

    @OnClick({R.id.rl_back, R.id.tv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_commit:
                int types = 0;
                int type = getIntent().getIntExtra("addGroupType", types);
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < lists.size(); i++) {
                    sb.append(lists.get(i).getId());
                    sb.append(",");
                }
                if (type == 0) {
                    makeGroup(Constant.userId, Constant.userId + "," + sb.substring(0, sb.length() - 1));
                } else {
                    enterGroup(getIntent().getStringExtra("groupId"), Constant.userId, sb.substring(0, sb.length() - 1));
                }

                break;
        }
    }
}
