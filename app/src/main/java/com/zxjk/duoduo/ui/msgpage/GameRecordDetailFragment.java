package com.zxjk.duoduo.ui.msgpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zxjk.duoduo.network.response.GetBetInfoDetailsResponse;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.ui.msgpage.adapter.GameRecordDetailAdapter;
import java.util.List;

public class GameRecordDetailFragment extends BaseFragment {
    private boolean hasInitData = false;

    public List<GetBetInfoDetailsResponse.BetInfoBean.Bean> data;
    public GetBetInfoDetailsResponse.GroupOwnerBean ownerBean;
    public int type;

    private RecyclerView recyclerView;
    private GameRecordDetailAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = new RecyclerView(container.getContext());
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        initData();
        return recyclerView;
    }

    private void initData() {
        adapter = new GameRecordDetailAdapter();
        adapter.data = data;
        adapter.ownerBean = ownerBean;
        adapter.type = type;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if ((null != rootView) && getUserVisibleHint() && !hasInitData) {
//            initData();
//        }
//    }
//
//    @Override
//    public void onStart() {
//        if (getUserVisibleHint() && !hasInitData) {
//            initData();
//        }
//        super.onStart();
//    }
}
