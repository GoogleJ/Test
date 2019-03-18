package com.zxjk.duoduo.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.CountryEntity;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CountryCodeConstantsUtils;


import org.w3c.dom.Text;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import io.reactivex.functions.Consumer;

/**
 * @author Mr.Chen
 * @// TODO: 2019\3\17 0017 注册
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.btn_register)
    Button btn_register;
    @BindView(R.id.login_country)
    TextView login_country;
    @BindView(R.id.register_code)
    TextView register_code;

    @BindView(R.id.text_user_agreement)
    CheckBox user_agreement;

    @BindView(R.id.text_go_login)
    TextView go_login;

    @BindView(R.id.edit_mobile)
    EditText edit_mobile;

    @BindView(R.id.edit_register_code)
    EditText edit_register_code;

    @BindView(R.id.edit_password)
    EditText edit_password;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }

    private boolean isAgreement;
    private String password;
    private String countryCode;
    private String mobile;
    private String code;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.login_country
            , R.id.register_code
            , R.id.text_go_login
            , R.id.btn_register
            , R.id.login_country_bottom})
    @Override
    public void onClick(View v) {
        int phoneLength = 11;
        String codes = "86";

        switch (v.getId()) {
            case R.id.login_country:
                CountrySelectActivity.start(this, CountryCodeConstantsUtils.REQUESTCODE_COUNTRY_SELECT);
                break;
            case R.id.login_country_bottom:
                CountrySelectActivity.start(this, CountryCodeConstantsUtils.REQUESTCODE_COUNTRY_SELECT);
                break;
            case R.id.register_code:

                String mobile = edit_mobile.getText().toString().trim();
                String country = login_country.getText().toString();
                countryCode = country.substring(1);
                if (!mobile.isEmpty() && !"".equals(mobile) && mobile.length() == phoneLength) {
                    registerCode(countryCode + "-" + mobile);
                    return;
                }
                ToastUtils.showShort(getString(R.string.edit_mobile_tip));
                break;
            case R.id.text_go_login:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.btn_register:
                mobile = edit_mobile.getText().toString();
                password = edit_password.getText().toString();
                String code = edit_register_code.getText().toString();


                if (!mobile.isEmpty() && !"".equals(mobile) && mobile.length() == phoneLength) {
                    ToastUtils.showShort(getString(R.string.edit_mobile_tip));
                    return;
                }
                if (password.isEmpty() || 6 >= password.length() || password.length() >= 14) {
                    ToastUtils.showShort(getString(R.string.edit_password_reg));
                    return;
                }
                if (code.isEmpty() || code.length() != 6) {
                    ToastUtils.showShort(getString(R.string.edit_code_tip));
                    return;
                }
                if (!isAgreement) {
                    ToastUtils.showShort(getString(R.string.edit_agreement_tip));
                    return;
                }
                register(mobile, edit_register_code.getText().toString(), edit_password.toString());

                break;
            default:
                break;
        }
    }


    @OnCheckedChanged({R.id.text_user_agreement})
    public void agreement(CheckBox view, boolean isChecked) {
        Log.e("TestActivity", "view: " + isChecked);
        isAgreement = isChecked;

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CountryCodeConstantsUtils.REQUESTCODE_COUNTRY_SELECT && resultCode == Activity.RESULT_OK && data != null) {
            CountryEntity countryEntity = (CountryEntity) data.getSerializableExtra("data");
            login_country.setText(" +" + (countryEntity != null ? countryEntity.countryCode : "86"));
        } else {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void register(String phone, String code, String pwd) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .register(phone, code, String.valueOf(EncryptUtils.encryptMD5ToString(pwd)).toLowerCase())
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    LogUtils.d(s);
                    ToastUtils.showShort(s);
                    finish();
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.d(throwable.getMessage() + "");
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void registerCode(String phone) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getCode(phone)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        ToastUtils.showShort(getString(R.string.code_label));
                        countDownTimer.start();

                        LogUtils.d(s);
//                        ToastUtils.showShort(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //handleApiError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                        LogUtils.d("ssssss0", throwable.getMessage());

                    }
                });

    }

    private CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            long time = millisUntilFinished / 1000;
            register_code.setText(time + "秒后重新获取");
            register_code.setClickable(false);
        }

        @Override
        public void onFinish() {
            register_code.setText("重新获取验证码");
            register_code.setClickable(true);
        }
    };


}
