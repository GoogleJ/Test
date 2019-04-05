package com.zxjk.duoduo.ui.grouppage;

import android.os.Bundle;
import android.view.View;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.weight.TitleBar;

import androidx.annotation.Nullable;


/**
 * @author Administrator
 * @// TODO: 2019\3\29 0029 关于群组详情的实现
 */
public class ChatInformationActivity extends BaseActivity {
    TitleBar titleBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_information);
        titleBar=findViewById(R.id.m_fragment_chat_information_title_bar);
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
