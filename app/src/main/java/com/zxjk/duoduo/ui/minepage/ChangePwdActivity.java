package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
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
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;

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

    public void submit(View view) {
        String s = etChangePwdOrigin.getText().toString().trim();
        String s1 = etChangePwdNew.getText().toString().trim();
        String s2 = etChangePwdConfirm.getText().toString().trim();

        if (!verifyPwd(s) || !s.equals(Constant.currentUser.getPassword())) {
            ToastUtils.showShort("请输入正确原密码");
            return;
        }

        if (!verifyPwd(s1)) {
            ToastUtils.showShort("请输入正确新密码");
            return;
        }

        if (!verifyPwd(s2)) {
            ToastUtils.showShort("请输入正确新密码");
            return;
        }

        if (!s1.equals(s2)) {
            ToastUtils.showShort("两次新密码输入不一致");
            return;
        }

        doChangePwd(s, s1);
    }

    private void doChangePwd(String oldPwd, String newPwd) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .updatePwd(oldPwd, newPwd, newPwd)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ChangePwdActivity.this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(response -> {
                    ToastUtils.showShort("修改成功");
                    finish();
                }, this::handleApiError);
    }

    private boolean verifyPwd(String pwd) {
        return !TextUtils.isEmpty(pwd) && (pwd.length() >= 6);
    }
}
