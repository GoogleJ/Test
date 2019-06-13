package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.bean.response.GetExtractRecordResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.ExtractRewardRecordAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 提取奖励记录
 */
public class ExtractRewardRecordActivity extends BaseActivity {

    private TextView tv_title;
    private String groupId;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private int page = 1;
    private int offset = 15;
    private boolean hasInitData;
    private boolean hasNextPage = true;

    private ExtractRewardRecordAdapter extractRewardRecordAdapter;
    private List<GetExtractRecordResponse.ListBean> listBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract_reward_record);

        initView();
        initAdapter();
        initData();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.extract_reward_record));
        groupId = getIntent().getStringExtra("groupId");
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
    }

    private void initAdapter() {
        recyclerView = findViewById(R.id.recyclerView);
        extractRewardRecordAdapter = new ExtractRewardRecordAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(extractRewardRecordAdapter);
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

                }
            }
        });
    }

    @SuppressLint("CheckResult")
    private void initData() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getExtractRecord(groupId, page, offset)
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
                        extractRewardRecordAdapter.setNewData(listBeans);
                    } else {
                        extractRewardRecordAdapter.addData(listBeans);
                    }
                    extractRewardRecordAdapter.notifyDataSetChanged();
                    if (!hasInitData) {
                        hasInitData = true;
                    }

                }, t -> {
                    handleApiError(t);
                    swipeRefreshLayout.setRefreshing(false);

                });


    }

}
