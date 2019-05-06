package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GetRebatePayRecordResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.ui.msgpage.adapter.GameRecordOwnerAdapter;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class GameRecordOwnerFragment extends BaseFragment {
    public String groupId;
    private boolean hasInitData = false;
    private boolean hasNextPage = true;
    private GameRecordOwnerAdapter adapter;
    private int page = 1;
    private List<GetRebatePayRecordResponse.ListBean> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = new SwipeRefreshLayout(getContext());
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        ((SwipeRefreshLayout) rootView).addView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        rootView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        ((SwipeRefreshLayout) rootView).setOnRefreshListener(() -> {
            page = 1;
            initData();
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GameRecordOwnerAdapter();
        adapter.setData(data);
        adapter.groupId = groupId;
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    if (!hasNextPage && hasInitData) {
                        ToastUtils.showShort(R.string.nomore);
                        return;
                    }
                    page += 1;
                    ((SwipeRefreshLayout) rootView).setRefreshing(true);
                    initData();
                }
            }
        });
        return rootView;
    }

    @SuppressLint("CheckResult")
    private void initData() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getRebatePayRecord(groupId, 1, 15)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(getActivity())))
                .subscribe(response -> {
                    ((SwipeRefreshLayout) rootView).setRefreshing(false);
                    hasNextPage = response.isHasNextPage();
                    if (!hasNextPage && hasInitData && page != 1) {
                        ToastUtils.showShort(R.string.nomore);
                        return;
                    }
                    if (page == 1) {
                        data.clear();
                    } else {
                        page += 1;
                    }
                    data.addAll(response.getList());
                    adapter.notifyDataSetChanged();
                    hasInitData = true;
                }, t -> {
                    ((SwipeRefreshLayout) rootView).setRefreshing(false);
                    handleApiError(t);
                });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if ((null != rootView) && getUserVisibleHint() && !hasInitData) {
            initData();
        }
    }

    @Override
    public void onStart() {
        if (getUserVisibleHint() && !hasInitData) {
            initData();
        }
        super.onStart();
    }
}
