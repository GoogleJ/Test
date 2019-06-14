package com.zxjk.duoduo.ui.msgpage.rongIM.plugin.game;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.response.GetDuoBaoIntegralDetailsResponse;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.DataUtils;
import com.zxjk.duoduo.utils.RecyclerItemAverageDecoration;

import razerdp.basepopup.BasePopupWindow;

public class DuoBaoPopupWindowOwner extends BasePopupWindow {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private RecyclerView recycler;

    private Context context;

    public DuoBaoPopupWindowOwner(Context context) {
        super(context);
        this.context = context;

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        recycler = findViewById(R.id.recycler);
    }

    public void show(GetDuoBaoIntegralDetailsResponse bean) {
        tv1.setText("本期期数：" + bean.getExpect());
        if (bean.getBetInfo().size() == 0) {
            recycler.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(bean.getBetCount())) {
            tv2.setText("截止目前下注人数：0人");
        } else {
            tv2.setText("截止目前下注人数：" + Integer.parseInt(bean.getBetCount()) + "人");
            recycler.setLayoutManager(new GridLayoutManager(context, 5));
            recycler.addItemDecoration(new RecyclerItemAverageDecoration(0, CommonUtils.dip2px(context, 12) / 2 * 2, 5));
            recycler.setAdapter(new BaseQuickAdapter<GetDuoBaoIntegralDetailsResponse.BetInfoBean, BaseViewHolder>(R.layout.item_duobao_xiazhu_number, bean.getBetInfo()) {
                @Override
                protected void convert(BaseViewHolder helper, GetDuoBaoIntegralDetailsResponse.BetInfoBean item) {
                    helper.setText(R.id.tv1, item.getBetCode());
                    helper.setText(R.id.tv2, item.getBetMoneyForCode());
                }
            });
        }
        tv3.setText("下注总额：" + bean.getBetSum() + "HK");
        if (TextUtils.isEmpty(bean.getTime())) {
            tv4.setText("开奖时间：" + "暂无");
        } else {
            tv4.setText("开奖时间：" + DataUtils.timeStamp2Date(Long.parseLong(bean.getTime()), "yyyy-MM-dd HH:mm"));
        }

        showPopupWindow();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_duobaogame_owner);
    }
}
