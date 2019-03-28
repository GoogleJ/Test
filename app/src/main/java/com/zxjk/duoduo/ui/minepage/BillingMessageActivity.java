package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.weight.TitleBar;
import com.zxjk.duoduo.weight.dialog.BaseAddTitleDialog;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @author Administrator
 * @// TODO: 2019\3\23 0023 收款信息页面
 */
@SuppressLint("CheckResult")
public class BillingMessageActivity extends BaseActivity implements View.OnClickListener {
    ConstraintLayout wechatBtn, alipyBtn, bankBtn;
    TextView wechatText, alipyText, bankText;
    ImageView wechatIcon, alipyIcon, bankIcon;
    Intent intent;
    TitleBar billingMessageTitle;
    String wechat = "1";
    String alipay = "2";
    String bank = "3";
    String type = "type";
    BaseAddTitleDialog dialog;
    String pwds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_message);
        initView();
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
                intent = new Intent(this, ReceiptTypeActivity.class);
                intent.putExtra(type, wechat);
                startActivity(intent);
                updatePayInfo(wechat);

                break;
            case R.id.alipy_btn:
                //跳转到支付宝信息页面
                intent = new Intent(this, ReceiptTypeActivity.class);
                intent.putExtra(type, alipay);
                startActivity(intent);
                updatePayInfo(alipay);
                break;
            case R.id.bank_btn:
                //跳转到银行卡信息页面
                intent = new Intent(this, ReceiptTypeActivity.class);
                intent.putExtra(type, bank);
                startActivity(intent);
                updatePayInfo(bank);
                break;
            default:
                break;
        }
    }
    public void updatePayInfo(String payType) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .updatePayInfo(payType)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    LogUtils.d("DEBUG", "成功"+s);
                }, throwable -> {
                    dialog = new BaseAddTitleDialog(BillingMessageActivity.this);
                    dialog.setOnClickListener(() -> {
                        dialog.dismiss();
                    });
                    dialog.show(throwable.getMessage());
                });

    }
}
