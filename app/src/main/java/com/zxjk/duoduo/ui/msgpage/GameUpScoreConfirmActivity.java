package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

/**
 * author L
 * create at 2019/5/9
 * description: 上分成功
 */
public class GameUpScoreConfirmActivity extends BaseActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_up_score_confirm);
        initView();
    }

    private void initView() {
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.upscore));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        String hk = getIntent().getStringExtra("hk");
        TextView tv = findViewById(R.id.tv);
        tv.setText("成功上分 " + hk + "HK");
    }

    public void commit(View view) {
        finish();
    }

}
