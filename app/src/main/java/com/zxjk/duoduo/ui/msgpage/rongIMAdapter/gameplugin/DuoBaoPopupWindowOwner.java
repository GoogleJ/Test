package com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetDuoBaoIntegralDetailsResponse;
import com.zxjk.duoduo.utils.DataUtils;

import java.text.SimpleDateFormat;

import razerdp.basepopup.BasePopupWindow;

public class DuoBaoPopupWindowOwner extends BasePopupWindow {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    public DuoBaoPopupWindowOwner(Context context) {
        super(context);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
    }

    public void show(GetDuoBaoIntegralDetailsResponse bean) {
        tv1.setText("本期期数：" + bean.getExpect());
        if (TextUtils.isEmpty(bean.getBetCount())) {
            tv2.setText("截止目前下注人数：0人");
        } else {
            tv2.setText("截止目前下注人数：" + Integer.parseInt(bean.getBetCount()) + "人");
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
