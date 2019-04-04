package com.zxjk.duoduo.ui.msgpage;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

public class TransferSuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_success);

        TextView tvTransferSuccessFriend = findViewById(R.id.tvTransferSuccessFriend);
        TextView tvTransferSuccessMoney = findViewById(R.id.tvTransferSuccessMoney);
        tvTransferSuccessFriend.setText("待好友" + getIntent().getStringExtra("name") + "确认收款");
        tvTransferSuccessMoney.setText(getIntent().getStringExtra("money"));
    }

    public void back(View view) {
        finish();
    }

    public void submit(View view) {
        finish();
    }
}
