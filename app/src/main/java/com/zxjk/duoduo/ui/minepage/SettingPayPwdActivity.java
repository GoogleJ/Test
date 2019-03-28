package com.zxjk.duoduo.ui.minepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ziyeyouhu.library.KeyboardTouchListener;
import com.ziyeyouhu.library.KeyboardUtil;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.weight.PayPsdInputView;
import com.zxjk.duoduo.weight.TitleBar;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\28 0028 支付密码设置
 */
public class SettingPayPwdActivity extends BaseActivity {
    PayPsdInputView payPsdInputView;
    TitleBar titleBar;
    TextView commmitBtn;
    KeyboardUtil keyboardUtil;

    LinearLayout rootView;
    ScrollView scrollView;
    TextView m_set_payment_pwd_label;

    String oldPwd = "";
    String newPwd;
    String newPwdTwo;


    private void initMoveKeyBoard() {
        keyboardUtil = new KeyboardUtil(this, rootView, scrollView);
        keyboardUtil.setOtherEdittext(payPsdInputView);
        // monitor the KeyBarod state
        keyboardUtil.setKeyBoardStateChangeListener(new SettingPayPwdActivity.KeyBoardStateListener());
        // monitor the finish or next Key
        keyboardUtil.setInputOverListener(new SettingPayPwdActivity.inputOverListener());
        payPsdInputView.setOnTouchListener(new KeyboardTouchListener(keyboardUtil, KeyboardUtil.INPUTTYPE_NUM_X, -1));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pay_pwd);
        ButterKnife.bind(this);
        initUI();
        initMoveKeyBoard();

    }


    private void initUI() {
        m_set_payment_pwd_label = findViewById(R.id.m_set_payment_pwd_label);
        rootView = (LinearLayout) findViewById(R.id.root_view);
        scrollView = (ScrollView) findViewById(R.id.sv_main);
        titleBar = findViewById(R.id.m_set_payment_pwd_title_bar);
        payPsdInputView = findViewById(R.id.m_set_payment_pwd_edit);
        commmitBtn = findViewById(R.id.m_edit_information_btn);
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        payPsdInputView.setComparePassword(new PayPsdInputView.onPasswordListener() {

            @Override
            public void onDifference(String oldPsd, String newPsd) {
                // TODO: 2018/1/22  和上次输入的密码不一致  做相应的业务逻辑处理
                payPsdInputView.cleanPsd();
                ToastUtils.showShort("与上次密码不一致");


                oldPwd = oldPsd;
            }


            @Override
            public void onEqual(String psd) {
                // TODO: 2017/5/7 两次输入密码相同，那就去进行支付楼

                commmitBtn.setVisibility(View.VISIBLE);

                newPwd = psd;

                newPwdTwo = psd;
            }

            @Override
            public void inputFinished(String inputPsd) {
                // TODO: 2018/1/3 输完逻辑
                m_set_payment_pwd_label.setText("请再次输入支付密码");
                payPsdInputView.setComparePassword(inputPsd);
                payPsdInputView.setText("");

            }
        });
        commmitBtn.setOnClickListener(v -> {
            Intent intent = getIntent();
            String number = intent.getStringExtra("idCardEdit");
            String verifiedCodeEdit = intent.getStringExtra("verifiedCodeEdit");
            settingPayPwd(number, verifiedCodeEdit, newPwd, newPwdTwo);
        });

    }


    public void settingPayPwd(String number, String securityCode, String newPwd, String newPwdTwo) {
        LogUtils.d("Pasdsdfaw", newPwd, newPwdTwo);

        ServiceFactory.getInstance().getBaseService(Api.class)
                .fandPayPwd(number, securityCode, newPwd, newPwdTwo)
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        SettingPayPwdActivity.this.finish();
                        ToastUtils.showShort(SettingPayPwdActivity.this.getString(R.string.update_success));
                    }
                }, this::handleApiError);
    }

    class KeyBoardStateListener implements KeyboardUtil.KeyBoardStateChangeListener {

        @Override
        public void KeyBoardStateChange(int state, EditText editText) {

        }
    }

    class inputOverListener implements KeyboardUtil.InputFinishListener {

        @Override
        public void inputHasOver(int onclickType, EditText editText) {
        }
    }

}
