package com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetIntegralDetailsResponse;

import java.text.SimpleDateFormat;

import razerdp.basepopup.BasePopupWindow;

public class GameRecordPopupWindow extends BasePopupWindow {

    private ImageView iv;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public GameRecordPopupWindow(Context context) {
        super(context);

        iv = findViewById(R.id.iv);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
    }

    public void show(int type, GetIntegralDetailsResponse bean) {
        if (type == 1) {
            iv.setImageResource(R.drawable.ic_shangfen_big);
            tv1.setText("上分金额：" + bean.getIntegral() + "HK");
            tv2.setText("上分时间：" + sf.format(Long.parseLong(bean.getTime())));
            tv3.setText("剩余积分：" + bean.getRemainingIntegral() + "HK");
        }
        if (type == 2) {
            iv.setImageResource(R.drawable.ic_xiafen_big);
            tv1.setText("下分金额：" + bean.getIntegral() + "HK");
            tv2.setText("下分时间：" + sf.format(Long.parseLong(bean.getTime())));
            tv3.setText("剩余积分：" + bean.getRemainingIntegral() + "HK");
        }
        if (type == 3) {
            iv.setImageResource(R.drawable.ic_liuju_big);
            tv1.setText("流局金额：" + bean.getIntegral() + "HK");
            tv2.setText("流局时间：" + sf.format(Long.parseLong(bean.getTime())));
            tv3.setText("剩余积分：" + bean.getRemainingIntegral() + "HK");
        }
        if (type == 4) {
            iv.setImageResource(R.drawable.ic_tixian_big);
            tv1.setText("提现金额：" + bean.getIntegral() + "HK");
            tv2.setText("提现时间：" + sf.format(Long.parseLong(bean.getTime())));
            tv3.setText("剩余积分：" + bean.getRemainingIntegral() + "HK");
        }
        showPopupWindow();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_game_record);
    }
}
