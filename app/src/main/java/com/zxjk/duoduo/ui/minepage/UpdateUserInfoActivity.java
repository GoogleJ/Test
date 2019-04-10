package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;

import io.reactivex.functions.Consumer;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;

/**
 * @author Administrator
 * @// TODO: 2019\3\25 0025 综合修改个人信息界面
 */
@SuppressLint("CheckResult")
public class UpdateUserInfoActivity extends BaseActivity {

    private EditText etChangeSign;
    private TextView tvChangeSign;
    private TextView tvUpdateInfoTitle;

    private static final int TYPE_SIGN = 1;
    private static final int TYPE_NICK = 2;
    private static final int TYPE_EMAIL = 3;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        etChangeSign = findViewById(R.id.etChangeSign);
        tvChangeSign = findViewById(R.id.tvChangeSign);
        tvUpdateInfoTitle = findViewById(R.id.tvUpdateInfoTitle);

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

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        if (type == TYPE_SIGN) {
            tvUpdateInfoTitle.setText(R.string.sign);
            etChangeSign.setHint(R.string.hint_sign);
        } else if (type == TYPE_NICK) {
            tvUpdateInfoTitle.setText(R.string.nick);
            etChangeSign.setHint(R.string.hint_nick);
        } else if (type == TYPE_EMAIL) {
            tvUpdateInfoTitle.setText(R.string.email);
            etChangeSign.setHint(R.string.hint_email);
            etChangeSign.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }
    }

    //提交
    public void submit(View view) {
        String sign = etChangeSign.getText().toString().trim();
        if (sign.length() == 0) {
            ToastUtils.showShort(R.string.input_empty);
            return;
        }
        LoginResponse update = new LoginResponse(Constant.userId);
        switch (type) {
            case TYPE_EMAIL:
                if (!RegexUtils.isEmail(sign)) {
                    ToastUtils.showShort(R.string.notaemail);
                    return;
                }
                update.setEmail(sign);
                break;
            case TYPE_NICK:
                update.setNick(sign);
                break;
            case TYPE_SIGN:
                update.setSignature(sign);
                break;
            default:
        }
        ServiceFactory.getInstance().getBaseService(Api.class)
                .updateUserInfo(GsonUtils.toJson(update))
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(response -> {
                    switch (type) {
                        case TYPE_EMAIL:
                            Constant.currentUser.setEmail(sign);
                            break;
                        case TYPE_NICK:
                            Constant.currentUser.setNick(sign);
                            break;
                        case TYPE_SIGN:
                            Constant.currentUser.setSignature(sign);
                            break;
                        default:
                    }
                    UpdateUserInfoActivity.this.finish();
                }, this::handleApiError);
    }

    public void back(View view) {
        finish();
    }

}
