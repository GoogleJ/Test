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
    private TextView tvChangePhoneCountryCode;
    private String phoneReciveVerify;

    CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            long time = millisUntilFinished / 1000;
            tvChangePhoneGetVerify.setText(time + getString(R.string.regain_after_seconds));
            tvChangePhoneGetVerify.setClickable(false);
        }

        @Override
        public void onFinish() {
            tvChangePhoneGetVerify.setText(getString(R.string.regain_verification_code));
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
        tvChangePhoneCountryCode = findViewById(R.id.tvChangePhoneCountryCode);
        tvChangePhoneGetVerify.setOnClickListener(this);

        tvChangePhoneCountryCode.setText("+" + Constant.HEAD_LOCATION);
    }

    //完成
    public void submit(View view) {
        String phone = etChangePhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort(getString(R.string.please_enter_phone_number));
            return;
        }

        String verifyCode = etChangePhoneVerify.getText().toString();
        if (TextUtils.isEmpty(verifyCode)) {
            ToastUtils.showShort(getString(R.string.please_enter_verification_code));
            return;
        }

        if (!RegexUtils.isMobileExact(phone)) {
            ToastUtils.showShort(getString(R.string.please_enter_a_valid_phone_number));
            return;
        }

        if (!phone.equals(phoneReciveVerify)) {
            ToastUtils.showShort(getString(R.string.verification_code_error));
            return;
        }

        doChangePhone(verifyCode);
    }

    private void doChangePhone(String verifyCode) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .updateMobile(phoneReciveVerify, verifyCode)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ChangePhoneActivity.this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    Constant.currentUser.setMobile(phoneReciveVerify);
                    ToastUtils.showShort(getString(R.string.successfully_modified));
                    finish();
                }, this::handleApiError);
    }

    @Override
    public void onClick(View v) {
        String phone = etChangePhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort(getString(R.string.please_enter_phone_number));
            return;
        }
        if (RegexUtils.isMobileExact(phone)) {
            phoneReciveVerify = phone;
            getVerifyCode();
            tvChangePhoneGetVerify.setClickable(false);
            return;
        }
        ToastUtils.showShort(getString(R.string.please_enter_a_valid_phone_number));
    }

    private void getVerifyCode() {
        timer.start();
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getCode(phoneReciveVerify, "0")
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ChangePhoneActivity.this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> ToastUtils.showShort(getString(R.string.the_verification_code_has_been_sent_please_check)), t -> {
                    handleApiError(t);
                    timer.cancel();
                    timer.onFinish();
                });
    }

    public void back(View view) {
        finish();
    }
}
