package com.zxjk.duoduo.ui.walletpage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetOverOrderResponse;

public class ExchangeOrderFailedActivity extends AppCompatActivity {

    private TextView tvConfirmSaleOrderId;
    private TextView tvConfirmSaleCoinType;
    private TextView tvConfirmSalePriceReference;
    private TextView tvConfirmSaleCount;
    private TextView tvConfirmSaleTotalPrice;
    private TextView tvConfirmSalePayType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_order_failed);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvConfirmSaleOrderId = findViewById(R.id.tvConfirmSaleOrderId);
        tvConfirmSaleCoinType = findViewById(R.id.tvConfirmSaleCoinType);
        tvConfirmSalePriceReference = findViewById(R.id.tvConfirmSalePriceReference);
        tvConfirmSaleCount = findViewById(R.id.tvConfirmSaleCount);
        tvConfirmSaleTotalPrice = findViewById(R.id.tvConfirmSaleTotalPrice);
        tvConfirmSalePayType = findViewById(R.id.tvConfirmSalePayType);

        GetOverOrderResponse data = (GetOverOrderResponse) getIntent().getSerializableExtra("data");
        tvConfirmSaleOrderId.setText(TextUtils.isEmpty(data.getBothOrderId()) ? (TextUtils.isEmpty(data.getSellOrderId()) ?
                data.getBuyOrderId() : data.getSellOrderId()) : data.getBothOrderId());
        tvConfirmSaleCoinType.setText(data.getCurrency().equals("1") ? "HK" : "Other");
        tvConfirmSalePriceReference.setText(getIntent().getStringExtra("rate"));
        tvConfirmSaleCount.setText(data.getNumber());
        tvConfirmSaleTotalPrice.setText(data.getMoney());

        if (data.getPayType().equals("1")) {
            tvConfirmSalePayType.setText(R.string.wechat);
        } else if (data.getPayType().equals("2")) {
            tvConfirmSalePayType.setText(R.string.alipay);
        } else {
            tvConfirmSalePayType.setText(R.string.bankcard);
        }
    }


    public void shensu(View view) {
        Intent intent = new Intent(this, ShenSuActivity.class);
        intent.putExtra("data", getIntent().getSerializableExtra("data"));
        startActivity(intent);
    }
}
