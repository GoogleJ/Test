package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.PayInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.weight.TitleBar;
import com.zxjk.duoduo.weight.dialog.BaseAddTitleDialog;
import com.zxjk.duoduo.weight.dialog.SelectPopupWindow;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\23 0023 收款信息页面
 */
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

String pwd=null;
    SelectPopupWindow selectPopupWindow;

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
                inoutPsw();
                isTag="1";
                break;
            case R.id.alipy_btn:
                //跳转到支付宝信息页面
                inoutPsw();
                isTag="2";
                break;
            case R.id.bank_btn:
                //跳转到银行卡信息页面
                inoutPsw();
                isTag="3";
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

             }, throwable -> {
                 dialog = new BaseAddTitleDialog(BillingMessageActivity.this);
                 dialog.setOnClickListener(() -> {
                     dialog.dismiss();
                 });
                 dialog.show(throwable.getMessage());
             });
    }

    //打开输入密码的对话框
    public void inoutPsw() {
        selectPopupWindow = new SelectPopupWindow(this, this);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        selectPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);

    }

    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (complete) {
            pwd=psw;
            if (isTag.equals(wechat)){
                Intent intent = new Intent(this, ReceiptTypeActivity.class);
                intent.putExtra(type, wechat);
                intent.putExtra("payPwd", psw);
                startActivity(intent);
                updatePayInfo(wechat);
            }else if (isTag.equals(alipay)){
                Intent intent = new Intent(this, ReceiptTypeActivity.class);
                intent.putExtra(type, alipay);
                intent.putExtra("payPwd", psw);
                startActivity(intent);
                updatePayInfo(alipay);
            }else{
                Intent intent = new Intent(this, ReceiptTypeActivity.class);
                intent.putExtra(type, bank);
                intent.putExtra("payPwd", psw);
                startActivity(intent);
                updatePayInfo(bank);
            }
        }
    }
    public void getPayInfo(){
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getPayInfo()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<List<PayInfoResponse>>() {
                    @Override
                    public void accept(List<PayInfoResponse> payInfoResponses) throws Exception {
                        LogUtils.d("DEBUG",payInfoResponses.toString());
                        for (int i=0;i<payInfoResponses.size();i++){
                            if (payInfoResponses.get(i).getPayType().equals("1")){
                                wechatText.setText(getString(R.string.pay_type_update_successful));
                                wechatIcon.setVisibility(View.VISIBLE);
                            }else if (payInfoResponses.get(i).getPayType().equals("2")){
                                alipyText.setText(getString(R.string.pay_type_update_successful));
                                alipyIcon.setVisibility(View.VISIBLE);
                            }else if (payInfoResponses.get(i).getPayType().equals("3")){
                                bankText.setText(getString(R.string.pay_type_update_successful));
                                bankIcon.setVisibility(View.VISIBLE);
                            }

                            if (wechatText.getText().toString().equals(getString(R.string.pay_type_update_successful))
                            &&alipyText.getText().toString().equals(getString(R.string.pay_type_update_successful))
                            &&bankText.getText().toString().equals(getString(R.string.pay_type_update_successful))){
                                SPUtils.getInstance().put("successfulPayType",getString(R.string.pay_type_update_successful));
                            }
                        }
                    }
                },this::handleApiError);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPayInfo();
    }
}
