package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.AllGroupActivity;

public class WithdrawalSuccessActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal_success);
        intiView();
    }

    @SuppressLint("SetTextI18n")
    private void intiView() {
        TextView tv_title = findViewById(R.id.tv_title);
        String money = getIntent().getStringExtra("money");
        TextView tv_money = findViewById(R.id.tv_money);
        tv_money.setText(money + "HK");
        tv_title.setText(getString(R.string.accomplish));
        findViewById(R.id.rl_back).setOnClickListener(v -> {
            Intent intent = new Intent(this, AllGroupActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        findViewById(R.id.tv_success).setOnClickListener(v -> {
            Intent intent = new Intent(WithdrawalSuccessActivity.this, AllGroupActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        });
    }
}
