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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.minepage.BalanceLeftActivity;
import com.zxjk.duoduo.ui.minepage.DetailListActivity;
import com.zxjk.duoduo.ui.minepage.scanuri.Action1;
import com.zxjk.duoduo.ui.minepage.scanuri.BaseUri;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.utils.MoneyValueFilter;
import com.zxjk.duoduo.utils.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RecipetQRActivity extends BaseActivity {

    private ImageView ivRecipetImg;
    private TextView tvMoney, tv_setMoney;

    private BaseUri uri;
    private String uri2Code;

    private int imgSize;
    private boolean isSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipet_qr);
        ivRecipetImg = findViewById(R.id.ivRecipetImg);
        ImageView ivCodeLogo = findViewById(R.id.ivCodeLogo);
        tvMoney = findViewById(R.id.tvMoney);
        tv_setMoney = findViewById(R.id.tv_setMoney);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.receiptCode));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());


        initUri();

        GlideUtil.loadCornerImg(ivCodeLogo, Constant.currentUser.getHeadPortrait(), 5);

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
        action1.money = "";
        uri.data = action1;
        uri2Code = new Gson().toJson(uri);
    }

    // 设置金额
    @SuppressLint("SetTextI18n")
    public void setMoney(View view) {
        if (!isSet) {
            NiceDialog.init().setLayoutId(R.layout.layout_general_dialog3).setConvertListener(new ViewConvertListener() {
                @Override
                protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                    holder.setText(R.id.tv_title, getString(R.string.please_set_money));
                    EditText editText = holder.getView(R.id.et_content);
                    TextView tv_cancel = holder.getView(R.id.tv_cancel);
                    TextView tv_notarize = holder.getView(R.id.tv_notarize);
                    tv_cancel.setText(getString(R.string.cancel));
                    tv_notarize.setText(getString(R.string.set));
                    editText.setFilters(new InputFilter[]{new MoneyValueFilter()});
                    holder.setOnClickListener(R.id.tv_cancel, v -> dialog.dismiss());
                    holder.setOnClickListener(R.id.tv_notarize, v -> {
                        dialog.dismiss();
                        if (!TextUtils.isEmpty(editText.getText())) {
                            tvMoney.setText(editText.getText() + " HK");
                            Action1 action1 = new Action1();
                            action1.money = editText.getText().toString();
                            uri.data = action1;
                            uri2Code = new Gson().toJson(uri);
                            getCodeBitmap();
                            tv_setMoney.setText(getString(R.string.clear_money));
                            isSet = true;
                        } else {
                            ToastUtils.showShort(getString(R.string.please_set_money));
                        }
                    });

                }
            }).setDimAmount(0.5f).setOutCancel(false).show(getSupportFragmentManager());
        } else {
            tvMoney.setText("");
            Action1 action1 = new Action1();
            action1.money = "";
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


}
