package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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

    private TextView tvBalanceleftAuthenticateFlag; //是否认证tv
    private TextView tvBalanceleftBalance; //余额
    private TextView tvBalanceleftType; //币种
    LinearLayout verifiedLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_left);

        tvBalanceleftAuthenticateFlag = findViewById(R.id.tvBalanceleftAuthenticateFlag);
        tvBalanceleftBalance = findViewById(R.id.tvBalanceleftBalance);
        tvBalanceleftType = findViewById(R.id.tvBalanceleftType);
        verifiedLayout = findViewById(R.id.verified_layout);
        verifiedLayout.setOnClickListener(v -> {
            if (Constant.currentUser.getIsAuthentication().equals("1")) {
                startActivity(new Intent(BalanceLeftActivity.this, VerifiedActivity.class));
            }
            if (Constant.currentUser.getIsAuthentication().equals("2")) {
                ToastUtils.showShort(R.string.verifying_pleasewait);
            }
        });

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getBalanceHk()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(b -> tvBalanceleftBalance.setText(b.getBalanceHk()), this::handleApiError);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvBalanceleftAuthenticateFlag.setText(CommonUtils.getAuthenticate(Constant.currentUser.getIsAuthentication()));
    }

    //订单详情
    public void orderDetail(View view) {

    }

    //支付设置
    public void paySetting(View view) {
        startActivity(new Intent(this, PaySettingActivity.class));
    }


    public void back(View view) {
        finish();
    }

}
