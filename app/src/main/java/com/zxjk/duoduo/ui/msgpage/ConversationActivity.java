package com.zxjk.duoduo.ui.msgpage;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.weight.TitleBar;

public class ConversationActivity extends FragmentActivity {

    TitleBar titleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        titleBar=findViewById(R.id.conversation_title);
        initView();
    }

    private void initView() {
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setTitle("用户");
        titleBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
