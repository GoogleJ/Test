package com.zxjk.duoduo.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.MD5Utils;
import com.zxjk.duoduo.ui.widget.KeyboardPopupWindow;
import com.zxjk.duoduo.ui.widget.PayPsdInputView;
import com.zxjk.duoduo.ui.widget.TitleBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import butterknife.ButterKnife;

/**
 * @author Administrator
 * @// TODO: 2019\3\21 0021  设置支付密码
 */
@SuppressLint("CheckResult")
@RequiresApi(api = Build.VERSION_CODES.M)
public class SetUpPaymentPwdActivity extends BaseActivity {

    PayPsdInputView payPsdInputView;
    TitleBar titleBar;
    TextView commmitBtn;


    LinearLayout rootView;
    ScrollView scrollView;
    TextView m_set_payment_pwd_label;

    String oldPwd = "";
    String newPwd;
    String newPwdTwo;
    boolean firstLogin;
    private boolean isUiCreated = false;
    KeyboardPopupWindow popupWindow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_payment_pwd);
        ButterKnife.bind(this);
        firstLogin = getIntent().getBooleanExtra("firstLogin", false);

        if (!firstLogin) {
            m_set_payment_pwd_label.setText(R.string.inputoldpsd);
        }

        initUI();
    }

    private void initUI() {
        m_set_payment_pwd_label = findViewById(R.id.m_set_payment_pwd_label);
        rootView = (LinearLayout) findViewById(R.id.root_view);
        scrollView = (ScrollView) findViewById(R.id.sv_main);
        titleBar = findViewById(R.id.m_set_payment_pwd_title_bar);
        payPsdInputView = findViewById(R.id.m_set_payment_pwd_edit);
        commmitBtn = findViewById(R.id.m_edit_information_btn);
        titleBar.getLeftImageView().setOnClickListener(v -> finish());
        titleBar.getTitleView().setText(firstLogin ? R.string.set_pay_pwd : R.string.update_pay_pwd);

        payPsdInputView.setComparePassword(new PayPsdInputView.onPasswordListener() {

            @Override
            public void onDifference(String oldPsd, String newPsd) {
            }

            @Override
            public void onEqual(String psd) {
            }

            @Override
            public void inputFinished(String inputPsd) {
                // TODO: 2018/1/3 输完逻辑
                if (TextUtils.isEmpty(oldPwd) && !firstLogin) {
                    payPsdInputView.cleanPsd();
                    oldPwd = inputPsd;
                    m_set_payment_pwd_label.setText(R.string.please_set_paypass);
                    return;
                }
                if (TextUtils.isEmpty(newPwd)) {
                    payPsdInputView.cleanPsd();
                    newPwd = inputPsd;
                    m_set_payment_pwd_label.setText(R.string.please_set_paypass_twtice);
                    return;
                }
                if (TextUtils.isEmpty(newPwdTwo)) {
                    newPwdTwo = inputPsd;
                }

                if (!newPwd.equals(newPwdTwo)) {
                    ToastUtils.showShort(R.string.passnotsame);
                    payPsdInputView.cleanPsd();
                    newPwdTwo = "";
                    newPwd = "";
                    m_set_payment_pwd_label.setText(R.string.please_set_paypass1);
                    return;
                }

                m_set_payment_pwd_label.setText(R.string.please_set_paypass2);
                commmitBtn.setVisibility(View.VISIBLE);
            }
        });
        commmitBtn.setOnClickListener(v -> updatePwd("", MD5Utils.getMD5(newPwd), MD5Utils.getMD5(newPwdTwo)));
        popupWindow = new KeyboardPopupWindow(this, getWindow().getDecorView(), payPsdInputView, false);
        payPsdInputView.setOnClickListener(v -> {
            if (popupWindow != null) {
                popupWindow.show();
            }
        });
        payPsdInputView.setOnFocusChangeListener((v, hasFocus) -> {
            if (popupWindow != null && isUiCreated) {
                popupWindow.refreshKeyboardOutSideTouchable(!hasFocus);
            }
            //隐藏系统软键盘
            if (hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(payPsdInputView.getWindowToken(), 0);
            }
        });
    }

    public void updatePwd(String oldPwd, String newPwd, String newPwdTwo) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .updatePayPwd(oldPwd, MD5Utils.getMD5(newPwd), MD5Utils.getMD5(newPwdTwo))
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    if (firstLogin) {
                        startActivity(new Intent(this, HomeActivity.class));
                        finish();
                        ToastUtils.showShort(R.string.setsuccess);
                    } else {
                        ToastUtils.showShort(R.string.successfully_modified);
                    }
                    finish();
                }, this::handleApiError);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        isUiCreated = true;
    }

    @Override
    protected void onDestroy() {
        if (popupWindow != null) {
            popupWindow.releaseResources();
        }
        super.onDestroy();
    }
}
