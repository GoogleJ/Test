package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.GroupGoldStupaInfoAdapter;
import com.zxjk.duoduo.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * *********************
 * Administrator
 * *********************
 * 2019/5/24
 * *********************
 * 金多宝下注详情 群主
 * *********************
 */
public class GroupGoldStupaInfoActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    private GroupGoldStupaInfoAdapter goldStupaInfoAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String groupId;
    private String expect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_gold_stupa_info);
        ButterKnife.bind(this);
        initView();
        initData();
        data();
    }


    private void initView() {
        tvTitle.setText("下注详情");
        groupId = getIntent().getStringExtra("groupId");
        expect = getIntent().getStringExtra("expect");

    }

    private void initData() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        goldStupaInfoAdapter = new GroupGoldStupaInfoAdapter();
        recyclerView.setAdapter(goldStupaInfoAdapter);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data();
            }
        });

    }


    @SuppressLint("CheckResult")
    private void data() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getGroupOwnerDuoBaoBetInfo(groupId, expect)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(responses -> {
                    goldStupaInfoAdapter.setNewData(responses);
                }, t -> {

                    handleApiError(t);
                });


    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }
}
