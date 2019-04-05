package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GetRedPackageRecordResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.ui.msgpage.adapter.RedPackageRecoderAdapter;
import com.zxjk.duoduo.ui.walletpage.adapter.ETHOrdersAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.functions.Consumer;

@SuppressLint("CheckResult")
public class RedPackageRecordFragment extends BaseFragment {

    private String type;
    private List<GetRedPackageRecordResponse.RedpackageListBean> data = new ArrayList<>();
    private RedPackageRecoderAdapter redPackageRecoderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = new SwipeRefreshLayout(getContext());
        rootView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        RecyclerView recyclerView = new RecyclerView(getContext());
        ((SwipeRefreshLayout) rootView).addView(recyclerView);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        redPackageRecoderAdapter = new RedPackageRecoderAdapter(data);
        recyclerView.setAdapter(redPackageRecoderAdapter);

        ((SwipeRefreshLayout) rootView).setRefreshing(true);

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getRedPackageRecord(type)
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .compose(bindToLifecycle())
                .subscribe(response -> redPackageRecoderAdapter.notifyDataSetChanged(), this::handleApiError);
        return rootView;
    }
}
