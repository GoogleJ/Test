package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.minepage.scanuri.Action1;
import com.zxjk.duoduo.ui.minepage.scanuri.BaseUri;
import com.zxjk.duoduo.ui.widget.TitleBar;
import com.zxjk.duoduo.utils.CommonUtils;

import org.json.JSONObject;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 *
 */
@SuppressLint("CheckResult")
public class QrCodeActivity extends BaseActivity implements QRCodeView.Delegate {
    @BindView(R.id.m_qr_code_zxing_view)
    ZXingView zxingview;
    @BindView(R.id.m_qr_code_title_bar)
    TitleBar titleBar;
    /**
     * 关于二维码扫描的实现
     */
    final int ACTION_LADY_PICKER_FLAG = 0X106;
    /**
     * 关于二维码的实现
     */
    final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;


    protected void initUI() {
        zxingview.setDelegate(this);
        titleBar.getRightTitle().setOnClickListener(v -> {
//            Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(QrCodeActivity.this)
//                    .cameraFileDir(null)
//                    .maxChooseCount(10)
//                    .selectedPhotos(null)
//                    .pauseOnScroll(false)
//                    .build();
//            startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
        });
        titleBar.getLeftImageView().setOnClickListener(v -> finish());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        ButterKnife.bind(this);

        initUI();
    }



    @Override
    public void onScanQRCodeSuccess(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            Object schem = jsonObject.opt("schem");
            if (!schem.equals("com.zxjk.duoduo")) {
                throw new RuntimeException();
            }

            Object action = jsonObject.opt("action");

            if (action.equals("action1")) {
                BaseUri<Action1> uri = new Gson().fromJson(result, new TypeToken<BaseUri<Action1>>() {
                }.getType());
                Intent intent = new Intent(this, TransferActivity.class);
                intent.putExtra("fromScan", true);
                intent.putExtra("money", uri.data.money);
                intent.putExtra("userId", uri.data.userId);
                startActivity(intent);
                finish();
            } else if (action.equals("action2")) {
                BaseUri<String> uri = new Gson().fromJson(result, new TypeToken<BaseUri<String>>() {
                }.getType());

                String userId = uri.data;

                if (userId.equals(Constant.userId)) {
                    //扫到了自己的二维码
                    finish();
                    return;
                }

                if (Constant.friendsList == null) {
                    ServiceFactory.getInstance().getBaseService(Api.class)
                            .getFriendListById()
                            .compose(bindToLifecycle())
                            .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                            .compose(RxSchedulers.normalTrans())
                            .subscribe(friendInfoResponses -> {
                                if (Constant.friendsList == null) {
                                    Constant.friendsList = friendInfoResponses;
                                }

                                handleFriendList(userId);
                            }, this::handleApiError);
                } else {
                    handleFriendList(userId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        if (isDark) {
            zxingview.openFlashlight();
        }
    }

    private void handleFriendList(String userId) {
        if (userId.equals(Constant.userId)) {
            //扫到了自己
            Intent intent = new Intent(this, FriendDetailsActivity.class);
            intent.putExtra("friendId", userId);
            startActivity(intent);
            return;
        }
        for (FriendInfoResponse f : Constant.friendsList) {
            if (f.getId().equals(userId)) {
                //扫到了自己的好友，进入详情页（可聊天）
                Intent intent = new Intent(this, FriendDetailsActivity.class);
                intent.putExtra("searchFriendDetails", f);
                startActivity(intent);
                finish();
                return;
            }
        }

        //扫到了陌生人，进入加好友页面
        Intent intent = new Intent(this, AddFriendDetailsActivity.class);
        intent.putExtra("newFriendId", userId);
        startActivity(intent);
        finish();
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
        zxingview.closeFlashlight();
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

    @Override
    protected void onDestroy() {
        zxingview.onDestroy();
        super.onDestroy();
    }


    private void readSystemAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, ACTION_LADY_PICKER_FLAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        zxingview.startSpotAndShowRect(); // 显示扫描框，并开始识别

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
//            final String picturePath = BGAPhotoPickerActivity.getSelectedPhotos(data).get(0);
            // 本来就用到 QRCodeView 时可直接调 QRCodeView 的方法，走通用的回调
//            zxingview.syncDecodeQRCode(picturePath);
        }
    }

}
