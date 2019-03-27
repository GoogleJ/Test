package com.zxjk.duoduo.ui.minepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.SetUpPaymentPwdActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.weight.dialog.BaseAddTitleDialog;

import androidx.annotation.Nullable;

/**
 * @author Administrator
 * @// TODO: 2019\3\27 0027 支付设置
 */
public class PaySettingActivity extends BaseActivity {

    String verifiedType;
    String type = "verifiedType";
    TextView retrievePayPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_setting);
        retrievePayPwd = findViewById(R.id.retrieve_pay_pwd);
        initData();
    }

    private void initData() {
        retrievePayPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                verifiedType = intent.getStringExtra(type);
                //已认证
                String verified = "0";
                if (verified.equals(verifiedType)) {
                    startActivity(new Intent(PaySettingActivity.this, RetrievePayPwdActivity.class));
                    retrievePayPwd.setClickable(true);
                    return;
                } else {
                    retrievePayPwd.setClickable(false);
                    ToastUtils.showShort(getString(R.string.please_first_verified));
                    return;
                }

            }
        });
    }

    /**
     * 修改支付密码
     *
     * @param view
     */
    public void updatePayPwd(View view) {
        startActivity(new Intent(PaySettingActivity.this, SetUpPaymentPwdActivity.class));

    }


}
