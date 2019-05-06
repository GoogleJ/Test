package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;

@SuppressLint("CheckResult")
public class BalanceLeftActivity extends BaseActivity {

    private TextView tv_authentication; //是否认证tv
    private TextView tv_balance; //余额
    private TextView tv_currency; //币种

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_left);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.balance));
        tv_balance = findViewById(R.id.tv_balance);
        tv_currency = findViewById(R.id.tv_currency);
        tv_authentication = findViewById(R.id.tv_authentication);

        //返回
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        //账单明细
        findViewById(R.id.rl_billingDetails).setOnClickListener(v ->
                startActivity(new Intent(BalanceLeftActivity.this, DetailListActivity.class)));
        //支付设置
        findViewById(R.id.rl_PaymentSettings).setOnClickListener(v ->
                startActivity(new Intent(BalanceLeftActivity.this, PaySettingActivity.class)));
        //实名认证
        findViewById(R.id.rl_realNameAuthentication).setOnClickListener(v -> {
            if (Constant.currentUser.getIsAuthentication().equals("2")) {
                ToastUtils.showShort(R.string.verifying_pleasewait);
            } else if (!Constant.currentUser.getIsAuthentication().equals("0") &&
                    !Constant.currentUser.getIsAuthentication().equals("1")) {
                startActivity(new Intent(this, VerifiedActivity.class));
            }
        });

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getBalanceHk()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(b -> tv_balance.setText(b.getBalanceHk()), this::handleApiError);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_authentication.setText(CommonUtils.getAuthenticate(Constant.currentUser.getIsAuthentication()));
    }


}
