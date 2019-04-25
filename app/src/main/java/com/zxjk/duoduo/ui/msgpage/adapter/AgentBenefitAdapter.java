package com.zxjk.duoduo.ui.msgpage.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetRebateInfoResponse;

public class AgentBenefitAdapter extends BaseQuickAdapter<GetRebateInfoResponse, BaseViewHolder> {
    public AgentBenefitAdapter() {
        super(R.layout.layout_agent_benefit);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetRebateInfoResponse item) {
        helper.setText(R.id.tv_grade, item.getGrade());
        helper.setText(R.id.tv_linePerformance, item.getMin() + " - " + item.getMax());
        helper.setText(R.id.tv_commissionRate, item.getCommission());

    }
}
