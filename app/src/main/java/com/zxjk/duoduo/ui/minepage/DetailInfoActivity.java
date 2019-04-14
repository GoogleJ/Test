package com.zxjk.duoduo.ui.minepage;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.DetailListResposne;
import com.zxjk.duoduo.ui.base.BaseActivity;

import java.text.SimpleDateFormat;

public class DetailInfoActivity extends BaseActivity {

    private TextView tvMoney;
    private TextView tvTradeType;
    private TextView tvTradeTime;
    private TextView tvTradeNumber;
    private TextView tvTradeNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);

        tvMoney = findViewById(R.id.tvMoney);
        tvTradeType = findViewById(R.id.tvTradeType);
        tvTradeTime = findViewById(R.id.tvTradeTime);
        tvTradeNumber = findViewById(R.id.tvTradeNumber);
        tvTradeNote = findViewById(R.id.tvTradeNote);

        DetailListResposne data = (DetailListResposne) getIntent().getSerializableExtra("data");
        tvMoney.setText(data.getHk() + " HK");
        tvTradeType.setText(data.getSerialType());
        tvTradeTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.parseLong(data.getCreateTime())));
        tvTradeNumber.setText(data.getSerialNumber());
        tvTradeNote.setText(data.getSerialType());
    }

    public void back(View view) {
        finish();
    }
}
