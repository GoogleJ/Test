package com.zxjk.duoduo.ui.minepage;

import android.os.Bundle;
import android.view.View;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

/**
 * 这里是关于设置的activity
 * @author Administrator
 */
public class SettingActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void back(View view) {
        finish();
    }
}
