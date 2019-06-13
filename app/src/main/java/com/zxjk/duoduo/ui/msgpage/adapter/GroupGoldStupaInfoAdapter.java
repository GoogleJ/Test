package com.zxjk.duoduo.ui.msgpage.adapter;

import android.graphics.Color;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.response.GetGroupOwnerDuoBaoBetInfoResponse;
import com.zxjk.duoduo.utils.DataUtils;

import java.util.ArrayList;

/**
 * *********************
 * Administrator
 * *********************
 * 2019/5/24
 * *********************
 * 金多宝下注详情 群主
 * *********************
 */
public class GroupGoldStupaInfoAdapter extends BaseQuickAdapter<GetGroupOwnerDuoBaoBetInfoResponse, BaseViewHolder> {
    public GroupGoldStupaInfoAdapter() {
        super(R.layout.item_game_record, new ArrayList<>());
    }

    @Override
    protected void convert(BaseViewHolder helper, GetGroupOwnerDuoBaoBetInfoResponse item) {
        helper.setText(R.id.tvTitle, item.getTitle());
        if (!TextUtils.isEmpty(item.getTime())) {
            helper.setText(R.id.tvTime, DataUtils.timeStamp2Date(Long.parseLong(item.getTime()), "yyyy-MM-dd HH:mm"));
        } else {
            helper.setText(R.id.tvTime, "暂无时间");
        }

        if (item.getType().equals("0")) {
            helper.setText(R.id.tvLeft, "已中奖");
            helper.setText(R.id.tvHk, "+" + DataUtils.getTwoDecimals(item.getIntegral()) + "HK");
            helper.setTextColor(R.id.tvHk, Color.RED);
            helper.setTextColor(R.id.tvLeft, Color.RED);
        } else if (item.getType().equals("1")) {
            helper.setText(R.id.tvLeft, "未中奖");
            helper.setText(R.id.tvHk, "-" + DataUtils.getTwoDecimals(item.getIntegral()) + "HK");
            helper.setTextColor(R.id.tvHk, Color.BLACK);
            helper.setTextColor(R.id.tvLeft, Color.BLACK);
        }
    }
}
