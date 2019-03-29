package com.zxjk.duoduo.ui.walletpage;

import android.os.Bundle;
import android.view.View;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

public class HuaZhuanActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hua_zhuan);
    }

    public void back(View view) {
        finish();
    }

    public void submit(View view) {

    }

    public void cancel(View view) {

    }
}
