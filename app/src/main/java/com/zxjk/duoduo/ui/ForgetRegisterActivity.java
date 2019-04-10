package com.zxjk.duoduo.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.CountryEntity;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.CountryCodeConstantsUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

import static com.zxjk.duoduo.utils.MD5Utils.getMD5;

/**
 * @author Administrator
 * 忘记密码
 */
public class ForgetRegisterActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_commit)
    Button btn_commit;

    @BindView(R.id.login_country)
    TextView login_country;

    @BindView(R.id.mobile_code)
    TextView mobile_code;

    @BindView(R.id.text_user_agreement)
    CheckBox user_agreement;

    @BindView(R.id.text_go_login)
    TextView go_login;

    @BindView(R.id.edit_mobile)
    EditText edit_mobile;

    @BindView(R.id.edit_mobile_code)
    EditText edit_mobile_code;

    @BindView(R.id.edit_password)
    EditText edit_password;

    private String mobile;
    private String password;
    private String code;

    public static void start(AppCompatActivity activity) {
        Intent intent = new Intent(activity, ForgetRegisterActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_register);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_commit, R.id.text_user_agreement, R.id.text_go_login, R.id.mobile_code, R.id.login_country})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                mobile = edit_mobile.getText().toString();
                password = edit_password.getText().toString();
                code = edit_mobile_code.getText().toString();
                if (TextUtils.isEmpty(mobile) || !RegexUtils.isMobileExact(mobile)) {
                    ToastUtils.showShort(getString(R.string.edit_mobile_tip));
                    return;
                }

                if (TextUtils.isEmpty(code) || code.length() != 6) {
                    ToastUtils.showShort(getString(R.string.edit_code_tip));
                    return;
                }

                if (TextUtils.isEmpty(password) || 5 >= password.length() || password.length() >= 14) {
                    ToastUtils.showShort(getString(R.string.edit_password_reg));
                    return;
                }
                forgetPwd(mobile, password, code);
                break;
            case R.id.text_user_agreement:
                break;
            case R.id.text_go_login:
                LoginActivity.start(this);
                break;
            case R.id.mobile_code:
                String countryCode = login_country.getText().toString().trim().substring(1);
                mobile = edit_mobile.getText().toString();
                password = edit_password.getText().toString();
                code = edit_mobile_code.getText().toString();
                if (!TextUtils.isEmpty(mobile) && RegexUtils.isMobileExact(mobile)) {
                    registerCode(countryCode + "-" + mobile);
                    return;
                }
                ToastUtils.showShort(getString(R.string.edit_mobile_tip));
                break;
            case R.id.login_country:
                CountrySelectActivity.start(this, CountryCodeConstantsUtils.REQUESTCODE_COUNTRY_SELECT);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CountryCodeConstantsUtils.REQUESTCODE_COUNTRY_SELECT && resultCode == Activity.RESULT_OK && data != null) {
            CountryEntity countryEntity = (CountryEntity) data.getSerializableExtra("data");
            login_country.setText(" +" + (countryEntity != null ? countryEntity.countryCode : "86"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("CheckResult")
    public void registerCode(String phone) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getCode(phone, "0")
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(s -> {
                    countDownTimer.start();
                    ToastUtils.showShort(getString(R.string.code_label));
                    btn_commit.setEnabled(false);
                }, this::handleApiError);

    }

    public void forgetPwd(String phone, String pwd, String code) {

        ServiceFactory.getInstance().getBaseService(Api.class)
                .forgetPwd(phone, getMD5(pwd), code)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> finish(), throwable -> handleApiError(throwable));
    }

    private CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            long time = millisUntilFinished / 1000;
            mobile_code.setText(time + "秒后重新获取");
            mobile_code.setClickable(false);
        }

        @Override
        public void onFinish() {
            mobile_code.setText("重新获取验证码");
            mobile_code.setClickable(true);
        }
    };
}
