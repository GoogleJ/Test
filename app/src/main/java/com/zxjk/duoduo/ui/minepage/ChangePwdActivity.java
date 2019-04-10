package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.LoginActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.MD5Utils;

/**
 * @author Administrator
 * @// TODO: 2019\3\25 0025 修改密码
 */
@SuppressLint("CheckResult")
public class ChangePwdActivity extends BaseActivity {

    private EditText etChangePwdOrigin;
    private EditText etChangePwdNew;
    private EditText etChangePwdConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        etChangePwdOrigin = findViewById(R.id.etChangePwdOrigin);
        etChangePwdNew = findViewById(R.id.etChangePwdNew);
        etChangePwdConfirm = findViewById(R.id.etChangePwdConfirm);
    }

    private String newPass;

    public void submit(View view) {
        String s = etChangePwdOrigin.getText().toString().trim();
        String s1 = etChangePwdNew.getText().toString().trim();
        String s2 = etChangePwdConfirm.getText().toString().trim();

        if (!verifyPwd(s)) {
            ToastUtils.showShort(R.string.verify_pwd1);
            return;
        }

        if (!verifyPwd(s1)) {
            ToastUtils.showShort(R.string.verify_pwd2);
            return;
        }

        if (!verifyPwd(s2)) {
            ToastUtils.showShort(R.string.verify_pwd2);
            return;
        }

        if (!s1.equals(s2)) {
            ToastUtils.showShort(R.string.verify_pwd3);
            return;
        }
        newPass = s1;
        doChangePwd(MD5Utils.getMD5(s), MD5Utils.getMD5(s1));
    }

    private void doChangePwd(String oldPwd, String newPwd) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .updatePwd(oldPwd, newPwd, newPwd)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ChangePwdActivity.this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(response -> {
                    Constant.clear();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    ToastUtils.showShort(R.string.update_success);
                    finish();
                }, this::handleApiError);
    }

    public void back(View view) {
        finish();
    }

    private boolean verifyPwd(String pwd) {
        return !TextUtils.isEmpty(pwd) && (pwd.length() >= 6);
    }
}
