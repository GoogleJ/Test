package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.AddPayInfoBean;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.TakePopWindow;
import com.zxjk.duoduo.ui.widget.TitleBar;
import com.zxjk.duoduo.ui.widget.dialog.PaymentTypeDialog;
import com.zxjk.duoduo.utils.AesUtil;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.MD5Utils;
import com.zxjk.duoduo.utils.OssUtils;
import com.zxjk.duoduo.utils.TakePicUtil;

import java.io.File;
import java.util.Collections;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import static com.zxjk.duoduo.ui.EditPersonalInformationFragment.REQUEST_ALBUM;
import static com.zxjk.duoduo.ui.EditPersonalInformationFragment.REQUEST_TAKE;

/**
 * @author Administrator
 */
@SuppressLint("CheckResult")
public class ReceiptTypeActivity extends BaseActivity implements View.OnClickListener, TakePopWindow.OnItemClickListener {
    TitleBar receiptTypeTitle;
    ConstraintLayout nickName, realName, accountIdCard;
    TextView receiptTypeName, receiptTypeCard, receiptTypePaymentName;
    TextView receiptTypeRealName, receiptTypeRealCardName, receiptTypePayment;
    ImageView receiptTypeGo, receiptTypeCardGo, receiptTypePaymentGo;
    TextView commitBtn;
    private TakePopWindow selectPicPopWindow;
    //接受的类型
    String wechat = "1";
    String alipay = "2";
    String bank = "3";
    PaymentTypeDialog dialog;
    String types;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_type);
        initView();
        initData();
        initClick();
    }

    private void initClick() {
        receiptTypeTitle.getLeftImageView().setOnClickListener(v -> finish());
        nickName.setOnClickListener(this);
        realName.setOnClickListener(this);
        accountIdCard.setOnClickListener(this);
        commitBtn.setOnClickListener(this);
    }

    private void initView() {
        selectPicPopWindow = new TakePopWindow(ReceiptTypeActivity.this);
        selectPicPopWindow.setOnItemClickListener(this);
        receiptTypeTitle = findViewById(R.id.receipt_type_title);
        nickName = findViewById(R.id.nick_name);
        realName = findViewById(R.id.real_name);
        accountIdCard = findViewById(R.id.account_id_card);
        receiptTypeName = findViewById(R.id.receipt_type_name);
        receiptTypeCard = findViewById(R.id.receipt_type_card);
        receiptTypePaymentName = findViewById(R.id.receipt_type_payment_name);
        receiptTypeRealName = findViewById(R.id.receipt_type_real_name);
        receiptTypeRealCardName = findViewById(R.id.receipt_type_real_card_name);
        receiptTypePayment = findViewById(R.id.receipt_type_payment);
        receiptTypeGo = findViewById(R.id.receipt_type_go);
        receiptTypeCardGo = findViewById(R.id.receipt_type_card_go);
        receiptTypePaymentGo = findViewById(R.id.receipt_type_payment_go);
        commitBtn = findViewById(R.id.commit_btn);
    }

    private void initData() {
        Intent intent = getIntent();
        types = intent.getStringExtra("type");
        if (wechat.equals(types)) {
            //微信信息，提交按钮已隐藏
            receiptTypeTitle.setTitle(getString(R.string.wechat_info));
            receiptTypeName.setText(R.string.nick);
            receiptTypeCard.setText(R.string.receipt_type_real_name);
            receiptTypePaymentName.setText(R.string.collection_code);
            receiptTypeRealCardName.setText(Constant.currentUser.getRealname());
            receiptTypePayment.setText(R.string.not_uploaded);
            receiptTypeCardGo.setVisibility(View.GONE);
        } else if (alipay.equals(types)) {
            //支付宝信息，提交按钮已隐藏
            receiptTypeTitle.setTitle(getString(R.string.alipy_info));
            receiptTypeName.setText(R.string.account_id);
            receiptTypeCard.setText(R.string.receipt_type_real_name);
            receiptTypePaymentName.setText(R.string.collection_code);
            receiptTypeRealCardName.setText(Constant.currentUser.getRealname());
            receiptTypePayment.setText(R.string.not_uploaded);
            receiptTypeCardGo.setVisibility(View.GONE);
        } else {
            //银行卡信息，提交按钮已隐藏
            receiptTypeTitle.setTitle(getString(R.string.bank_info));
            receiptTypeName.setText(R.string.account_name);
            receiptTypeCard.setText(R.string.bank_id_card);
            receiptTypePaymentName.setText(R.string.bank);
            receiptTypeRealName.setText(Constant.currentUser.getRealname());
            receiptTypeGo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nick_name:
                if (wechat.equals(types)) {
                    dialog = new PaymentTypeDialog(this);
                    dialog.setOnClickListener(editContent -> {
                        receiptTypeRealName.setText(editContent);
                        receiptTypeRealName.setVisibility(View.VISIBLE);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        dialog.dismiss();
                    });
                    dialog.show(getString(R.string.wechat_nick), getString(R.string.hint_nick), wechat);
                    return;
                } else if (alipay.equals(types)) {
                    dialog = new PaymentTypeDialog(this);
                    dialog.setOnClickListener(editContent -> {
                        receiptTypeRealName.setText(editContent);
                        receiptTypeRealName.setVisibility(View.VISIBLE);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        dialog.dismiss();
                    });
                    dialog.show(getString(R.string.alipay_number), getString(R.string.hint_alipay), alipay);
                    return;
                }
                break;
            case R.id.real_name:
                if (wechat.equals(types)) {
                    return;
                } else if (alipay.equals(types)) {
                    return;
                } else {
                    dialog = new PaymentTypeDialog(ReceiptTypeActivity.this);
                    dialog.setOnClickListener(editContent -> {
                        receiptTypeRealCardName.setText(editContent);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        dialog.dismiss();
                    });
                    dialog.show(getString(R.string.bankcard), getString(R.string.input_bank_number), bank);
                }
                break;
            case R.id.account_id_card:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (wechat.equals(types)) {
                    selectPicPopWindow.showAtLocation(this.findViewById(android.R.id.content),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    return;
                } else if (alipay.equals(types)) {
                    selectPicPopWindow.showAtLocation(this.findViewById(android.R.id.content),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    return;
                } else {
                    dialog = new PaymentTypeDialog(ReceiptTypeActivity.this);
                    dialog.setOnClickListener(editContent -> {
                        receiptTypePayment.setText(editContent);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        dialog.dismiss();
                    });
                    dialog.show(getString(R.string.open_bank), getString(R.string.please_upload_selector_open_bank), "4");
                }
                break;
            case R.id.commit_btn:
                String pwd = getIntent().getStringExtra("payPwd");
                AddPayInfoBean addPayInfoBean = new AddPayInfoBean();
                if (wechat.equals(types)) {
                    addPayInfoBean.setPayType(wechat);
                    addPayInfoBean.setWechatNick(receiptTypeRealName.getText().toString());
                    addPayInfoBean.setPayPicture(url);
                    addPayInfoBean.setPayPwd(MD5Utils.getMD5(pwd));
                } else if (alipay.equals(types)) {
                    addPayInfoBean.setPayType(alipay);
                    addPayInfoBean.setZhifubaoNumber(receiptTypeRealName.getText().toString());
                    addPayInfoBean.setPayPicture(url);
                    addPayInfoBean.setPayPwd(MD5Utils.getMD5(pwd));
                } else {
                    addPayInfoBean.setPayType(bank);
                    addPayInfoBean.setPayNumber(receiptTypeRealCardName.getText().toString());
                    addPayInfoBean.setOpenBank(receiptTypePayment.getText().toString());
                    addPayInfoBean.setPayPwd(MD5Utils.getMD5(pwd));
                }
                addPayInfo(AesUtil.getInstance().encrypt(GsonUtils.toJson(addPayInfoBean)));
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String filePath = "";
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE:
                    filePath = TakePicUtil.file.getAbsolutePath();
                    break;
                case REQUEST_ALBUM:
                    filePath = TakePicUtil.getPath(this, data.getData());
                    break;
            }
        }
        if (!TextUtils.isEmpty(filePath)) {
            zipFile(Collections.singletonList(filePath), files -> {
                File file = files.get(0);
                OssUtils.uploadFile(file.getAbsolutePath(), url -> {
                    this.url = url;
                    receiptTypePayment.setText(R.string.uploaded);
                });
            });
        }
    }

    public void addPayInfo(String data) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .addPayInfo(data)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    commitBtn.setEnabled(false);
                    ToastUtils.showShort(getString(R.string.pay_type_successful));
                    finish();
                }, this::handleApiError);
    }

    @Override
    public void setOnItemClick(View v) {
        selectPicPopWindow.dismiss();
        switch (v.getId()) {
            case R.id.tvCamera:
                TakePicUtil.takePicture(this, REQUEST_TAKE);
                break;
            case R.id.tvPhoto:
                TakePicUtil.albumPhoto(this, REQUEST_ALBUM);
                break;
        }
    }
}
