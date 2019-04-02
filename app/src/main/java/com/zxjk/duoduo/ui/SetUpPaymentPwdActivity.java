package com.zxjk.duoduo.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;
import com.ziyeyouhu.library.KeyboardTouchListener;
import com.ziyeyouhu.library.KeyboardUtil;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.MD5Utils;
import com.zxjk.duoduo.weight.PayPsdInputView;
import com.zxjk.duoduo.weight.TitleBar;
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
    KeyboardUtil keyboardUtil;

    LinearLayout rootView;
    ScrollView scrollView;
    TextView m_set_payment_pwd_label;

    String oldPwd;
    String newPwd;
    String newPwdTwo;

    private void initMoveKeyBoard() {
        keyboardUtil = new KeyboardUtil(this, rootView, scrollView);
        keyboardUtil.setOtherEdittext(payPsdInputView);
        // monitor the KeyBarod state
        keyboardUtil.setKeyBoardStateChangeListener(new KeyBoardStateListener());
        // monitor the finish or next Key
        keyboardUtil.setInputOverListener(new inputOverListener());
        payPsdInputView.setOnTouchListener(new KeyboardTouchListener(keyboardUtil, KeyboardUtil.INPUTTYPE_NUM_X, -1));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_payment_pwd);
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
        titleBar.getLeftImageView().setOnClickListener(v -> finish());

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


                if (TextUtils.isEmpty(oldPwd)) {
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
                    newPwdTwo = "";
                    newPwd = "";
                    m_set_payment_pwd_label.setText(R.string.please_set_paypass1);
                    return;
                }

                m_set_payment_pwd_label.setText(R.string.please_set_paypass2);
                commmitBtn.setVisibility(View.VISIBLE);
            }
        });
        commmitBtn.setOnClickListener(v -> updatePwd(oldPwd, newPwd, newPwdTwo));
    }


    public void updatePwd(String oldPwd, String newPwd, String newPwdTwo) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .updatePayPwd(MD5Utils.getMD5(oldPwd), MD5Utils.getMD5(newPwd), MD5Utils.getMD5(newPwdTwo))
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    ToastUtils.showShort(R.string.modifysuccess);
                    finish();
                }, this::handleApiError);
    }

    class KeyBoardStateListener implements KeyboardUtil.KeyBoardStateChangeListener {

        @Override
        public void KeyBoardStateChange(int state, EditText editText) {
//            System.out.println("state" + state);
//            System.out.println("editText" + editText.getText().toString());
        }
    }

    class inputOverListener implements KeyboardUtil.InputFinishListener {

        @Override
        public void inputHasOver(int onclickType, EditText editText) {
//            System.out.println("onclickType" + onclickType);
//            System.out.println("editText" + editText.getText().toString());
        }
    }


}
