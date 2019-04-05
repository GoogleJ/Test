package com.zxjk.duoduo.ui.minepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

import androidx.annotation.Nullable;

/**
 * @author Administrator
 * @// TODO: 2019\3\27 0027 支付设置
 */
public class PaySettingActivity extends BaseActivity {

    TextView retrievePayPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_setting);
        retrievePayPwd = findViewById(R.id.retrieve_pay_pwd);

    }

    public void retrievePayPwd(View view) {
        if (!Constant.currentUser.getIsAuthentication().equals("0")) {
            ToastUtils.showShort(R.string.verifyfirstpls);
            return;
        }
        startActivity(new Intent(PaySettingActivity.this, RetrievePayPwdActivity.class));
    }

    /**
     * 修改支付密码
     *
     * @param view
     */
    public void updatePayPwd(View view) {
        startActivity(new Intent(PaySettingActivity.this, UpdatePayPwdActivity.class));
    }

}
