package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.walletpage.RecipetQRActivity;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.weight.TitleBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Administrator
 * @// TODO: 2019\3\19 0019 我的二维码页面
 */
public class MyQrCodeActivity extends BaseActivity {

    @BindView(R.id.m_qr_code_title_bar_me)
    TitleBar titleBar;
    @BindView(R.id.m_my_qr_code_qr_code_icon)
    ImageView qrCode;

    private ImageView m_my_qr_code_header_icon;
    private TextView m_my_qr_code_user_name_text;
    private TextView m_my_qr_code_signature_label;

    private Uri uri = new Uri();
    private String uri2Code;

    public static void start(AppCompatActivity activity) {
        Intent intent = new Intent(activity, MyQrCodeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr_code);
        ButterKnife.bind(this);

        uri2Code = new Gson().toJson(uri);

        m_my_qr_code_header_icon = findViewById(R.id.m_my_qr_code_header_icon);
        m_my_qr_code_user_name_text = findViewById(R.id.m_my_qr_code_user_name_text);
        m_my_qr_code_signature_label = findViewById(R.id.m_my_qr_code_signature_label);

        GlideUtil.loadCornerImg(m_my_qr_code_header_icon, Constant.currentUser.getHeadPortrait(), 2);
        m_my_qr_code_user_name_text.setText(Constant.currentUser.getNick());
        m_my_qr_code_signature_label.setText(Constant.currentUser.getSignature());

        titleBar.getLeftImageView().setOnClickListener(v -> finish());

        getCodeBitmap();
    }

    private void getCodeBitmap() {
        Glide.with(this).asBitmap().load(Constant.currentUser.getHeadPortrait())
                .into(new SimpleTarget<Bitmap>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Observable.create((ObservableOnSubscribe<Bitmap>) emitter -> {
                            Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(uri2Code, BGAQRCodeUtil.dp2px(MyQrCodeActivity.this, 300),
                                    Color.BLACK, resource);
                            emitter.onNext(bitmap);
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(b -> qrCode.setImageBitmap(b), t -> {
                                });
                    }
                });
    }

    class Uri {
        private String schem = "com.zxjk.duoduo";
        private String data = Constant.userId;
        private String action = "action2";
    }
}
