package com.zxjk.duoduo.ui.msgpage.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetDuoBaoIntegralDetailsResponse;
import com.zxjk.duoduo.network.response.GetGroupMemberDuoBaoBetInfoResponse;
import com.zxjk.duoduo.network.response.GetGroupOwnerDuoBaoBetInfoResponse;
import com.zxjk.duoduo.ui.msgpage.GroupGoldStupaInfoActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.DataUtils;
import com.zxjk.duoduo.utils.RecyclerItemAverageDecoration;
import com.zxjk.duoduo.view.ScreenUtil;

import razerdp.basepopup.BasePopupWindow;

public class DuoBaoGameDetailPopWindow extends BasePopupWindow {
    private TextView tv;
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

    public DuoBaoGameDetailPopWindow(Context context, GetGroupMemberDuoBaoBetInfoResponse response, GetDuoBaoIntegralDetailsResponse r) {
        super(context);

        tv = findViewById(R.id.tv);
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

        if (response.getBetInfo().size() > 10) {
            tv.setVisibility(View.VISIBLE);
        }

        if (response.getBetInfo().size() <= 5) {
            recycler.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                ViewGroup.LayoutParams layoutParams = recycler.getLayoutParams();
                if (layoutParams.height != ScreenUtil.dip2px(50)) {
                    layoutParams.height = ScreenUtil.dip2px(50);
                    recycler.setLayoutParams(layoutParams);
                }
            });
        }

        recycler.setLayoutManager(new GridLayoutManager(context, 5));
        recycler.addItemDecoration(new RecyclerItemAverageDecoration(0, CommonUtils.dip2px(context, 8), 5));
        recycler.setAdapter(new BaseQuickAdapter<GetGroupMemberDuoBaoBetInfoResponse.BetInfoBean, BaseViewHolder>(R.layout.item_duobao_xiazhu_number, response.getBetInfo()) {
            @Override
            protected void convert(BaseViewHolder helper, GetGroupMemberDuoBaoBetInfoResponse.BetInfoBean item) {
                helper.setText(R.id.tv1, item.getBetCode());
                helper.setText(R.id.tv2, item.getBetMoneyForCode());
            }
        });

        if (!TextUtils.isEmpty(response.getOpenCode())) {
            ll1.setVisibility(View.VISIBLE);
            tv2.setText(response.getOpenCode());
        }

        tv4.setText(response.getBetSum() + "HK");
        tv1.setText(response.getExpect());

        if (!r.getType().equals("2")) {
            ll2.setVisibility(View.VISIBLE);
            if (r.getType().equals("0")) {
                //转进
                if (r.getIntegral().contains("-")) {
                    tv3.setText(r.getIntegral() + "HK");
                } else {
                    tv3.setText("+" + r.getIntegral() + "HK");
                }
                tv3.setTextColor(ContextCompat.getColor(context, R.color.red_eth_in));
            } else if (r.getType().equals("1")) {
                //转出
                tv3.setText("0HK");
                tv3.setTextColor(ContextCompat.getColor(context, R.color.textcolor2));
            }
        }

        if (!TextUtils.isEmpty(response.getOpentime())) {
            tv5.setText(DataUtils.timeStamp2Date(Long.parseLong(response.getOpentime()), "yyyy-MM-dd HH:mm"));
        } else {
            tv5.setText("暂无时间");
        }

        if (response.getWinerList().size() != 0) {
            ll3.setVisibility(View.VISIBLE);
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : response.getWinerList()) {
                stringBuilder.append(str + ",");
            }
            tv6.setText(stringBuilder.substring(0, stringBuilder.length() - 1));
        }

        if (!TextUtils.isEmpty(r.getRemainingIntegral())) {
            ll4.setVisibility(View.VISIBLE);
            tv7.setText(r.getRemainingIntegral() + "HK");
        }
    }

    public DuoBaoGameDetailPopWindow(GroupGoldStupaInfoActivity context, GetGroupMemberDuoBaoBetInfoResponse response, GetGroupOwnerDuoBaoBetInfoResponse o) {
        super(context);

        tv = findViewById(R.id.tv);
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

        if (response.getBetInfo().size() > 10) {
            tv.setVisibility(View.VISIBLE);

        }
        if (response.getBetInfo().size() <= 5) {
            recycler.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                ViewGroup.LayoutParams layoutParams = recycler.getLayoutParams();
                if (layoutParams.height != ScreenUtil.dip2px(50)) {
                    layoutParams.height = ScreenUtil.dip2px(50);
                    recycler.setLayoutParams(layoutParams);
                }
            });
        }

        recycler.setLayoutManager(new GridLayoutManager(context, 5));
        recycler.addItemDecoration(new RecyclerItemAverageDecoration(0, CommonUtils.dip2px(context, 8), 5));
        recycler.setAdapter(new BaseQuickAdapter<GetGroupMemberDuoBaoBetInfoResponse.BetInfoBean, BaseViewHolder>(R.layout.item_duobao_xiazhu_number, response.getBetInfo()) {
            @Override
            protected void convert(BaseViewHolder helper, GetGroupMemberDuoBaoBetInfoResponse.BetInfoBean item) {
                helper.setText(R.id.tv1, item.getBetCode());
                helper.setText(R.id.tv2, item.getBetMoneyForCode());
            }
        });

        if (!TextUtils.isEmpty(response.getOpenCode())) {
            ll1.setVisibility(View.VISIBLE);
            tv2.setText(response.getOpenCode());
        }

        tv4.setText(response.getBetSum() + "HK");
        tv1.setText(response.getExpect());

        if (!o.getType().equals("2")) {
            ll2.setVisibility(View.VISIBLE);
            if (o.getType().equals("0")) {
                //转进
                if (o.getIntegral().contains("-")) {
                    tv3.setText(o.getIntegral() + "HK");
                } else {
                    tv3.setText("+" + o.getIntegral() + "HK");
                }
                tv3.setTextColor(ContextCompat.getColor(context, R.color.red_eth_in));
            } else if (o.getType().equals("1")) {
                //转出
                tv3.setText("0HK");
                tv3.setTextColor(ContextCompat.getColor(context, R.color.textcolor2));
            }
        }

        if (!TextUtils.isEmpty(response.getOpentime())) {
            tv5.setText(DataUtils.timeStamp2Date(Long.parseLong(response.getOpentime()), "yyyy-MM-dd HH:mm"));
        } else {
            tv5.setText("暂无时间");
        }

        if (response.getWinerList().size() != 0) {
            ll3.setVisibility(View.VISIBLE);
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : response.getWinerList()) {
                stringBuilder.append(str + ",");
            }

            tv6.setText(stringBuilder.substring(0, stringBuilder.length() - 1));
        }

        if (!TextUtils.isEmpty(o.getRemainingIntegral())) {
            ll4.setVisibility(View.VISIBLE);
            tv7.setText(o.getRemainingIntegral() + "HK");
        }
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_duobaogame_detail);
    }
}
