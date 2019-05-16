package com.zxjk.duoduo.ui.minepage;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.DetailListResposne;
import com.zxjk.duoduo.ui.base.BaseActivity;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailInfoActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    private TextView tvMoney;
    private TextView tvTradeType;
    private TextView tvTradeTime;
    private TextView tvTradeNumber;
    private TextView tvTradeNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        ButterKnife.bind(this);
        tvTitle.setText(getString(R.string.shouruxiangqing));

        tvMoney = findViewById(R.id.tvMoney);
        tvTradeType = findViewById(R.id.tvTradeType);
        tvTradeTime = findViewById(R.id.tvTradeTime);
        tvTradeNumber = findViewById(R.id.tvTradeNumber);
        tvTradeNote = findViewById(R.id.tvTradeNote);

        DetailListResposne data = (DetailListResposne) getIntent().getSerializableExtra("data");
        tvMoney.setText(data.getHk() + " HK");
        tvTradeType.setText(getIntent().getStringExtra("type"));
        tvTradeTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.parseLong(data.getCreateTime())));
        tvTradeNumber.setText(data.getSerialNumber());
        if (!TextUtils.isEmpty(getIntent().getStringExtra("remarks"))) {
            tvTradeNote.setText(getIntent().getStringExtra("remarks"));
        } else {
            tvTradeNote.setText(getIntent().getStringExtra("type"));
        }
    }


    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }
}
