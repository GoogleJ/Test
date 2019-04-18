package com.zxjk.duoduo.ui.minepage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.TitleBar;

import androidx.annotation.Nullable;

/**
 * @author Administrator
 */
public class PaySettingActivity extends BaseActivity {

    TextView retrievePayPwd;
    TitleBar title_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_setting);
        retrievePayPwd = findViewById(R.id.retrieve_pay_pwd);
        title_bar=findViewById(R.id.title_bar);
        title_bar.getLeftImageView().setOnClickListener(v -> finish());

    }

    public void retrievePayPwd(View view) {
        if (TextUtils.isEmpty(Constant.currentUser.getIsAuthentication())) {
            ToastUtils.showShort(R.string.verifyfirstpls);
            return;
        }
        if (Constant.currentUser.getIsAuthentication().equals("1")) {
            ToastUtils.showShort(R.string.verifyfirstpls);
            return;
        }
        if (Constant.currentUser.getIsAuthentication().equals("2")) {
            ToastUtils.showShort(R.string.waitAuthentication);
            return;
        }
        startActivity(new Intent(this, RetrievePayPwdActivity.class));
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
