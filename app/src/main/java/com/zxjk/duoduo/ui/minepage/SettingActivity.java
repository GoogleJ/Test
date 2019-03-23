package com.zxjk.duoduo.ui.minepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

/**
 * 这里是关于设置的activity
 *
 * @author Administrator
 */
public class SettingActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    //账户
    public void jump2Account(View view) {
        startActivity(new Intent(this, AccountActivity.class));
    }

    /**
     * 意见反馈
     * @param view
     */
    public void feedback(View view){
        startActivity(new Intent(this,FeedbackActivity.class));
    }
    /**
     * 账号切换
     * @param view
     */
    public void accountSwitch(View view){
        startActivity(new Intent(this, AccountSwitchActivity.class));
    }
    /**
     * 关于嘟嘟界面
     * @param view
     */
    public void aboutDuoDuo(View view){
        startActivity(new Intent(this, AboutActivity.class));
    }

    /**
     * 隐私界面的跳转
     * @param view
     */
    public void privacy(View view){
        startActivity(new Intent(this,PrivacyActivity.class));
    }

    public void back(View view) {
        finish();
    }
}
