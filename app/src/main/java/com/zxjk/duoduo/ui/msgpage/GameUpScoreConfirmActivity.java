package com.zxjk.duoduo.ui.msgpage;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

public class GameUpScoreConfirmActivity extends BaseActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_up_score_confirm);

        String hk = getIntent().getStringExtra("hk");
        tv = findViewById(R.id.tv);
        tv.setText(tv.getText() + hk + "HK");
    }

    public void commit(View view) {
        finish();
    }

    public void back(View view) {
        finish();
    }
}
