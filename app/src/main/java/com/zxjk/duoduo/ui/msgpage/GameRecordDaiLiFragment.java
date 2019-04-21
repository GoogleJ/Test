package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.utils.CommonUtils;

public class GameRecordDaiLiFragment extends BaseFragment {

    public String groupId;
    private boolean hasInitData = false;

    private TextView tvDaiLi;
    private TextView tvHk;
    private TextView tvShouYi;
    private TextView tvHkShouYi1;
    private TextView tvHkShouYi2;
    private TextView tvNum1;
    private TextView tvNum2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dailifanyong, container, false);

        tvDaiLi = rootView.findViewById(R.id.tvDaiLi);
        tvHk = rootView.findViewById(R.id.tvHk);
        tvShouYi = rootView.findViewById(R.id.tvShouYi);
        tvHkShouYi1 = rootView.findViewById(R.id.tvHkShouYi1);
        tvHkShouYi2 = rootView.findViewById(R.id.tvHkShouYi2);
        tvNum1 = rootView.findViewById(R.id.tvNum1);
        tvNum2 = rootView.findViewById(R.id.tvNum2);

        return rootView;
    }

    @SuppressLint("CheckResult")
    private void initData() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getRebateById(groupId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(getActivity())))
                .subscribe(response -> {
                    tvDaiLi.setText(response.getGrade());
                    tvNum1.setText(response.getTeamNum());
                    tvNum2.setText(response.getDirectNum());
                }, this::handleApiError);
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
