package com.zxjk.duoduo.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.CountryEntity;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.service.RegisterBlockWalletService;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.zxjk.duoduo.utils.MD5Utils.getMD5;

@SuppressLint("CheckResult")
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

    public static void start(AppCompatActivity activity) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }

    private boolean isAgreement;
    private String password;
    private String countryCode;


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
        switch (v.getId()) {
            case R.id.login_country:
                CountrySelectActivity.start(this, 200);
                break;
            case R.id.login_country_bottom:
                CountrySelectActivity.start(this, 200);
                break;
            case R.id.register_code:
                String mobile = edit_mobile.getText().toString().trim();
                String country = login_country.getText().toString().trim();
                countryCode = country.substring(1);
                if (!TextUtils.isEmpty(mobile) && RegexUtils.isMobileExact(mobile)) {
                    registerCode(countryCode + "-" + mobile);
                    return;
                }
                ToastUtils.showShort(getString(R.string.edit_mobile_tip));
                break;
            case R.id.text_go_login:
                finish();
                break;
            case R.id.btn_register:
                mobile = edit_mobile.getText().toString().trim();
                password = edit_password.getText().toString().trim();
                String code = edit_register_code.getText().toString().trim();

                if (TextUtils.isEmpty(mobile) || !RegexUtils.isMobileExact(mobile)) {
                    ToastUtils.showShort(getString(R.string.edit_mobile_tip));
                    return;
                }
                if (TextUtils.isEmpty(code) || code.length() != 6) {
                    ToastUtils.showShort(getString(R.string.edit_code_tip));
                    return;
                }
                if (TextUtils.isEmpty(password) || 5 >= password.length()) {
                    ToastUtils.showShort(getString(R.string.edit_password_reg));
                    return;
                }
                if (!isAgreement) {
                    ToastUtils.showShort(getString(R.string.edit_agreement_tip));
                    return;
                }
                register(mobile, code, password);

                break;
            default:
                break;
        }
    }


    @OnCheckedChanged({R.id.text_user_agreement})
    public void agreement(CheckBox view, boolean isChecked) {
        isAgreement = isChecked;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == Activity.RESULT_OK && data != null) {
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
                .register(phone, code, getMD5(pwd))
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(loginResponse -> {
                    Constant.currentUser = loginResponse;
                    Constant.userId = loginResponse.getId();
                    startService(new Intent(this, RegisterBlockWalletService.class));
                    finish();
                    btn_register.setEnabled(false);
                }, this::handleApiError);
    }

    public void registerCode(String phone) {
        countDownTimer.start();
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getCode(phone, "0")
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> ToastUtils.showShort(getString(R.string.code_label)), t -> {
                    countDownTimer.cancel();
                    countDownTimer.onFinish();
                    handleApiError(t);
                });

    }

    private CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            long time = millisUntilFinished / 1000;
            register_code.setText(time + getString(R.string.regain_after_seconds));
            register_code.setClickable(false);
        }

        @Override
        public void onFinish() {
            register_code.setText(getString(R.string.regain_verification_code));
            register_code.setClickable(true);
        }
    };


}
