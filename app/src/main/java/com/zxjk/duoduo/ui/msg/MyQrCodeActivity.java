package com.zxjk.duoduo.ui.msg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

 
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.weight.TitleBar;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * @author Administrator
 * @// TODO: 2019\3\19 0019 我的二维码页面 
 */
public class MyQrCodeActivity extends BaseActivity {

    @BindView(R.id.m_qr_code_title_bar_me)
    TitleBar titleBar;
    @BindView(R.id.m_my_qr_code_qr_code_icon)
    ImageView qrCode;

    public static void start(Activity activity){
        Intent intent=new Intent(activity,MyQrCodeActivity.class);
        activity.startActivity(intent);
    }

 
    private void initCreate() {

        createChineseQRCodeWithLogo();
        createEnglishQRCodeWithLogo();

    }
 

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr_code);
        ButterKnife.bind(this);
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initCreate();
    }

    private void createChineseQRCodeWithLogo() {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap logoBitmap = BitmapFactory.decodeResource(MyQrCodeActivity.this.getResources(), R.drawable.icon_header);
                return QRCodeEncoder.syncEncodeQRCode("http://www.baidu.com", BGAQRCodeUtil.dp2px(MyQrCodeActivity.this, 150), Color.parseColor("#ff0000"), logoBitmap);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    qrCode.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(MyQrCodeActivity.this, "生成带logo的中文二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void createEnglishQRCodeWithLogo() {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap logoBitmap = BitmapFactory.decodeResource(MyQrCodeActivity.this.getResources(), R.drawable.icon_header);
                return QRCodeEncoder.syncEncodeQRCode("http://www.baidu.com", BGAQRCodeUtil.dp2px(MyQrCodeActivity.this, 150), Color.BLACK, Color.WHITE,
                        logoBitmap);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    qrCode.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(MyQrCodeActivity.this, "生成带logo的英文二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }


    private void decode(final Bitmap bitmap, final String errorTip) {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github
        .com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return QRCodeDecoder.syncDecodeQRCode(bitmap);
            }

            @Override
            protected void onPostExecute(String result) {
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(MyQrCodeActivity.this, errorTip, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyQrCodeActivity.this, result, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
