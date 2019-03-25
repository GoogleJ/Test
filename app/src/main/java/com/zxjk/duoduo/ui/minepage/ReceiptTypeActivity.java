package com.zxjk.duoduo.ui.minepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.TakePopWindow;
import com.zxjk.duoduo.utils.AesUtil;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.utils.OssUtils;
import com.zxjk.duoduo.utils.TakePicUtil;
import com.zxjk.duoduo.weight.TitleBar;
import com.zxjk.duoduo.weight.dialog.PaymentTypeDialog;

import java.io.File;
import java.util.Collections;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import io.reactivex.functions.Consumer;

import static com.zxjk.duoduo.ui.EditPersonalInformationFragment.REQUEST_ALBUM;
import static com.zxjk.duoduo.ui.EditPersonalInformationFragment.REQUEST_TAKE;
import static com.zxjk.duoduo.utils.PermissionUtils.cameraPremissions;
import static com.zxjk.duoduo.utils.PermissionUtils.verifyStoragePermissions;

/**
 * @author Administrator
 * @// TODO: 2019\3\23 0023 获取支付类型的字符
 */
public class ReceiptTypeActivity extends BaseActivity implements View.OnClickListener,TakePopWindow.OnItemClickListener {
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
    String receiptTypePayments = "已上传";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_type);

        initView();
        initData();
        initClick();

    }

    private void initClick() {
        receiptTypeTitle.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        realName = findViewById(R.id.nick_name);
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
            receiptTypeRealName.setVisibility(View.GONE);
            receiptTypeRealCardName.setText(R.string.user_name);
            receiptTypePayment.setText(R.string.not_uploaded);
            receiptTypeCardGo.setVisibility(View.GONE);
        } else if (alipay.equals(types)) {
            //支付宝信息，提交按钮已隐藏
            receiptTypeTitle.setTitle(getString(R.string.alipy_info));
            receiptTypeName.setText(R.string.account_id);
            receiptTypeCard.setText(R.string.receipt_type_real_name);
            receiptTypePaymentName.setText(R.string.collection_code);
            receiptTypeRealName.setVisibility(View.GONE);
            receiptTypeRealCardName.setText(R.string.user_name);
            receiptTypePayment.setText(R.string.not_uploaded);
            receiptTypeCardGo.setVisibility(View.GONE);
        } else {
            //银行卡信息，提交按钮已隐藏
            receiptTypeTitle.setTitle(getString(R.string.bank_info));
            receiptTypeName.setText(R.string.account_name);
            receiptTypeCard.setText(R.string.bank_id_card);
            receiptTypePaymentName.setText(R.string.bank);
            receiptTypeRealName.setText(R.string.user_name);
            receiptTypeRealCardName.setVisibility(View.GONE);
            receiptTypePayment.setVisibility(View.GONE);
            receiptTypeGo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nick_name:
                if (wechat.equals(types)) {
                    if (receiptTypePayments.equals(receiptTypePayment)) {
                        dialog = new PaymentTypeDialog(this);
                        dialog.setOnClickListener(editContent -> {
                            Constant.payInfoBean.setPayType(wechat);
                            Constant.payInfoBean.setWechatNick(editContent);
                            Constant.payInfoBean.setPayPicture(url);
                            addPayInfo(AesUtil.getInstance().encrypt(GsonUtils.toJson(Constant.payInfoBean)));
                            commitBtn.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        });
                        dialog.show(getString(R.string.wechat_nick), wechat);
                    } else {

                    }


                } else if (alipay.equals(types)) {
                    if (receiptTypePayments.equals(receiptTypePayment)) {
                        dialog = new PaymentTypeDialog(this);

                        dialog.setOnClickListener(editContent -> {
                            Constant.payInfoBean.setZhifubaoNumber(editContent);
                            Constant.payInfoBean.setPayType(alipay);
                            Constant.payInfoBean.setPayPicture(url);
                            addPayInfo(AesUtil.getInstance().encrypt(GsonUtils.toJson(Constant.payInfoBean)));
                            commitBtn.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        });
                        dialog.show(getString(R.string.alipay_number), alipay);
                    } else {
                        ToastUtils.showShort(R.string.please_upload_payment_code);
                    }

                } else {
                    if (receiptTypePayment.getText().length() != 0) {
                        dialog = new PaymentTypeDialog(this);

                        dialog.setOnClickListener(editContent -> {
                            Constant.payInfoBean.setPayType(bank);
                            Constant.payInfoBean.setPayNumber(editContent);
                            addPayInfo(AesUtil.getInstance().encrypt(GsonUtils.toJson(Constant.payInfoBean)));
                            commitBtn.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        });
                        dialog.show(getString(R.string.bank_real_name), bank);
                    } else {
                        ToastUtils.showShort(R.string.please_upload_selector_open_bank);
                    }
                }
                break;
            case R.id.real_name:
                break;
            case R.id.account_id_card:
                if (wechat.equals(types)) {

                    receiptTypePaymentName.setText(R.string.uploaded);
                        verifyStoragePermissions(this);
                        cameraPremissions(this);
                        selectPicPopWindow.showAtLocation(this.findViewById(android.R.id.content),
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);



                } else if (alipay.equals(types)) {

                    receiptTypePaymentName.setText(R.string.uploaded);
                    verifyStoragePermissions(this);
                    cameraPremissions(this);
                    selectPicPopWindow.showAtLocation(this.findViewById(android.R.id.content),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                } else {

                }
                break;
            case R.id.commit_btn:
                break;

            default:
                break;
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
                 this.url=url;


                });
            });
        }

    }

    public void addPayInfo(String data) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .addPayInfo(data)
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtils.d("DEBUG", "修改成功");
                    }
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
