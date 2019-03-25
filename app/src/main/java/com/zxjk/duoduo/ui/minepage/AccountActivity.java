package com.zxjk.duoduo.ui.minepage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

/**
 * @author Administrator
 * @// TODO: 2019\3\25 0025 关于账户切换页面
 */
public class AccountActivity extends BaseActivity {

    private TextView tvAccountDuDuNum;
    private TextView tvAccountPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        tvAccountDuDuNum = findViewById(R.id.tvAccountDuDuNum);
        tvAccountPhone = findViewById(R.id.tvAccountPhone);

        tvAccountDuDuNum.setText(TextUtils.isEmpty(Constant.currentUser.getDuoduoId()) ? "暂未设置" : Constant.currentUser.getDuoduoId());
        tvAccountPhone.setText(Constant.currentUser.getMobile());
    }

    /**
     * 修改手机号页面跳转
     * @param view
     */
    public void changePhone(View view) {
        startActivity(new Intent(this, ChangePhoneActivity.class));
    }

    /**
     * 修改密码页面跳转
     * @param view
     */
    public void changePass(View view) {
        startActivity(new Intent(this, ChangePwdActivity.class));
    }

    public void back(View view) {
        finish();
    }
}
