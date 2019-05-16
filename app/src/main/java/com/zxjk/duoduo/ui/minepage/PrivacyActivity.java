package com.zxjk.duoduo.ui.minepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Administrator
 */
public class PrivacyActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        ButterKnife.bind(this);
        tvTitle.setText(getString(R.string.m_privacy_title_bar));
    }


    /**
     * 添加我的方式页面的跳转
     */
    public void addMyWay(View view) {
        startActivity(new Intent(this, AddMyWayActivity.class));

    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }
}
