package com.zxjk.duoduo.ui.walletpage;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetOverOrderResponse;

import androidx.appcompat.app.AppCompatActivity;

public class WaitForPayOverActivity extends AppCompatActivity {

    private TextView tvConfirmSaleOrderId;
    private TextView tvConfirmSaleCoinType;
    private TextView tvConfirmSalePriceReference;
    private TextView tvConfirmSaleCount;
    private TextView tvConfirmSaleTotalPrice;
    private TextView tvConfirmSalePayType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_pay_over);

        tvConfirmSaleOrderId = findViewById(R.id.tvConfirmSaleOrderId);
        tvConfirmSaleCoinType = findViewById(R.id.tvConfirmSaleCoinType);
        tvConfirmSalePriceReference = findViewById(R.id.tvConfirmSalePriceReference);
        tvConfirmSaleCount = findViewById(R.id.tvConfirmSaleCount);
        tvConfirmSaleTotalPrice = findViewById(R.id.tvConfirmSaleTotalPrice);
        tvConfirmSalePayType = findViewById(R.id.tvConfirmSalePayType);

        GetOverOrderResponse data = (GetOverOrderResponse) getIntent().getSerializableExtra("data");
        tvConfirmSaleOrderId.setText(data.getBothOrderId());
        tvConfirmSaleCoinType.setText(data.getCurrency().equals("1") ? "HK" : "Other");
        tvConfirmSalePriceReference.setText(getIntent().getStringExtra("rate"));
        tvConfirmSaleCount.setText(data.getNumber());
        tvConfirmSaleTotalPrice.setText(data.getMoney());

        StringBuilder sb = new StringBuilder();
        if (data.getPayType().contains(",")) {
            String[] split = data.getPayType().split(",");
            for (String str : split) {
                switch (str) {
                    case "1":
                        sb.append("微信" + ",");
                        break;
                    case "2":
                        sb.append("支付宝" + ",");
                        break;
                    case "3":
                        sb.append("银行卡" + ",");
                        break;
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        } else {
            switch (data.getPayType()) {
                case "1":
                    sb.append("微信" + ",");
                    break;
                case "2":
                    sb.append("支付宝" + ",");
                    break;
                case "3":
                    sb.append("银行卡" + ",");
                    break;
            }
        }
        tvConfirmSalePayType.setText(sb.toString());
    }

    public void back(View view) {
        finish();
    }
}
