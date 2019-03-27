package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
    LinearLayout verifiedLayout;
    String verifiedType;
    String type="verifiedType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_left);

        tvBalanceleftAuthenticateFlag = findViewById(R.id.tvBalanceleftAuthenticateFlag);
        tvBalanceleftBalance = findViewById(R.id.tvBalanceleftBalance);
        tvBalanceleftType = findViewById(R.id.tvBalanceleftType);

        tvBalanceleftAuthenticateFlag.setText(CommonUtils.getAuthenticate(Constant.currentUser.getIsAuthentication()));
        getVerified();
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getBalanceHk()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(b -> tvBalanceleftBalance.setText(b.getBalanceHk()), this::handleApiError);
        verifiedLayout = findViewById(R.id.verified_layout);
        initView();
    }

    private void initView() {
        verifiedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BalanceLeftActivity.this, VerifiedActivity.class));

            }
        });
    }

    //订单详情
    public void orderDetail(View view) {

    }

    //支付设置
    public void paySetting(View view) {
//        startActivity(new Intent(this, PaySettingActivity.class));
        Intent intent=new Intent(this, PaySettingActivity.class);
        intent.putExtra(type,verifiedType);
        startActivity(intent);
    }


    public void back(View view) {
        finish();
    }

    public void getVerified() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getCustomerAuth()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    this.verifiedType=s;
                    //已认证
                    String verified = "0";
                    //审核中
                    String underReview = "2";
                    //审核不通过
                    String authenFalse = "1";
                    Constant.verifiedBean.setState(s);
                    if (verified.equals(s)) {
                        verifiedLayout.setClickable(false);
                        tvBalanceleftAuthenticateFlag.setText(R.string.verified_successful);
                    } else if (underReview.equals(s)) {
                        verifiedLayout.setClickable(false);
                        tvBalanceleftAuthenticateFlag.setText(R.string.under_review);
                    } else if (authenFalse.equals(s)) {
                        //其他均为未认证
                        verifiedLayout.setClickable(true);
                        tvBalanceleftAuthenticateFlag.setText(R.string.not_verified);
                    } else {
                        verifiedLayout.setClickable(true);
                        tvBalanceleftAuthenticateFlag.setText(R.string.authen_false);
                    }

                }, this::handleApiError);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getVerified();
    }
}
