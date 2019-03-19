package com.zxjk.duoduo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Application;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.CountryEntity;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CountryCodeConstantsUtils;
import com.zxjk.duoduo.weight.dialog.AccountFreezeDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

//import io.rong.imkit.RongIM;
//import io.rong.imlib.RongIMClient;

/**
 * 此处是登录界面及操作
 * @author Administrator
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.btn_login)
    Button btn_login;
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


    public static void start(AppCompatActivity activity){
        Intent intent=new Intent(activity,LoginActivity.class);
        activity.startActivity(intent);
    }
    AccountFreezeDialog dialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }




    @OnClick({R.id.login_country
            , R.id.text_forget_password
            , R.id.text_go_register
            , R.id.change_language
            , R.id.btn_login
            , R.id.login_country_bottom})
    @Override
    public void onClick(View v) {
        int phoneLength=11;
        switch (v.getId()) {
            case R.id.login_country:
                CountrySelectActivity.start(LoginActivity.this, CountryCodeConstantsUtils.REQUESTCODE_COUNTRY_SELECT);
                break;
            case R.id.login_country_bottom:
                CountrySelectActivity.start(LoginActivity.this, CountryCodeConstantsUtils.REQUESTCODE_COUNTRY_SELECT);
                break;
            case R.id.text_forget_password:
                ForgetRegisterActivity.start(LoginActivity.this);
                break;
            case R.id.text_go_register:
                RegisterActivity.start(this);
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

                break;
            case R.id.change_language:
                startActivity(new Intent(LoginActivity.this, ChangeLanguageActivity.class));
                finish();
                break;
            case R.id.btn_login:

                String mobile = edit_mobile.getText().toString().trim();
                String password = edit_password.getText().toString().trim();
                if (mobile.isEmpty()||password.isEmpty()){
                    ToastUtils.showShort(getString(R.string.edit_mobile_or_password_tip));
                    return;
                }
                if (mobile.isEmpty() && "".equals(mobile) && mobile.length() == phoneLength) {
                    ToastUtils.showShort(getString(R.string.edit_mobile_tip));
                    return;
                }
                if (password.isEmpty() || 5 >= password.length() || password.length() >= 14) {
                    ToastUtils.showShort(getString(R.string.edit_password_reg));
                    return;
                }

                login(mobile,password);
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
    Disposable subscribe;
    public void login(String phone,String pwd){
        subscribe = ServiceFactory.getInstance().getBaseService(Api.class)
                .login(phone, String.valueOf(EncryptUtils.encryptMD5ToString(pwd)).toLowerCase())
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse loginResponse) throws Exception {


                        String isDelete = "1";
                        String idFirstLogin = "0";
                        if (isDelete.equals(loginResponse.getIsDelete())) {
                            dialog = new AccountFreezeDialog(LoginActivity.this);
                            dialog.show();
                        } else if (idFirstLogin.equals(loginResponse.getIsFirstLogin())) {
                            LoginActivity.this.startActivity(new Intent(LoginActivity.this, EditPersonalInformationFragment.class));
                        } else {
                            Constant.token = loginResponse.getToken();
                            Constant.userId = loginResponse.getId();
                            Constant.currentUser = loginResponse;
                            LoginActivity.this.connect(loginResponse.getRongToken());
                            LoginActivity.this.startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        }

                    }
                }, throwable -> {
                    handleApiError(throwable);
                });

    }


    public void connect(String token) {

        if (getApplicationInfo().packageName.equals(Application.getCurProcessName(getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {

                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.d("GJSONSSSSS", "--onSuccess" + userid);
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.i("GJSONSSSS", "" + errorCode.getMessage());

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
        }
        super.onDestroy();
    }
}
