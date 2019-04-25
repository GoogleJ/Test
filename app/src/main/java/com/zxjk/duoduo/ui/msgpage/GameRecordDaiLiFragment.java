package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

import java.text.DecimalFormat;

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
    DecimalFormat df = new DecimalFormat("0.00%");

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

    @SuppressLint({"CheckResult", "SetTextI18n"})
    private void initData() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getRebateById(groupId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(getActivity())))
                .subscribe(response -> {
                    hasInitData = true;
                    tvDaiLi.setText(response.getGrade());
                    tvNum1.setText(response.getTeamNum());
                    tvNum2.setText(response.getDirectNum());
                    tvShouYi.setText(df.format(Double.parseDouble(response.getRebateRate())));
                    tvHk.setText(response.getCurrentTeamPer() + "HK");
                    tvHkShouYi1.setText(response.getRebateAmount() + "HK");
                    tvHkShouYi2.setText(response.getRebateTotalAmount() + "HK");
                    //我的代理
                    rootView.findViewById(R.id.ll_myAgency).setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), AgentBenefitActivity.class);
                        intent.putExtra("groupId", response.getGroupId());
                        startActivity(intent);
                    });
                    //上周收益
                    rootView.findViewById(R.id.ll_lastEarnings).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), ExtractRewardActivity.class);
                            intent.putExtra("groupId", response.getGroupId());
                            startActivity(intent);


                        }
                    });
                    //总收益
                    rootView.findViewById(R.id.ll_totalEarnings).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), DetailedStatementActivity.class);
                            intent.putExtra("groupId", response.getGroupId());
                            intent.putExtra("currentDirectPer", response.getCurrentDirectPer());
                            intent.putExtra("currentTeamPer", response.getCurrentTeamPer());

                            startActivity(intent);


                        }
                    });


                    //我的团队
                    rootView.findViewById(R.id.ll_myGroup).setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), MyGroupActivity.class);
                        intent.putExtra("groupId", response.getGroupId());
                        startActivity(intent);
                    });


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
