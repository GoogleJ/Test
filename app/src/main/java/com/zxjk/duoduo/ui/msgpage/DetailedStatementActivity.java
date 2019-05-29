package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GetRebateDetailsResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.DetailedStatementAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 收益明细表
 */
@SuppressLint("CheckResult")
public class DetailedStatementActivity extends BaseActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView tv_currentDirectPer, tv_currentTeamPer;
    private String groupId, currentDirectPer, currentTeamPer;
    private LinearLayout llTop;

    private int page = 1;
    private int offset = 15;
    private boolean hasInitData;
    private boolean hasNextPage = true;

    private DetailedStatementAdapter detailedStatementAdapter;
    private List<GetRebateDetailsResponse.ListBean> listBeans = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_statement);
        initView();
        initAdapter();
        initData();
    }

    private void initAdapter() {
        detailedStatementAdapter = new DetailedStatementAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(detailedStatementAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    if (!hasNextPage) {
                        ToastUtils.showShort(R.string.nomore);
                        return;
                    }
                    page += 1;
                    swipeRefreshLayout.setRefreshing(true);
                    initData();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        TextView tv_title = findViewById(R.id.tv_title);
        llTop = findViewById(R.id.llTop);
        tv_title.setText(getString(R.string.detailed_statement));
        groupId = getIntent().getStringExtra("groupId");
        currentDirectPer = getIntent().getStringExtra("currentDirectPer");
        currentTeamPer = getIntent().getStringExtra("currentTeamPer");
        tv_currentDirectPer = findViewById(R.id.tv_currentDirectPer);
        tv_currentTeamPer = findViewById(R.id.tv_currentTeamPer);
        tv_currentDirectPer.setText(currentDirectPer + "HK");
        tv_currentTeamPer.setText(currentTeamPer + "HK");
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            page = 1;
            offset = 15;
            initData();
            swipeRefreshLayout.setRefreshing(false);
        });
        llTop.setVisibility(getIntent().getBooleanExtra("duobao", false) ? View.GONE : View.VISIBLE);
    }

    private void initData() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getRebateDetails(groupId, page, offset)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(getRebateDetailsResponse -> {
                    listBeans = getRebateDetailsResponse.getList();
                    swipeRefreshLayout.setRefreshing(false);
                    hasNextPage = getRebateDetailsResponse.isHasNextPage();
                    if (!hasNextPage && hasInitData && page != 1) {
                        ToastUtils.showShort(R.string.nomore);
                        return;
                    }
                    if (page == 1) {
                        detailedStatementAdapter.setNewData(listBeans);
                    } else {
                        detailedStatementAdapter.addData(listBeans);
                    }
                    detailedStatementAdapter.notifyDataSetChanged();
                    if (!hasInitData) {
                        hasInitData = true;
                    }

                }, t -> {
                    handleApiError(t);
                    swipeRefreshLayout.setRefreshing(false);

                });
    }
}
