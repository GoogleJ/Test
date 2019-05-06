package com.zxjk.duoduo.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Application;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.CountryEntity;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.dialog.AccountFreezeDialog;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.MMKVUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

import static com.zxjk.duoduo.utils.MD5Utils.getMD5;

/**
 * 此处是登录界面及操作
 *
 * @author Administrator
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.login_country)
    TextView login_country;
    @BindView(R.id.text_forget_password)
    TextView forget_password;
    @BindView(R.id.text_go_register)
    TextView go_register;
    @BindView(R.id.edit_mobile)
    EditText edit_mobile;
    @BindView(R.id.edit_password)
    EditText edit_password;
    String EXTRA_DATA = "data";
    @BindView(R.id.tv_login)
    TextView tvLogin;


    public static void start(AppCompatActivity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    AccountFreezeDialog dialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        edit_mobile.setText(SPUtils.getInstance().getString("mobile"));


//        login("18202987805", "123456");

    }


    @OnClick({R.id.login_country
            , R.id.text_forget_password
            , R.id.text_go_register
            , R.id.change_language
            , R.id.tv_login
            , R.id.login_country_bottom})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_country:
                CountrySelectActivity.start(LoginActivity.this, 200);
                break;
            case R.id.login_country_bottom:
                CountrySelectActivity.start(LoginActivity.this, 200);
                break;
            case R.id.text_forget_password:
                ForgetRegisterActivity.start(LoginActivity.this);
                break;
            case R.id.text_go_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.change_language:
                startActivity(new Intent(LoginActivity.this, ChangeLanguageActivity.class));
                break;
            case R.id.tv_login:
                String mobile = edit_mobile.getText().toString().trim();
                String password = edit_password.getText().toString().trim();
                if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(password)) {
                    ToastUtils.showShort(getString(R.string.edit_mobile_or_password_tip));
                    return;
                }
                if (TextUtils.isEmpty(mobile) && "".equals(mobile)) {
                    ToastUtils.showShort(getString(R.string.edit_mobile_tip));
                    return;
                }
                if (TextUtils.isEmpty(password) || 5 >= password.length() || password.length() >= 14) {
                    ToastUtils.showShort(getString(R.string.edit_password_reg));
                    return;
                }
                login(mobile, password);
                SPUtils.getInstance().put("mobile", edit_mobile.getText().toString().trim());
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == Activity.RESULT_OK && data != null) {
            CountryEntity countryEntity = (CountryEntity) data.getSerializableExtra(EXTRA_DATA);
            login_country.setText(" +" + (countryEntity != null ? countryEntity.countryCode : "86"));
            if (countryEntity != null) {
                Constant.HEAD_LOCATION = countryEntity.countryCode;
            }
        }
    }

    @SuppressLint("CheckResult")
    public void login(String phone, String pwd) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .login(phone, getMD5(pwd))
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(loginResponse -> {
                    Constant.token = loginResponse.getToken();
                    Constant.userId = loginResponse.getId();
                    Constant.currentUser = loginResponse;
                    Constant.authentication = loginResponse.getIsAuthentication();

                    MMKVUtils.getInstance().enCode("isLogin", true);
                    MMKVUtils.getInstance().enCode("date1", TimeUtils.getNowMills());
                    MMKVUtils.getInstance().enCode("login", loginResponse);
                    MMKVUtils.getInstance().enCode("token", loginResponse.getToken());
                    MMKVUtils.getInstance().enCode("userId", loginResponse.getId());


                    if (loginResponse.getIsDelete().equals(Constant.FLAG_IS_DELETE)) {
                        dialog = new AccountFreezeDialog(LoginActivity.this);
                        dialog.show();
                    } else if (loginResponse.getIsFirstLogin().equals(Constant.FLAG_FIRSTLOGIN)) {
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, EditPersonalInformationFragment.class));
                    } else {
                        LoginActivity.this.connect(loginResponse.getRongToken());
                    }

                }, this::handleApiError);
    }

    // 连接融云
    private void connect(String token) {
        if (getApplicationInfo().packageName.equals(Application.getCurProcessName(getApplicationContext()))) {
            CommonUtils.initDialog(this).show();
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                @Override
                public void onTokenIncorrect() {
                    CommonUtils.destoryDialog();
                }

                @Override
                public void onSuccess(String userid) {

                    CommonUtils.destoryDialog();
                    UserInfo userInfo = new UserInfo(userid, Constant.currentUser.getNick(), Uri.parse(Constant.currentUser.getHeadPortrait()));
                    RongIM.getInstance().setCurrentUserInfo(userInfo);
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    tvLogin.setEnabled(false);
                    finish();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    CommonUtils.destoryDialog();
                }
            });
        }
    }
}
