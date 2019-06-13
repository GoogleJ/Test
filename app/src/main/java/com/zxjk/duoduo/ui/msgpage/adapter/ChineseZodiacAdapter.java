package com.zxjk.duoduo.ui.msgpage.adapter;

import android.graphics.Color;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.response.DuobaoParameterResponse;

import java.util.List;

/**
 * *********************
 * Administrator
 * *********************
 * 2019/6/5
 * *********************
 * 12生肖
 * *********************
 */
public class ChineseZodiacAdapter extends BaseQuickAdapter<DuobaoParameterResponse.ChineseZodiacBean, BaseViewHolder> {
    public ChineseZodiacAdapter(int layoutResId, @Nullable List<DuobaoParameterResponse.ChineseZodiacBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DuobaoParameterResponse.ChineseZodiacBean item) {
        TextView tv_db_zodiac = helper.getView(R.id.tv_db_zodiac);
        tv_db_zodiac.setText(item.getZodiac());
        if (item.isChecked()) {
            tv_db_zodiac.setBackgroundResource(R.drawable.shape_db_orange);
            tv_db_zodiac.setTextColor(Color.WHITE);
        } else {
            tv_db_zodiac.setBackgroundResource(R.drawable.shape_db_white);
            tv_db_zodiac.setTextColor(Color.BLACK);
        }


    }
}
