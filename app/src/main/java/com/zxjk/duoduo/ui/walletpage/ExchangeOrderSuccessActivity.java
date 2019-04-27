package com.zxjk.duoduo.ui.walletpage;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetOverOrderResponse;

import java.text.SimpleDateFormat;

public class ExchangeOrderSuccessActivity extends AppCompatActivity {

    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private TextView tv8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_order_detail);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);
        tv7 = findViewById(R.id.tv7);
        tv8 = findViewById(R.id.tv8);

        GetOverOrderResponse data = (GetOverOrderResponse) getIntent().getSerializableExtra("data");

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
        tv8.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.parseLong(data.getCloseTime())));
    }


}
