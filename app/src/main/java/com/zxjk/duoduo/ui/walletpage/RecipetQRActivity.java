package com.zxjk.duoduo.ui.walletpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.minepage.BalanceLeftActivity;
import com.zxjk.duoduo.ui.minepage.scanuri.Action1;
import com.zxjk.duoduo.ui.minepage.scanuri.BaseUri;
import com.zxjk.duoduo.ui.widget.dialog.EditTextDialog;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.utils.MoneyValueFilter;
import com.zxjk.duoduo.utils.QRCodeEncoder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RecipetQRActivity extends BaseActivity {

    private ImageView ivRecipetImg;
    private ImageView ivCodeLogo;
    private TextView tvMoney;
    private EditTextDialog editTextDialog;

    private BaseUri uri;
    private String uri2Code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipet_qr);
        ivRecipetImg = findViewById(R.id.ivRecipetImg);
        ivCodeLogo = findViewById(R.id.ivCodeLogo);
        tvMoney = findViewById(R.id.tvMoney);

        editTextDialog = new EditTextDialog();

        initUri();

        getCodeBitmap();
    }

    private void getCodeBitmap() {
        GlideUtil.loadCornerImg(ivCodeLogo,Constant.currentUser.getHeadPortrait(),1);
        Glide.with(this).asBitmap().centerCrop()
                .load(Constant.currentUser.getHeadPortrait())
                .into(new SimpleTarget<Bitmap>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Observable.create((ObservableOnSubscribe<Bitmap>) emitter -> {
                            Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(uri2Code, BGAQRCodeUtil.dp2px(RecipetQRActivity.this, 300),
                                    Color.BLACK);
                            emitter.onNext(bitmap);
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(b -> ivRecipetImg.setImageBitmap(b), t -> {
                                });
                    }
                });
    }

    private void initUri() {
        uri = new BaseUri("action1");
        Action1 action1 = new Action1();
        action1.money = "0.00";
        uri.data = new Gson().toJson(action1);
        uri2Code = new Gson().toJson(uri);
    }

    // 设置金额
    public void setMoney(View view) {
        editTextDialog.show(this, "请输入金额", "设置", "取消");
        editTextDialog.getEdit().setFilters(new InputFilter[]{new MoneyValueFilter()});
        editTextDialog.getYes().setOnClickListener(v -> {
            tvMoney.setText(editTextDialog.getText() + " HK");
            editTextDialog.hideDialog();
            Action1 action1 = new Action1();
            action1.money = editTextDialog.getText();
            uri.data = new Gson().toJson(action1);
            uri2Code = new Gson().toJson(uri);
            getCodeBitmap();
        });
    }

    // 进入我的余额
    public void enterMyBalance(View view) {
        startActivity(new Intent(this, BalanceLeftActivity.class));
    }

    public void back(View view) {
        finish();
    }
}
