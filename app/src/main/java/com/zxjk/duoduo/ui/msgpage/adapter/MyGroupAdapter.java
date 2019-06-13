package com.zxjk.duoduo.ui.msgpage.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.response.GetTeamInfoResponse;

public class MyGroupAdapter extends BaseQuickAdapter<GetTeamInfoResponse, BaseViewHolder> {
    public MyGroupAdapter() {
        super(R.layout.layout_getteam_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetTeamInfoResponse item) {
        helper.setText(R.id.tv_duoduoId, item.getDuoduoId());
        helper.setText(R.id.tv_nickname, item.getNick());
        helper.setText(R.id.tv_totalMoney, item.getBetTotalMoney());
        helper.setText(R.id.tv_grade, item.getGrade());
        helper.setText(R.id.tv_groupNum, String.valueOf(item.getTeamNum()));
        helper.setText(R.id.tv_lastContribution, item.getCurrentDirectPer());
        helper.setText(R.id.tv_lastGroupContribution, item.getCurrentTeamPer());
    }
}
