package com.zxjk.duoduo.ui.minepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

/**
 * 帮助中心
 */
public class HelpActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.help_center));

        findViewById(R.id.rl_back).setOnClickListener(v -> finish());

    }

    public void jump(View view) {
        finish();
        startActivity(new Intent(this, OnlineServiceActivity.class));
    }


}
