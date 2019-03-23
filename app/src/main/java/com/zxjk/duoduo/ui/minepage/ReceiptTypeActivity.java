package com.zxjk.duoduo.ui.minepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.weight.TitleBar;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @author Administrator
 * @// TODO: 2019\3\23 0023 获取支付类型的字符
 */
public class ReceiptTypeActivity extends BaseActivity {
    TitleBar receiptTypeTitle;
    ConstraintLayout nickName,realName,accountIdCard;
    TextView receiptTypeName, receiptTypeCard,receiptTypePaymentName;
    TextView receiptTypeRealName,receiptTypeRealCardName,receiptTypePayment;
    ImageView receiptTypeGo,receiptTypeCardGo,receiptTypePaymentGo;
    //接受的类型
    int type;
    int wechat=0;
    int alipay=1;

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
    }

    private void initView() {
        receiptTypeTitle=findViewById(R.id.receipt_type_title);
        nickName=findViewById(R.id.nick_name);
        realName=findViewById(R.id.nick_name);
        accountIdCard=findViewById(R.id.account_id_card);
        receiptTypeName=findViewById(R.id.receipt_type_name);
        receiptTypeCard=findViewById(R.id.receipt_type_card);
        receiptTypePaymentName=findViewById(R.id.receipt_type_payment_name);
        receiptTypeRealName=findViewById(R.id.receipt_type_real_name);
        receiptTypeRealCardName=findViewById(R.id.receipt_type_real_card_name);
        receiptTypePayment=findViewById(R.id.receipt_type_payment);
        receiptTypeGo=findViewById(R.id.receipt_type_go);
        receiptTypeCardGo=findViewById(R.id.receipt_type_card_go);
        receiptTypePaymentGo=findViewById(R.id.receipt_type_payment_go);
    }
    private void initData(){
        Intent intent=getIntent();
        int types=intent.getIntExtra("type",type);
        if (types==wechat){
            //微信信息，提交按钮已隐藏
            receiptTypeTitle.setTitle(getString(R.string.wechat_info));
            receiptTypeName.setText(R.string.nick);
            receiptTypeCard.setText(R.string.receipt_type_real_name);
            receiptTypePaymentName.setText(R.string.collection_code);
            receiptTypeRealName.setVisibility(View.GONE);
            receiptTypeRealCardName.setText(R.string.user_name);
            receiptTypePayment.setText(R.string.not_uploaded);
            receiptTypeCardGo.setVisibility(View.GONE);
        }else if (types==alipay){
            //支付宝信息，提交按钮已隐藏
            receiptTypeTitle.setTitle(getString(R.string.alipy_info));
            receiptTypeName.setText(R.string.account_id);
            receiptTypeCard.setText(R.string.receipt_type_real_name);
            receiptTypePaymentName.setText(R.string.collection_code);
            receiptTypeRealName.setVisibility(View.GONE);
            receiptTypeRealCardName.setText(R.string.user_name);
            receiptTypePayment.setText(R.string.not_uploaded);
            receiptTypeCardGo.setVisibility(View.GONE);
        }else{
            //银行卡信息，提交按钮已隐藏
            receiptTypeTitle.setTitle(getString(R.string.bank_info));
            receiptTypeName.setText(R.string.account_name);
            receiptTypeCard.setText(R.string.bank_id_card);
            receiptTypePaymentName.setText(R.string.bank);
            receiptTypeRealName. setText(R.string.user_name);
           receiptTypeRealCardName.setVisibility(View.GONE);
           receiptTypePayment.setVisibility(View.GONE);
            receiptTypeGo.setVisibility(View.GONE);
        }
    }
}
