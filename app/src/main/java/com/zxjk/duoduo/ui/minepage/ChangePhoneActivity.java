package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;

@SuppressLint("CheckResult")
public class ChangePhoneActivity extends BaseActivity implements View.OnClickListener {

    private EditText etChangePhone;
    private EditText etChangePhoneVerify;
    private TextView tvChangePhoneGetVerify;
    private String phoneReciveVerify;

    CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            long time = millisUntilFinished / 1000;
            tvChangePhoneGetVerify.setText(time + "秒后重新获取");
        }

        @Override
        public void onFinish() {
            tvChangePhoneGetVerify.setText("重新获取验证码");
            tvChangePhoneGetVerify.setClickable(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);

        etChangePhone = findViewById(R.id.etChangePhone);
        etChangePhoneVerify = findViewById(R.id.etChangePhoneVerify);
        tvChangePhoneGetVerify = findViewById(R.id.tvChangePhoneGetVerify);
        tvChangePhoneGetVerify.setOnClickListener(this);
    }

    //完成
    public void submit(View view) {
        String phone = etChangePhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort("请输入手机号");
            return;
        }

        String verifyCode = etChangePhoneVerify.getText().toString();
        if (TextUtils.isEmpty(verifyCode)) {
            ToastUtils.showShort("请输入验证码");
            return;
        }

        if (!RegexUtils.isMobileExact(phone)) {
            ToastUtils.showShort("请输入正确手机号");
            return;
        }

        if (!phone.equals(phoneReciveVerify)) {
            ToastUtils.showShort("验证码错误");
            return;
        }

        doChangePhone(verifyCode);
    }

    private void doChangePhone(String verifyCode) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .updateMobile(Constant.HEAD_LOCATION + phoneReciveVerify, verifyCode)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ChangePhoneActivity.this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    Constant.currentUser.setMobile(phoneReciveVerify);
                    ToastUtils.showShort("修改成功");
                    finish();
                }, this::handleApiError);
    }

    @Override
    public void onClick(View v) {
        String phone = etChangePhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort("请输入手机号");
            return;
        }
        if (RegexUtils.isMobileExact(phone)) {
            phoneReciveVerify = phone;
            getVerifyCode();
            tvChangePhoneGetVerify.setClickable(false);
            timer.start();
            return;
        }
        ToastUtils.showShort("请输入正确手机号");
    }

    private void getVerifyCode() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getCode(Constant.HEAD_LOCATION + phoneReciveVerify)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ChangePhoneActivity.this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> ToastUtils.showShort("验证码已发送，请查收"), t -> {
                    handleApiError(t);
                    timer.cancel();
                    timer.onFinish();
                });
    }
}
