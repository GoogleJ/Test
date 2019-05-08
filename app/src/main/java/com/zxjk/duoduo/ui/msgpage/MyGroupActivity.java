package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.MyGroupAdapter;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 *author L
 *create at 2019/5/8
 *description: 我的团队
 *
 */
@SuppressLint("CheckResult")
public class MyGroupActivity extends BaseActivity implements CancelAdapt {

    private String groupId;
    private MyGroupAdapter myGroupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group);
        initView();
        initData();
    }

    private void initView() {
        groupId = getIntent().getStringExtra("groupId");
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.my_group));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        initAdapter();
    }

    private void initAdapter() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myGroupAdapter = new MyGroupAdapter();
        recyclerView.setAdapter(myGroupAdapter);
    }

    private void initData() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getTeamInfo(groupId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(getTeamInfoResponses -> myGroupAdapter.setNewData(getTeamInfoResponses), this::handleApiError);

    }
}
