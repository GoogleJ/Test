package com.zxjk.duoduo.ui.minepage;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.TitleBar;

/**
 * @author Administrator
 */
public class AboutActivity extends BaseActivity {
    TitleBar titleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        titleBar = findViewById(R.id.about_title);
        initUI();
    }

    private void initUI() {
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 版本说明和版本号，现在没有版本，提示文字为此功能暂未实现
     *
     * @param view
     */
    public void aboutVersion(View view) {
        ToastUtils.showShort(getString(R.string.under_development));
    }
}
