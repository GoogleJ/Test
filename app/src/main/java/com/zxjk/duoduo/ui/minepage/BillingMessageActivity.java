package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.TitleBar;
import com.zxjk.duoduo.ui.widget.dialog.BaseAddTitleDialog;
import com.zxjk.duoduo.ui.widget.dialog.SelectPopupWindow;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.MD5Utils;

@SuppressLint("CheckResult")
public class BillingMessageActivity extends BaseActivity implements View.OnClickListener, SelectPopupWindow.OnPopWindowClickListener {
    ConstraintLayout wechatBtn, alipyBtn, bankBtn;
    TextView wechatText, alipyText, bankText;
    ImageView wechatIcon, alipyIcon, bankIcon;

    TitleBar billingMessageTitle;
    String wechat = "1";
    String alipay = "2";
    String bank = "3";
    String type = "type";
    String isTag;
    BaseAddTitleDialog dialog;

    SelectPopupWindow selectPopupWindow;

    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_message);
        intent = new Intent(this, ReceiptTypeActivity.class);

        initView();
        getPayInfo();
    }

    private void initView() {

        wechatBtn = findViewById(R.id.wechat_btn);
        alipyBtn = findViewById(R.id.alipy_btn);
        bankBtn = findViewById(R.id.bank_btn);
        wechatText = findViewById(R.id.billing_message_wechat_type);
        alipyText = findViewById(R.id.billing_message_alipy_type);
        bankText = findViewById(R.id.billing_message_bank_type);
        wechatIcon = findViewById(R.id.billing_message_wechat_type_icon);
        alipyIcon = findViewById(R.id.billing_message_alipy_type_icon);
        bankIcon = findViewById(R.id.billing_message_bank_type_icon);
        wechatBtn.setOnClickListener(this);
        alipyBtn.setOnClickListener(this);
        bankBtn.setOnClickListener(this);


        billingMessageTitle = findViewById(R.id.billing_message_title);
        billingMessageTitle.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wechat_btn:
                //跳转到微信信息页面
                inoutPsw();
                isTag = "1";
                break;
            case R.id.alipy_btn:
                //跳转到支付宝信息页面
                inoutPsw();
                isTag = "2";
                break;
            case R.id.bank_btn:
                //跳转到银行卡信息页面
                inoutPsw();
                isTag = "3";
                break;
            default:
        }
    }

    public void updatePayInfo(String payType) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .updatePayInfo(payType)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> startActivityForResult(intent, 2000), throwable -> {
                    dialog = new BaseAddTitleDialog(BillingMessageActivity.this);
                    dialog.setOnClickListener(() -> dialog.dismiss());
                    dialog.show(throwable.getMessage());
                });
    }

    //打开输入密码的对话框
    public void inoutPsw() {
        KeyboardUtils.hideSoftInput(BillingMessageActivity.this);
        selectPopupWindow = new SelectPopupWindow(this, this);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        selectPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
    }

    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (!complete) {
            return;
        }
        ServiceFactory.getInstance().getBaseService(Api.class)
                .verifyPayPwd(MD5Utils.getMD5(psw))
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(BillingMessageActivity.this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    intent.putExtra("payPwd", psw);
                    if (isTag.equals(wechat)) {
                        intent.putExtra(type, isTag);
                        updatePayInfo(wechat);
                    } else if (isTag.equals(alipay)) {
                        intent.putExtra(type, alipay);
                        updatePayInfo(alipay);
                    } else {
                        intent.putExtra(type, bank);
                        updatePayInfo(bank);
                    }
                }, this::handleApiError);
    }

    public void getPayInfo() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getPayInfo()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(payInfoResponses -> {
                    for (int i = 0; i < payInfoResponses.size(); i++) {
                        if (payInfoResponses.get(i).getPayType().equals("1")) {
                            wechatText.setText(getString(R.string.pay_type_update_successful));
                            wechatIcon.setVisibility(View.VISIBLE);
                        } else if (payInfoResponses.get(i).getPayType().equals("2")) {
                            alipyText.setText(getString(R.string.pay_type_update_successful));
                            alipyIcon.setVisibility(View.VISIBLE);
                        } else if (payInfoResponses.get(i).getPayType().equals("3")) {
                            bankText.setText(getString(R.string.pay_type_update_successful));
                            bankIcon.setVisibility(View.VISIBLE);
                        }

                        if (wechatText.getText().toString().equals(getString(R.string.pay_type_update_successful))
                                && alipyText.getText().toString().equals(getString(R.string.pay_type_update_successful))
                                && bankText.getText().toString().equals(getString(R.string.pay_type_update_successful))) {
                            SPUtils.getInstance().put(Constant.currentUser.getId(), true);
                        }
                    }
                }, this::handleApiError);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == 1000 && requestCode == 2000) {
                if (data.getStringExtra("pay").equals("1")) {
                    wechatText.setText(getString(R.string.pay_type_update_successful));
                } else if (data.getStringExtra("pay").equals("2")) {
                    alipyText.setText(getString(R.string.pay_type_update_successful));
                } else {
                    bankText.setText(getString(R.string.pay_type_update_successful));
                }
                if ((wechatText.getText().toString().equals(getString(R.string.pay_type_update_successful)))
                        && (alipyText.getText().toString().equals(getString(R.string.pay_type_update_successful)))
                        && (bankText.getText().toString().equals(getString(R.string.pay_type_update_successful)))) {
                    SPUtils.getInstance().put(Constant.currentUser.getId(), true);
                }

            }
        }

    }
}
