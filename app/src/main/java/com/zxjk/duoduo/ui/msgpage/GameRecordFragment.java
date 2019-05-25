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

import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GetDuoBaoIntegralDetailsResponse;
import com.zxjk.duoduo.network.response.GroupResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.ui.msgpage.adapter.GameGoldAdapter;
import com.zxjk.duoduo.ui.msgpage.adapter.GameRecordAdapter;
import com.zxjk.duoduo.ui.msgpage.widget.DuoBaoGameDetailPopWindow;
import com.zxjk.duoduo.utils.CommonUtils;

public class GameRecordFragment extends BaseFragment implements GameGoldAdapter.OnclickListener {

    public GroupResponse groupResponse;
    private boolean hasInitData = false;
    private GameRecordAdapter adapter1;
    private GameGoldAdapter adapter2;
    public String groupId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = new SwipeRefreshLayout(getContext());
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        ((SwipeRefreshLayout) rootView).addView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        rootView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        ((SwipeRefreshLayout) rootView).setOnRefreshListener(this::initData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (groupResponse.getGroupInfo().getGameType().equals("4")) {
            adapter2 = new GameGoldAdapter();
            adapter2.setOnclickListener(this);
            adapter2.groupId = groupId;
            adapter2.group = groupResponse;
            recyclerView.setAdapter(adapter2);
        } else {
            adapter1 = new GameRecordAdapter();
            adapter1.groupId = groupId;
            recyclerView.setAdapter(adapter1);
        }

        return rootView;
    }

    @SuppressLint("CheckResult")
    private void initData() {
        ((SwipeRefreshLayout) rootView).setRefreshing(true);
        if (groupResponse.getGroupInfo().getGameType().equals("4")) {
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getDuoBaoIntegralDetails(groupId)
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(getActivity())))
                    .subscribe(s -> {
                        hasInitData = true;
                        ((SwipeRefreshLayout) rootView).setRefreshing(false);
                        adapter2.setData(s);
                    }, t -> {
                        ((SwipeRefreshLayout) rootView).setRefreshing(false);
                        handleApiError(t);
                    });
        } else {
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getIntegralDetails(groupId)
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(getActivity())))
                    .subscribe(getIntegralDetailsResponses -> {
                        hasInitData = true;
                        ((SwipeRefreshLayout) rootView).setRefreshing(false);
                        adapter1.setData(getIntegralDetailsResponses);
                    }, t -> {
                        ((SwipeRefreshLayout) rootView).setRefreshing(false);
                        handleApiError(t);
                    });
        }
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

    //多宝item点击（群成员）
    @SuppressLint("CheckResult")
    @Override
    public void click(int position) {
        GetDuoBaoIntegralDetailsResponse r = adapter2.getData().get(position);
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getGroupMemberDuoBaoBetInfo(groupId, r.getExpect(), r.getCustomerId())
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(getActivity())))
                .subscribe(response -> {
                    DuoBaoGameDetailPopWindow duoBaoGameDetailPopWindow = new DuoBaoGameDetailPopWindow(getContext(), response, r);
                    duoBaoGameDetailPopWindow.showPopupWindow();
                }, this::handleApiError);
    }
}
