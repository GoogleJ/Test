package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.minepage.scanuri.BaseUri;
import com.zxjk.duoduo.ui.widget.TitleBar;
import com.zxjk.duoduo.utils.GlideUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyQrCodeActivity extends BaseActivity {

    @BindView(R.id.m_qr_code_title_bar_me)
    TitleBar titleBar;
    @BindView(R.id.m_my_qr_code_qr_code_icon)
    ImageView qrCode;

    private ImageView m_my_qr_code_header_icon;
    private ImageView ivIcon;
    private TextView m_my_qr_code_user_name_text;
    private TextView m_my_qr_code_signature_label;

    private int imgSize;
    private BaseUri uri = new BaseUri("action2");
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

        uri.data = Constant.userId;
        uri2Code = new Gson().toJson(uri);

        m_my_qr_code_header_icon = findViewById(R.id.m_my_qr_code_header_icon);
        ivIcon = findViewById(R.id.ivIcon);
        m_my_qr_code_user_name_text = findViewById(R.id.m_my_qr_code_user_name_text);
        m_my_qr_code_signature_label = findViewById(R.id.m_my_qr_code_signature_label);

        m_my_qr_code_user_name_text.setText(Constant.currentUser.getNick());
        m_my_qr_code_signature_label.setText(Constant.currentUser.getSignature());
        titleBar.getLeftImageView().setOnClickListener(v -> finish());

        GlideUtil.loadCornerImg(m_my_qr_code_header_icon, Constant.currentUser.getHeadPortrait(), 2);
        GlideUtil.loadCornerImg(ivIcon, Constant.currentUser.getHeadPortrait(), 2);

        qrCode.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                qrCode.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = qrCode.getWidth();
                imgSize = width;

                ViewGroup.LayoutParams layoutParams = qrCode.getLayoutParams();
                layoutParams.width = imgSize;
                layoutParams.height = imgSize;
                qrCode.setLayoutParams(layoutParams);

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
                .subscribe(b -> qrCode.setImageBitmap(b), t -> {
                });
    }
}
