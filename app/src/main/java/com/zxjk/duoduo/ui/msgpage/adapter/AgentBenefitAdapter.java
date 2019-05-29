package com.zxjk.duoduo.ui.msgpage.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetRebateInfoResponse;
import com.zxjk.duoduo.utils.ArithUtils;
import com.zxjk.duoduo.utils.DataUtils;

import java.math.BigDecimal;

public class AgentBenefitAdapter extends BaseQuickAdapter<GetRebateInfoResponse, BaseViewHolder> {
    public AgentBenefitAdapter() {
        super(R.layout.layout_agent_benefit);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetRebateInfoResponse item) {
        String min = "";
        String max = "";
        helper.setText(R.id.tv_grade, item.getGrade());
        BigDecimal data1 = new BigDecimal(item.getMin());
        BigDecimal data2 = new BigDecimal(100);
        BigDecimal data3 = new BigDecimal(item.getMax());
        if (data1.compareTo(data2) < 0) {
            min = DataUtils.getTwoDecimals(item.getMin());
        } else {
            double dMin = ArithUtils.div(Double.parseDouble(item.getMin()), Double.parseDouble("10000"));
            min = DataUtils.getTwoDecimals(String.valueOf(dMin)) + "W";
        }
        if (data3.compareTo(data2) < 0) {
            max = DataUtils.getTwoDecimals(item.getMax());
        } else {
            double dMax = ArithUtils.div(Double.parseDouble(item.getMax()), Double.parseDouble("10000"));
            max = DataUtils.getTwoDecimals(String.valueOf(dMax)) + "W";
        }
        helper.setText(R.id.tv_linePerformance, min + " - " + max);
        double p = ArithUtils.mul(Double.parseDouble(item.getCommission()), Double.parseDouble("10000"));
        helper.setText(R.id.tv_commissionRate, "每万返佣" + DataUtils.getInteger(String.valueOf(p)));

    }
}
