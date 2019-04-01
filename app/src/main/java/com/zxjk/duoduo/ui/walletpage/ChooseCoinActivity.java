package com.zxjk.duoduo.ui.walletpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetTransferEthResponse;
import com.zxjk.duoduo.ui.minepage.VerifiedActivity;
import com.zxjk.duoduo.ui.walletpage.adapter.ETHOrdersAdapter;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class ChooseCoinActivity extends AppCompatActivity {

    private TextView tvChooseCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_coin);

        tvChooseCoin = findViewById(R.id.tvChooseCoin);

        String coin = getIntent().getStringExtra("coin");

        if (tvChooseCoin.getText().equals(coin)) {
            tvChooseCoin.setText("ETH");
            Drawable drawable = getDrawable(R.drawable.ic_blockwallet_eth);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicWidth());
            tvChooseCoin.setCompoundDrawables(null, null, drawable, null);
        }
    }

    public void enterETHOrders(View view) {
        Intent intent = new Intent();
        intent.putExtra("coin", tvChooseCoin.getText().toString());
        setResult(2, intent);
        finish();
    }

    public void back(View view) {
        finish();
    }
}
