package com.zxjk.duoduo.ui.msgpage;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

public class JinDuoBaoActiviity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jin_duo_bao);

        findViewById(R.id.rl_back).setOnClickListener(v -> finish());

        ((TextView) findViewById(R.id.tv_title)).setText(R.string.jinduobao);



    }
}
