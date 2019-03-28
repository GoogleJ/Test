package com.zxjk.duoduo.ui.minepage;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @author Administrator
 * @// TODO: 2019\3\27 0027 找回支付密码身份证验证
 */
public class RetrievePayPwdActivity extends BaseActivity implements View.OnClickListener {
    ImageView idCardImage;
    ImageView phoneCodeImage;
    ConstraintLayout idCardLayout;
    ConstraintLayout phoneCodeLayout;
    EditText idCardEdit;
    TextView phone;
    TextView textGetCode;
    EditText verifiedCodeEdit;
    TextView commitBtn;
    int length = 10;
    int codeLength = 5;
    boolean isTrue = true;

    //短信验证
    String messagfeCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_payment_pwd);
        initView();
    }

    private void initView() {
        idCardImage = findViewById(R.id.id_card_image);
        phoneCodeImage = findViewById(R.id.phone_code_image);
        idCardLayout = findViewById(R.id.id_card_layout);
        phoneCodeLayout = findViewById(R.id.phone_code_layout);
        idCardEdit = findViewById(R.id.id_card_edit);
        phone = findViewById(R.id.phone);
        textGetCode = findViewById(R.id.text_get_code);
        verifiedCodeEdit = findViewById(R.id.verified_code_edit);
        commitBtn = findViewById(R.id.commit_btn);
        commitBtn.setOnClickListener(this);
        textGetCode.setOnClickListener(this);
        phone.setText(Constant.currentUser.getMobile());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.commit_btn:
                if (isTrue) {
                    if (idCardEdit.getText().toString().isEmpty() && idCardEdit.getText().length() > length) {
                        ToastUtils.showShort(getString(R.string.please_input_id_card));
                        return;
                    }
                    idCardLayout.setVisibility(View.GONE);
                    phoneCodeLayout.setVisibility(View.VISIBLE);
                    phoneCodeImage.setImageResource(R.drawable.icon_phone_code);
                    isTrue = false;
                } else {
                    if (verifiedCodeEdit.getText().toString().isEmpty() && verifiedCodeEdit.getText().length() < codeLength) {
                        ToastUtils.showShort(getString(R.string.edit_register_code));
                        return;
                    }
                    Intent intent = new Intent(this, SettingPayPwdActivity.class);
                    intent.putExtra("idCardEdit", idCardEdit.getText().toString());
                    intent.putExtra("verifiedCodeEdit", verifiedCodeEdit.getText().toString());
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.text_get_code:
                String type = "1";
                registerCode(phone.getText().toString(), type);
                break;
            default:
                break;
        }

    }

    public void registerCode(String phone, String type) {

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getCode(phone, type)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    ToastUtils.showShort(getString(R.string.code_label));
                    LogUtils.d(s);
                    messagfeCode = s;
                    countDownTimer.start();
                }, this::handleApiError);

    }

    private CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            long time = millisUntilFinished / 1000;
            textGetCode.setText(time + getString(R.string.tiem_new_code));
            textGetCode.setClickable(false);
        }

        @Override
        public void onFinish() {
            textGetCode.setText(getString(R.string.newCode));
            textGetCode.setClickable(true);
        }
    };
}
