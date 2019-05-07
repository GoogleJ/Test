package com.zxjk.duoduo.ui.walletpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetOverOrderResponse;
import com.zxjk.duoduo.ui.ImgActivity;

import java.text.SimpleDateFormat;

public class ExchangeOrderSuccessActivity extends AppCompatActivity {

    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv7;
    private TextView tv8;
    private ImageView iv_wechat, iv_alipay, iv_bank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_order_detail);

        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv7 = findViewById(R.id.tv7);
        tv8 = findViewById(R.id.tv8);
        iv_wechat = findViewById(R.id.iv_wechat);
        iv_alipay = findViewById(R.id.iv_alipay);
        iv_bank = findViewById(R.id.iv_bank);
        GetOverOrderResponse data = (GetOverOrderResponse) getIntent().getSerializableExtra("data");

        tv2.setText(data.getMoney());
        tv3.setText(getIntent().getStringExtra("rate"));
        tv4.setText(data.getNumber());
        tv5.setText(data.getBothOrderId());

        if (data.getPayType().equals("1")) {
            iv_wechat.setVisibility(View.VISIBLE);
        } else if (data.getPayType().equals("2")) {
            iv_alipay.setVisibility(View.VISIBLE);
        } else {
            iv_bank.setVisibility(View.VISIBLE);
        }

        tv7.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.parseLong(data.getCreateTime())));
        tv8.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.parseLong(data.getCloseTime())));
    }

    public void showQR(View view) {
        GetOverOrderResponse data = (GetOverOrderResponse) getIntent().getSerializableExtra("data");
        Intent intent = new Intent(this, ImgActivity.class);
        intent.putExtra("url", data.getPicture());
        startActivity(intent);
    }

}
