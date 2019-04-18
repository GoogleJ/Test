package com.zxjk.duoduo.ui.walletpage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GetOverOrderResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.walletpage.model.ShenSuRequest;
import com.zxjk.duoduo.ui.widget.TakePopWindow;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.OssUtils;
import com.zxjk.duoduo.utils.TakePicUtil;

import java.io.File;
import java.util.Collections;

import androidx.annotation.Nullable;

public class ShenSuActivity extends BaseActivity implements TakePopWindow.OnItemClickListener {

    private EditText et;

    private LinearLayout llContainer;

    private FrameLayout fl1;
    private ImageView iv1;
    private ImageView ivedit1;

    private FrameLayout fl2;
    private ImageView iv2;
    private ImageView ivedit2;

    private FrameLayout fl3;
    private ImageView iv3;
    private ImageView ivedit3;

    private String url1 = "";
    private String url2 = "";
    private String url3 = "";
    private int currentImg = 1;

    private TakePopWindow selectPicPopWindow;
    private static final int REQUEST_TAKE = 1;
    private static final int REQUEST_ALBUM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shen_su);

        selectPicPopWindow = new TakePopWindow(this);
        selectPicPopWindow.setOnItemClickListener(this);

        et = findViewById(R.id.et);
        llContainer = findViewById(R.id.llContainer);

        fl1 = findViewById(R.id.fl1);
        iv1 = findViewById(R.id.iv1);
        ivedit1 = findViewById(R.id.ivedit1);
        fl2 = findViewById(R.id.fl2);
        iv2 = findViewById(R.id.iv2);
        ivedit2 = findViewById(R.id.ivedit2);
        fl3 = findViewById(R.id.fl3);
        iv3 = findViewById(R.id.iv3);
        ivedit3 = findViewById(R.id.ivedit3);

        iv1.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            ViewGroup.LayoutParams layoutParams = llContainer.getLayoutParams();
            layoutParams.height = iv1.getWidth();
            llContainer.setLayoutParams(layoutParams);
        });

        getPermisson(fl1, result -> {
            if (result) {
                currentImg = 1;
                KeyboardUtils.hideSoftInput(ShenSuActivity.this);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                selectPicPopWindow.showAtLocation(findViewById(android.R.id.content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        getPermisson(fl2, result -> {
            if (result) {
                currentImg = 2;
                KeyboardUtils.hideSoftInput(ShenSuActivity.this);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                selectPicPopWindow.showAtLocation(findViewById(android.R.id.content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        getPermisson(fl3, result -> {
            if (result) {
                currentImg = 3;
                KeyboardUtils.hideSoftInput(ShenSuActivity.this);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                selectPicPopWindow.showAtLocation(findViewById(android.R.id.content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    //提交
    @SuppressLint("CheckResult")
    public void commit(View view) {
        if (TextUtils.isEmpty(et.getText().toString().trim())) {
            ToastUtils.showShort(R.string.input_reason);
            return;
        }

        GetOverOrderResponse data = (GetOverOrderResponse) getIntent().getSerializableExtra("data");

        ShenSuRequest request = new ShenSuRequest();
        request.setAppealReason(et.getText().toString().trim());
        request.setBothOrderId(data.getBothOrderId());
        request.setContact(Constant.currentUser.getMobile());
        request.setPlaintiffId(Constant.currentUser.getId());
        request.setPlaintiffDuoduoId(Constant.currentUser.getDuoduoId());
        request.setCurrency(data.getCurrency());
        request.setPictureOne(url1);
        request.setPictureTwo(url2);
        request.setPictureThree(url3);

        String indicteeId = "";
        String indicteeDuoduoId = "";
        request.setIndicteeId(indicteeId);
        request.setIndicteeDuoduoId(indicteeDuoduoId);

        ServiceFactory.getInstance().getBaseService(Api.class)
                .addAppeal(new Gson().toJson(request))
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(s -> {

                }, this::handleApiError);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void setOnItemClick(View v) {
        selectPicPopWindow.dismiss();
        switch (v.getId()) {
            case R.id.tvCamera:
                TakePicUtil.takePicture(this, REQUEST_TAKE);
                break;
            case R.id.tvPhoto:
                TakePicUtil.albumPhoto(this, REQUEST_ALBUM);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String filePath = "";

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE:
                    filePath = TakePicUtil.file.getAbsolutePath();
                    break;
                case REQUEST_ALBUM:
                    filePath = TakePicUtil.getPath(this, data.getData());
                    break;
                default:
            }
        }

        if (!TextUtils.isEmpty(filePath)) {
            zipFile(Collections.singletonList(filePath), files -> {
                File file = files.get(0);
                OssUtils.uploadFile(file.getAbsolutePath(), url -> {
                    if (currentImg == 1) {
                        url1 = url;
                        ivedit1.setVisibility(View.VISIBLE);
                        fl2.setVisibility(View.VISIBLE);
                    } else if (currentImg == 2) {
                        url2 = url;
                        ivedit2.setVisibility(View.VISIBLE);
                        fl3.setVisibility(View.VISIBLE);
                    } else {
                        url3 = url;
                        ivedit3.setVisibility(View.VISIBLE);
                    }
                });
            });
        }
    }
}
