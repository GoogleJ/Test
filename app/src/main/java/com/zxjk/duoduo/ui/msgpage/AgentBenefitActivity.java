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
import com.zxjk.duoduo.ui.msgpage.adapter.AgentBenefitAdapter;

/**
 * 代理收益
 */
@SuppressLint("CheckResult")
public class AgentBenefitActivity extends BaseActivity {
    private String groupId;

    private AgentBenefitAdapter agentBenefitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_benefit);
        initView();
        initData();
    }

    private void initView() {
        groupId = getIntent().getStringExtra("groupId");
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.agent_benefit));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        initAdapter();
    }

    private void initAdapter() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        agentBenefitAdapter = new AgentBenefitAdapter();
        recyclerView.setAdapter(agentBenefitAdapter);
    }

    private void initData() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getRebateInfoByGroupId(groupId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(getRebateInfoResponses ->
                                agentBenefitAdapter.setNewData(getRebateInfoResponses),
                        this::handleApiError);

    }
}
