package com.zxjk.duoduo.ui.walletpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.minepage.BalanceLeftActivity;
import com.zxjk.duoduo.ui.msgpage.MyQrCodeActivity;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.utils.MoneyValueFilter;
import com.zxjk.duoduo.weight.dialog.EditTextDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RecipetQRActivity extends BaseActivity {

    private ImageView ivRecipetImg;
    private TextView tvMoney;
    private EditTextDialog editTextDialog;

    private Uri uri;
    private String uri2Code;

    private int codeWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipet_qr);
        ivRecipetImg = findViewById(R.id.ivRecipetImg);
        tvMoney = findViewById(R.id.tvMoney);

        editTextDialog = new EditTextDialog();

//        initCodeWidth();

        initUri();

        getCodeBitmap();
    }

//    private void initCodeWidth() {
//        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        ivRecipetImg.measure(w, h);
//        int height = ivRecipetImg.getMeasuredHeight();
//        int width = ivRecipetImg.getMeasuredWidth();
//        codeWidth = (height + width) / 2;
//    }

    private void getCodeBitmap() {
        Glide.with(this).asBitmap().load(Constant.currentUser.getHeadPortrait())
                .into(new SimpleTarget<Bitmap>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Observable.create((ObservableOnSubscribe<Bitmap>) emitter -> {
                            Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(uri2Code, BGAQRCodeUtil.dp2px(RecipetQRActivity.this, 300),
                                    Color.BLACK, resource);
                            emitter.onNext(bitmap);
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(b -> ivRecipetImg.setImageBitmap(b), t -> {
                                });
                    }
                });
    }

    private void initUri() {
        uri = new Uri();
        uri2Code = new Gson().toJson(uri);
    }

    // 设置金额
    public void setMoney(View view) {
        editTextDialog.show(this, "请输入金额", "设置", "取消");
        editTextDialog.getEdit().setFilters(new InputFilter[]{new MoneyValueFilter()});
        editTextDialog.getYes().setOnClickListener(v -> {
            tvMoney.setText(editTextDialog.getText() + "HK");
            editTextDialog.hideDialog();
            uri.data.money = editTextDialog.getText();
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

    static class Uri {
        private String schem = "com.zxjk.duoduo";
        private String action = "action1";
        private Data data = new Data();

        static class Data {
            private String userID = Constant.userId;
            private String money = "0";
        }
    }
}
