package com.zxjk.duoduo.ui.msgpage.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetRebateDetailsResponse;
import com.zxjk.duoduo.utils.CommonUtils;

public class DetailedStatementAdapter extends BaseQuickAdapter<GetRebateDetailsResponse.ListBean, BaseViewHolder> {
    public DetailedStatementAdapter() {
        super(R.layout.layout_detailed_statement);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetRebateDetailsResponse.ListBean item) {
        helper.setText(R.id.tv_nickname, "周收益");
        helper.setText(R.id.tv_time, CommonUtils.timeStamp2Date(String.valueOf(item.getCreateDate())));
        helper.setText(R.id.tv_hk, item.getRebateTotalAmount() + "HK");
        helper.setImageResource(R.id.iv_head, R.drawable.ic_game_record_list1);


    }
}
