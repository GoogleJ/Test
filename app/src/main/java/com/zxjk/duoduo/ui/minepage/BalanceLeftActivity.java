package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;

@SuppressLint("CheckResult")
public class BalanceLeftActivity extends BaseActivity {

    private TextView tvBalanceleftAuthenticateFlag; //是否认证tv
    private TextView tvBalanceleftBalance; //余额
    private TextView tvBalanceleftType; //币种

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_left);

        tvBalanceleftAuthenticateFlag = findViewById(R.id.tvBalanceleftAuthenticateFlag);
        tvBalanceleftBalance = findViewById(R.id.tvBalanceleftBalance);
        tvBalanceleftType = findViewById(R.id.tvBalanceleftType);

        tvBalanceleftAuthenticateFlag.setText(CommonUtils.getAuthenticate(Constant.currentUser.getIsAuthentication()));

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getBalanceHk()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(b -> tvBalanceleftBalance.setText(b.getBalanceHk()), this::handleApiError);
    }

    //订单详情
    public void orderDetail(View view) {

    }

    //支付设置
    public void paySetting(View view) {

    }

    //实名认证
    public void authenticate(View view) {

    }

    public void back(View view) {
        finish();
    }
}
