package com.zxjk.duoduo.ui.walletpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

public class ExchangeActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final int PAY_TYPE_WEXINN = 0;
    private static final int PAY_TYPE_ALI = 1;
    private static final int PAY_TYPE_BANK = 2;

    private LinearLayout llExchangeWexin;
    private LinearLayout llExchangeAliPay;
    private LinearLayout llExchangeBank;
    private RadioButton rbExchangeWexin;
    private RadioButton rbExchangeAliPay;
    private RadioButton rbExchangeBank;
    private TextView tvExchangeCoinType;
    private TextView tvExchangePrice;
    private TextView tvExchangeCount;
    private TextView tvExchangeTotal;
    private RadioGroup rgExchangeTop;
    private TextView tvExchangeLine1;
    private TextView tvExchangeLine3;
    private TextView tvExchangeLine4;
    private TextView tvExchangePayType;
    private Button btnExchangeConfirm;

    private int payType;
    private boolean buyOrSale = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        initViews();

    }

    private void initViews() {
        llExchangeWexin = findViewById(R.id.llExchangeWexin);
        llExchangeAliPay = findViewById(R.id.llExchangeAliPay);
        llExchangeBank = findViewById(R.id.llExchangeBank);
        rbExchangeWexin = findViewById(R.id.rbExchangeWexin);
        rbExchangeAliPay = findViewById(R.id.rbExchangeAlipay);
        rbExchangeBank = findViewById(R.id.rbExchangeBank);
        tvExchangeCoinType = findViewById(R.id.tvExchangeCoinType);
        tvExchangePrice = findViewById(R.id.tvExchangePrice);
        tvExchangeCount = findViewById(R.id.tvExchangeCount);
        tvExchangeTotal = findViewById(R.id.tvExchangeTotal);
        rgExchangeTop = findViewById(R.id.rgExchangeTop);
        tvExchangeLine1 = findViewById(R.id.tvExchangeLine1);
        tvExchangeLine3 = findViewById(R.id.tvExchangeLine3);
        tvExchangeLine4 = findViewById(R.id.tvExchangeLine4);
        tvExchangePayType = findViewById(R.id.tvExchangePayType);
        btnExchangeConfirm = findViewById(R.id.btnExchangeConfirm);

        llExchangeWexin.setOnClickListener(this);
        llExchangeAliPay.setOnClickListener(this);
        llExchangeBank.setOnClickListener(this);
        rgExchangeTop.check(R.id.rb1);
        rgExchangeTop.setOnCheckedChangeListener(this);
    }

    public void submit(View view) {

    }

    //选择数量
    public void chooseCount(View view) {

    }

    public void jump2List(View view) {
        startActivity(new Intent(this, ExchangeListActivity.class));
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        payType = v.getId() == R.id.llExchangeWexin ? PAY_TYPE_WEXINN : (v.getId() == R.id.llExchangeAliPay ? PAY_TYPE_ALI : PAY_TYPE_BANK);
        changeType(payType);
    }

    private void changeType(int type) {
        rbExchangeWexin.setChecked(false);
        rbExchangeAliPay.setChecked(false);
        rbExchangeBank.setChecked(false);
        switch (type) {
            case PAY_TYPE_WEXINN:
                rbExchangeWexin.setChecked(true);
                break;
            case PAY_TYPE_ALI:
                rbExchangeAliPay.setChecked(true);
                break;
            case PAY_TYPE_BANK:
                rbExchangeBank.setChecked(true);
                break;
            default:
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb2:
                buyOrSale = false;
                tvExchangeLine1.setText("出售币种");
                tvExchangeLine3.setText("出售数量");
                tvExchangeLine4.setText("出售总金额");
                tvExchangePayType.setText("收款方式");
                btnExchangeConfirm.setText("确认出售");
                break;
            case R.id.rb1:
                buyOrSale = true;
                tvExchangeLine1.setText("购买币种");
                tvExchangeLine3.setText("购买数量");
                tvExchangeLine4.setText("需支付金额");
                tvExchangePayType.setText("支付方式");
                btnExchangeConfirm.setText("确认购买");
                break;
        }
    }
}
