package com.zxjk.duoduo.ui.msgpage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.response.GroupResponse;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.minepage.scanuri.BaseUri;
import com.zxjk.duoduo.utils.ImageUtil;
import com.zxjk.duoduo.utils.SaveImageUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class GroupQRActivity extends BaseActivity {

    private LinearLayout llQR;
    private ImageView ivHead;
    private TextView tvTitle1;
    private ImageView m_my_qr_code_qr_code_icon;
    private ImageView ivIcon;

    private int imgSize;

    private BaseUri uri = new BaseUri("action3");
    private String uri2Code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_qr);

        GroupResponse data = (GroupResponse) getIntent().getSerializableExtra("data");

        uri.data = new GroupQRData();
        ((GroupQRData) uri.data).groupId = data.getGroupInfo().getId();
        ((GroupQRData) uri.data).inviterId = Constant.userId;
        ((GroupQRData) uri.data).groupName = data.getGroupInfo().getGroupNikeName();
        uri2Code = new Gson().toJson(uri);

        llQR = findViewById(R.id.llQR);
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        tvTitle1 = findViewById(R.id.tvTitle1);
        tvTitle1.setText(R.string.group_qr);
        ((TextView) findViewById(R.id.tv_title)).setText(R.string.group_qr);

        ivHead = findViewById(R.id.ivHead);
        m_my_qr_code_qr_code_icon = findViewById(R.id.m_my_qr_code_qr_code_icon);
        ivIcon = findViewById(R.id.ivIcon);

        m_my_qr_code_qr_code_icon.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                m_my_qr_code_qr_code_icon.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                imgSize = m_my_qr_code_qr_code_icon.getWidth();

                ViewGroup.LayoutParams layoutParams = m_my_qr_code_qr_code_icon.getLayoutParams();
                layoutParams.width = imgSize;
                layoutParams.height = imgSize;
                m_my_qr_code_qr_code_icon.setLayoutParams(layoutParams);

                getCodeBitmap();
            }
        });

        save2Phone();

        loadHead(data);
        tvTitle1.setText(data.getGroupInfo().getGroupNikeName());
    }

    private void save2Phone() {
        getPermisson(findViewById(R.id.tvSave), g -> {
            //保存到手机
            if (bitmap == null) {
                return;
            }
            llQR.buildDrawingCache();

            SaveImageUtil.get().savePic(llQR.getDrawingCache(), success -> {
                if (success) {
                    ToastUtils.showShort(R.string.savesucceed);
                    return;
                }
                ToastUtils.showShort(R.string.savefailed);
            });
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void loadHead(GroupResponse data) {
        String s = "";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.getCustomers().size(); i++) {
            stringBuilder.append(data.getCustomers().get(i).getHeadPortrait() + ",");
            if (i == data.getCustomers().size() - 1 || i == 8) {
                s = stringBuilder.substring(0, stringBuilder.length() - 1);
                break;
            }
        }

        ImageUtil.loadGroupPortrait(ivHead, s);
    }

    private Bitmap bitmap;

    @SuppressLint("CheckResult")
    private void getCodeBitmap() {
        Observable.create((ObservableOnSubscribe<Bitmap>) emitter -> {
            bitmap = QRCodeEncoder.syncEncodeQRCode(uri2Code, imgSize, Color.BLACK);
            emitter.onNext(bitmap);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> m_my_qr_code_qr_code_icon.setImageBitmap(b), t -> {
                });
    }

    //分享二维码
    public void share(View view) {
        RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                llQR.buildDrawingCache();
                Constant.shareGroupQR = llQR.getDrawingCache();
                Intent intent = new Intent(GroupQRActivity.this, ShareGroupQRActivity.class);
                intent.putParcelableArrayListExtra("data", (ArrayList<Conversation>) conversations);
                startActivity(intent);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    public static class GroupQRData {
        public String groupId;
        public String inviterId;
        public String groupName;
    }
}
