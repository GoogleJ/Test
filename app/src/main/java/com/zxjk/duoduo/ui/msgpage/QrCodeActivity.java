package com.zxjk.duoduo.ui.msgpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.weight.TitleBar;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.qrcode.core.ProcessDataTask;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

import static com.zxjk.duoduo.utils.PermissionUtils.cameraPremissions;


/**
 * @author Administrator
 * @// TODO: 2019\3\19 0019 扫描二维码
 */

public class QrCodeActivity extends BaseActivity implements QRCodeView.Delegate {
    @BindView(R.id.m_qr_code_zxing_view)
    ZXingView zxingview;
    @BindView(R.id.m_qr_code_title_bar)
    TitleBar titleBar;
    protected ProcessDataTask mProcessDataTask;
    /**
     * 关于二维码扫描的实现
     */
    final int ACTION_LADY_PICKER_FLAG = 0X106;
    /**
     * 关于二维码的实现
     */
    final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    /**
     * 判断是否开灯
     */
    private boolean isFlashlight;



    protected void initUI() {

        zxingview.setDelegate(this);
        titleBar.getRightTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(QrCodeActivity.this)
                        .cameraFileDir(null)
                        .maxChooseCount(1)
                        .selectedPhotos(null)
                        .pauseOnScroll(false)
                        .build();
                startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);

            }
        });
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        ButterKnife.bind(this);
        cameraPremissions(this);
        initUI();


    }


    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i("ssss", "result:" + result);
        ToastUtils.showShort(result);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    /**
     * 开始扫描
     */
    private void startScan() {
        zxingview.startCamera();
        zxingview.startSpotAndShowRect();
        zxingview.startSpot(); // 开始识别
    }

    /**
     * 关闭扫描，同时关灯 关摄像头
     */
    private void stopScan() {
        zxingview.stopCamera();
        zxingview.stopSpotAndHiddenRect();
        if (isFlashlight) {
            closeFlashlight();
        }
    }

    /**
     * 开灯
     */
    private void openFlashlight() {
        isFlashlight = true;
        zxingview.openFlashlight();


        //  idTorch.setImageResource(R.drawable.torch_off);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startScan();
    }

    @Override
    protected void onStop() {
        stopScan();
        super.onStop();
    }

    /**
     * 关灯
     */
    private void closeFlashlight() {
        zxingview.closeFlashlight();
        isFlashlight = false;
    }


    private void readSystemAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, ACTION_LADY_PICKER_FLAG);
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        zxingview.startSpotAndShowRect();
        zxingview.startSpotAndShowRect(); // 显示扫描框，并开始识别

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = BGAPhotoPickerActivity.getSelectedPhotos(data).get(0);
            // 本来就用到 QRCodeView 时可直接调 QRCodeView 的方法，走通用的回调
//            zxingview.decodeQRCode(picturePath);
        }
    }

}
