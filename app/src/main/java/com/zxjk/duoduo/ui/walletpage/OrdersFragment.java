package com.zxjk.duoduo.ui.walletpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.BaseResponse;
import com.zxjk.duoduo.network.response.GetTransferAllResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.ui.walletpage.adapter.BlockOrderAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class OrdersFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private boolean hasInitData;
    private boolean hasNextPage = true;

    public String type;

    private int page = 1;
    private int offset = 15;
    private BlockOrderAdapter blockOrderAdapter;
    private List<GetTransferAllResponse.ListBean> data = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = new SwipeRefreshLayout(getContext());
        recyclerView = new RecyclerView(getContext());
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        ((SwipeRefreshLayout) rootView).addView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        rootView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        ((SwipeRefreshLayout) rootView).setOnRefreshListener(() -> {
            page = 1;
            offset = 15;
            initData();
        });

        blockOrderAdapter = new BlockOrderAdapter();
        blockOrderAdapter.setData(data);
        blockOrderAdapter.setOnClickListener(data -> {
            Intent intent = new Intent(getActivity(), BlockOrderDetailActivity.class);
            intent.putExtra("data", data);
            intent.putExtra("type",data.getSerialType());
            startActivity(intent);
        });
        recyclerView.setAdapter(blockOrderAdapter);

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
                    ((SwipeRefreshLayout) rootView).setRefreshing(true);
                    initData();
                }
            }
        });

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if ((null != rootView) && getUserVisibleHint() && !hasInitData) {
            ((SwipeRefreshLayout) rootView).setRefreshing(true);
            initData();
        }
    }

    @Override
    public void onStart() {
        if (getUserVisibleHint() && !hasInitData) {
            ((SwipeRefreshLayout) rootView).setRefreshing(true);
            initData();
        }
        super.onStart();
    }

    @SuppressLint("CheckResult")
    private void initData() {
        Api service = ServiceFactory.getInstance().getBaseService(Api.class);
        Observable<BaseResponse<GetTransferAllResponse>> upstream;
        switch (type) {
            case "1":
                upstream = service.getTransferAll(Constant.walletResponse.getWalletAddress(), String.valueOf(page), String.valueOf(offset));
                break;
            case "2":
                upstream = service.getTransfer(Constant.walletResponse.getWalletAddress(), String.valueOf(page), String.valueOf(offset));
                break;
            case "3":
                upstream = service.getTransferOut(Constant.walletResponse.getWalletAddress(), String.valueOf(page), String.valueOf(offset));
                break;
            case "4":
                upstream = service.getTransferIn(Constant.walletResponse.getWalletAddress(), String.valueOf(page), String.valueOf(offset));
                break;
            case "5":
                upstream = service.getSerialsFail(Constant.walletResponse.getWalletAddress(), String.valueOf(page), String.valueOf(offset));
                break;
            default:
                upstream = service.getTransferAll(Constant.walletResponse.getWalletAddress(), String.valueOf(page), String.valueOf(offset));
        }

        upstream.compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(response -> {
                    hasNextPage = response.isHasNextPage();
                    ((SwipeRefreshLayout) rootView).setRefreshing(false);
                    if (!hasNextPage && hasInitData && page != 1) {
                        ToastUtils.showShort(R.string.nomore);
                        return;
                    }
                    if (page == 1) {
                        data.clear();
                    }
                    data.addAll(response.getList());
                    blockOrderAdapter.notifyDataSetChanged();
                    if (!hasInitData){ hasInitData = true;}
                }, t -> {
                    handleApiError(t);
                    ((SwipeRefreshLayout) rootView).setRefreshing(false);
                });
    }
}