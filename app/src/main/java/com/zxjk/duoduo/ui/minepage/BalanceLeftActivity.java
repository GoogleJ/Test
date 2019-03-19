package com.zxjk.duoduo.ui.minepage;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

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
