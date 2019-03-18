package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;

@SuppressLint("CheckResult")
public class ChangeSignActivity extends BaseActivity {

    private EditText etChangeSign;
    private TextView tvChangeSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_sign);

        etChangeSign = findViewById(R.id.etChangeSign);
        tvChangeSign = findViewById(R.id.tvChangeSign);

        etChangeSign.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().length();
                tvChangeSign.setText("" + (20 - length));
            }
        });
    }

    //提交
    public void submit(View view) {
        String sign = etChangeSign.getText().toString();
        if (sign.length() == 0) {
            ToastUtils.showShort("请输入内容");
            return;
        }
        LoginResponse update = new LoginResponse(Constant.userId);
        update.setSignature(sign);
        ServiceFactory.getInstance().getBaseService(Api.class)
                .updateUserInfo(GsonUtils.toJson(update))
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(response -> {
                    Constant.currentUser.setSignature(sign);
                }, this::handleApiError);
    }

    public void back(View view) {
        finish();
    }

}
