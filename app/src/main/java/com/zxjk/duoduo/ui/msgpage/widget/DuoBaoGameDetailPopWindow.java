package com.zxjk.duoduo.ui.msgpage.widget;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.RecyclerItemAverageDecoration;

import razerdp.basepopup.BasePopupWindow;

public class DuoBaoGameDetailPopWindow extends BasePopupWindow {
    //期数
    private TextView tv1;

    //开奖号码
    private TextView tv2;

    //中奖金额
    private TextView tv3;

    //下注总额
    private TextView tv4;

    //开奖时间
    private TextView tv5;

    //中奖用户
    private TextView tv6;

    //剩余积分
    private TextView tv7;

    //下注号码
    private RecyclerView recycler;

    //开奖号码
    private LinearLayout ll1;

    //中奖金额
    private LinearLayout ll2;

    //中奖用户
    private LinearLayout ll3;

    //剩余积分
    private LinearLayout ll4;

    public DuoBaoGameDetailPopWindow(Context context) {
        super(context);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);
        tv7 = findViewById(R.id.tv7);

        recycler = findViewById(R.id.recycler);

        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll4 = findViewById(R.id.ll4);

        recycler.setLayoutManager(new GridLayoutManager(context, 5));
        recycler.addItemDecoration(new RecyclerItemAverageDecoration(0, CommonUtils.dip2px(context, 8), 5));
//        recycler.setAdapter(new BaseQuickAdapter<GetGameClassResponse.CommissionConfigBean, BaseViewHolder>(R.layout.item_duobao_xiazhu_number, ) {
//
//            @Override
//            protected void convert(BaseViewHolder helper, GetGameClassResponse.CommissionConfigBean item) {
//
//            }
//
//            @Override
//            public int getItemCount() {
//                return super.getItemCount();
//            }
//        });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_duobaogame_detail);
    }
}
