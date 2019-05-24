package com.zxjk.duoduo.ui.msgpage.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetRebateInfoResponse;
import com.zxjk.duoduo.utils.ArithUtils;
import com.zxjk.duoduo.utils.DataUtils;

public class AgentBenefitAdapter extends BaseQuickAdapter<GetRebateInfoResponse, BaseViewHolder> {
    public AgentBenefitAdapter() {
        super(R.layout.layout_agent_benefit);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetRebateInfoResponse item) {
        helper.setText(R.id.tv_grade, item.getGrade());
        helper.setText(R.id.tv_linePerformance, item.getMin() + " - " + item.getMax());
        double p = ArithUtils.mul(Double.parseDouble(item.getCommission()), Double.parseDouble("10000"));
        helper.setText(R.id.tv_commissionRate, "每万返佣" + DataUtils.getInteger(String.valueOf(p)));

    }
}
