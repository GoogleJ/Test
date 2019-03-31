package com.zxjk.duoduo.ui.walletpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetTransferEthResponse;
import com.zxjk.duoduo.ui.walletpage.adapter.ETHOrdersAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChooseCoinActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_coin);

    }

    public void back(View view) {
        finish();
    }
}
