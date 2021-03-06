package com.zxjk.duoduo.ui.msgpage.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.response.GetExtractRecordResponse;
import com.zxjk.duoduo.utils.CommonUtils;

public class ExtractRewardRecordAdapter extends BaseQuickAdapter<GetExtractRecordResponse.ListBean, BaseViewHolder> {


    public ExtractRewardRecordAdapter() {
        super(R.layout.layout_extract_reward_record);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetExtractRecordResponse.ListBean item) {
        helper.setText(R.id.tv_time, CommonUtils.timeStamp2Date(String.valueOf(item.getCreateTime())));
        helper.setText(R.id.tv_hk, item.getHk() + "HK");
        helper.setImageResource(R.id.iv_head, R.drawable.ic_game_record_list1);
    }
}
