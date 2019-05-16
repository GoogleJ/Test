package com.zxjk.duoduo.ui.minepage;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * *********************
 * Administrator
 * *********************
 * 2019/5/16
 * *********************
 * 添加我的方式
 * *********************
 */
public class AddMyWayActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_way);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        tvTitle.setText(getString(R.string.m_add_my_way_title_bar));
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }
}
