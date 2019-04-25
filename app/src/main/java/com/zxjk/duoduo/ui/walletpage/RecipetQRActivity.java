package com.zxjk.duoduo.ui.walletpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.minepage.BalanceLeftActivity;
import com.zxjk.duoduo.ui.minepage.DetailListActivity;
import com.zxjk.duoduo.ui.minepage.scanuri.Action1;
import com.zxjk.duoduo.ui.minepage.scanuri.BaseUri;
import com.zxjk.duoduo.ui.widget.dialog.EditTextDialog;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.utils.MoneyValueFilter;
import com.zxjk.duoduo.utils.QRCodeEncoder;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 收款码
 */
public class RecipetQRActivity extends BaseActivity {

    private ImageView ivRecipetImg;
    private ImageView ivCodeLogo;
    private TextView tvMoney, tv_setMoney;
    private EditTextDialog editTextDialog;

    private BaseUri uri;
    private String uri2Code;

    private int imgSize;
    private boolean isSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipet_qr);
        ivRecipetImg = findViewById(R.id.ivRecipetImg);
        ivCodeLogo = findViewById(R.id.ivCodeLogo);
        tvMoney = findViewById(R.id.tvMoney);
        tv_setMoney = findViewById(R.id.tv_setMoney);

        editTextDialog = new EditTextDialog();

        initUri();

        GlideUtil.loadCornerImg(ivCodeLogo, Constant.currentUser.getHeadPortrait(), 2);

        initImgSize();

    }

    private void initImgSize() {
        ivRecipetImg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivRecipetImg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = ivRecipetImg.getWidth();
                int height = ivRecipetImg.getHeight();
                imgSize = Math.min(width, height);

                ViewGroup.LayoutParams layoutParams = ivRecipetImg.getLayoutParams();
                layoutParams.width = imgSize;
                layoutParams.height = imgSize;
                ivRecipetImg.setLayoutParams(layoutParams);

                getCodeBitmap();
            }
        });
    }

    @SuppressLint("CheckResult")
    private void getCodeBitmap() {
        Observable.create((ObservableOnSubscribe<Bitmap>) emitter -> {
            Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(uri2Code, imgSize, Color.BLACK);
            emitter.onNext(bitmap);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> ivRecipetImg.setImageBitmap(b), t -> {
                });
    }

    private void initUri() {
        uri = new BaseUri("action1");
        Action1 action1 = new Action1();
        action1.money = "0.00";
        uri.data = action1;
        uri2Code = new Gson().toJson(uri);
    }

    // 设置金额
    @SuppressLint("SetTextI18n")
    public void setMoney(View view) {
        if (!isSet) {
            editTextDialog.show(this, getString(R.string.input_money), getString(R.string.set), getString(R.string.cancel));
            editTextDialog.getEdit().setFilters(new InputFilter[]{new MoneyValueFilter()});
            editTextDialog.getYes().setOnClickListener(v -> {
                if (!TextUtils.isEmpty(editTextDialog.getText())) {
                    tvMoney.setText(editTextDialog.getText() + " HK");
                    editTextDialog.hideDialog();
                    Action1 action1 = new Action1();
                    action1.money = editTextDialog.getText();
                    uri.data = action1;
                    uri2Code = new Gson().toJson(uri);
                    getCodeBitmap();
                    tv_setMoney.setText(getString(R.string.clear_money));
                    isSet = true;
                } else {
                    ToastUtils.showShort(getString(R.string.please_set_money));
                }
            });
        } else {
            tvMoney.setText("");
            Action1 action1 = new Action1();
            action1.money = "0.00";
            uri.data = action1;
            uri2Code = new Gson().toJson(uri);
            getCodeBitmap();
            tv_setMoney.setText(getString(R.string.set_money));
            isSet = false;
        }
    }

    // 进入我的余额
    public void enterMyBalance(View view) {
        startActivity(new Intent(this, BalanceLeftActivity.class));
    }

    // 收款详情
    public void jump2Detail(View view) {
        startActivity(new Intent(this, DetailListActivity.class));
    }

    public void back(View view) {
        finish();
    }
}
