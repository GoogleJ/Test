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

/**
 * author L
 * create at 2019/5/8
 * description:代理返佣
 */
public class GameRecordDaiLiFragment extends BaseFragment {

    public String groupId;
    private boolean hasInitData = false;

    private TextView tvDaiLi;
    private TextView tv_totalPerformance;//总业绩
    private TextView tv_earnings;//收益
    private TextView tv_gainsBoard;//上局收益
    private TextView tv_totalRevenue;//总收益
    private TextView tv_totalTeamMembers;//团队总人数
    private TextView tv_KeepPushingNumber;//直推人数
    private DecimalFormat df = new DecimalFormat("0.00%");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dailifanyong, container, false);

        tvDaiLi = rootView.findViewById(R.id.tvDaiLi);
        tv_totalPerformance = rootView.findViewById(R.id.tv_totalPerformance);
        tv_earnings = rootView.findViewById(R.id.tv_earnings);
        tv_gainsBoard = rootView.findViewById(R.id.tv_gainsBoard);
        tv_totalRevenue = rootView.findViewById(R.id.tv_totalRevenue);
        tv_totalTeamMembers = rootView.findViewById(R.id.tv_totalTeamMembers);
        tv_KeepPushingNumber = rootView.findViewById(R.id.tv_KeepPushingNumber);

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
                    //总业绩
                    tv_totalPerformance.setText(response.getTeamTotalPer() + "HK");
                    //收益
                    tv_earnings.setText(df.format(Double.parseDouble(response.getRebateRate())));
                    //上局收益
                    tv_gainsBoard.setText(response.getRebateAmount() + "HK");
                    //总收益
                    tv_totalRevenue.setText(response.getRebateTotalAmount() + "HK");
                    //团队总人数
                    tv_totalTeamMembers.setText(response.getTeamNum());
                    //直推人数
                    tv_KeepPushingNumber.setText(response.getDirectNum());

                    //我的代理
                    rootView.findViewById(R.id.ll_myAgency).setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), AgentBenefitActivity.class);
                        intent.putExtra("groupId", response.getGroupId());
                        startActivity(intent);
                    });
                    //上周收益
                    rootView.findViewById(R.id.ll_lastEarnings).setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), ExtractRewardActivity.class);
                        intent.putExtra("groupId", response.getGroupId());
                        startActivity(intent);
                    });
                    //总收益
                    rootView.findViewById(R.id.ll_totalEarnings).setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), DetailedStatementActivity.class);
                        intent.putExtra("groupId", response.getGroupId());
                        intent.putExtra("currentDirectPer", response.getCurrentDirectPer());
                        intent.putExtra("currentTeamPer", response.getCurrentTeamPer());
                        startActivity(intent);
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
