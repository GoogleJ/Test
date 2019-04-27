package com.zxjk.duoduo.ui.walletpage;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GetOverOrderResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.dialog.SelectPopupWindow;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.MD5Utils;

import java.text.SimpleDateFormat;

@SuppressLint("CheckResult")
public class OverOrderActivity extends BaseActivity {
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private ImageView iv;

    private GetOverOrderResponse data;

    private SelectPopupWindow selectPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_order);

        inoutPsw();

        data = (GetOverOrderResponse) getIntent().getSerializableExtra("data");

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);
        tv7 = findViewById(R.id.tv7);
        iv = findViewById(R.id.iv);

        tv1.setText(data.getBuyNick());
        tv2.setText(data.getMoney());
        tv3.setText(getIntent().getStringExtra("rate"));
        tv4.setText(data.getNumber());
        tv5.setText(data.getBothOrderId());

        if (data.getPayType().equals("1")) {
            tv6.setText(R.string.wechat);
        } else if (data.getPayType().equals("2")) {
            tv6.setText(R.string.alipay);
        } else {
            tv6.setText(R.string.bankcard);
        }

        tv7.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.parseLong(data.getCreateTime())));
    }


    private void inoutPsw() {
        selectPopupWindow = new SelectPopupWindow(OverOrderActivity.this, (psw, complete) -> {
            if (complete) {
                selectPopupWindow.dismiss();
                ServiceFactory.getInstance().getBaseService(Api.class)
                        .overOrder(data.getBuyId(), data.getBuyOrderId()
                                , data.getSellOrderId(), data.getBothOrderId(), MD5Utils.getMD5(psw))
                        .compose(bindToLifecycle())
                        .compose(RxSchedulers.normalTrans())
                        .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                        .subscribe(s -> {
                            ToastUtils.showShort(R.string.judgefinish);
                            finish();
                        }, this::handleApiError);

            }
        });
    }

    //拒绝审核
    @SuppressLint("CheckResult")
    public void rejectOrder(View view) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .rejectAudit(data.getBuyOrderId()
                        , data.getBothOrderId(), data.getSellOrderId())
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(s -> {
                    ToastUtils.showShort(R.string.rejectfinish);
                    finish();
                }, this::handleApiError);
    }

    //通过审核
    @SuppressLint("CheckResult")
    public void overOrder(View view) {
        KeyboardUtils.hideSoftInput(OverOrderActivity.this);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        selectPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);

    }

    public void back(View view) {
        finish();
    }
}
