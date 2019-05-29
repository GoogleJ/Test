package com.zxjk.duoduo.ui.minepage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.VerifiedBean;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.AesUtil;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.utils.MMKVUtils;
import com.zxjk.duoduo.utils.OssUtils;
import com.zxjk.duoduo.utils.TakePicUtil;

import java.io.File;
import java.util.Collections;

/**
 * author L
 * create at 2019/5/7
 * description: 实名认证
 */
@SuppressLint("CheckResult")
public class VerifiedActivity extends BaseActivity {
    public static final int REQUEST_TAKE = 1;
    public static final int REQUEST_ALBUM = 2;


    TextView tv_certificateType;
    EditText et_realName;

    EditText idCard;
    /**
     * 证件正面照
     */
    ImageView frontPhotoOfTheDocument;
    /**
     * 证件正面照编辑
     */
    ImageView frontPhotoOfTheDocumentEdit;
    /**
     * 证件反面照
     */
    ImageView reversePhotoOfTheDocument;
    /**
     * 证件反面照编辑
     */
    ImageView reversePhotoOfTheDocumentEdit;
    /**
     * 手持证件照
     */
    ImageView handHeldPassportPhoto;
    /**
     * 手持证件照编辑
     */

    String url1;
    String url2;
    String url3;
    ImageView handHeldPassportPhotoEdit;
    String realNames;
    String idCards;

    TextView tv_commit;

    private int currentPictureFlag;
    private String otherIdCardType = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.verified));
        tv_commit = findViewById(R.id.tv_commit);
        tv_commit.setVisibility(View.VISIBLE);
        tv_commit.setText(getString(R.string.commit));
        initView();
    }


    @SuppressLint("WrongViewCast")
    private void initView() {
        findViewById(R.id.rl_back).setOnClickListener(v -> {
            CommonUtils.hideInputMethod(this);
            finish();
        });
        tv_certificateType = findViewById(R.id.tv_certificateType);
        otherIdCardType = getIntent().getStringExtra("otherIdCardType");
        if (otherIdCardType.equals("2")) {
            tv_certificateType.setText("护照");
        } else if (otherIdCardType.equals("3")) {
            tv_certificateType.setText("其他国家或地区身份证");
        }
        et_realName = findViewById(R.id.et_realName);
        idCard = findViewById(R.id.id_card);
        frontPhotoOfTheDocument = findViewById(R.id.front_photo_of_the_document);
        frontPhotoOfTheDocumentEdit = findViewById(R.id.front_photo_of_the_document_edit);
        reversePhotoOfTheDocument = findViewById(R.id.reverse_photo_of_the_document);
        reversePhotoOfTheDocumentEdit = findViewById(R.id.reverse_photo_of_the_document_edit);
        handHeldPassportPhoto = findViewById(R.id.hand_held_passport_photo);
        handHeldPassportPhotoEdit = findViewById(R.id.hand_held_passport_photo_edit);
        tv_commit.setOnClickListener(v -> commit());

        getPermisson(frontPhotoOfTheDocument, result -> {
            if (result) {
                currentPictureFlag = 1;
                dialogType();

            }
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

        getPermisson(reversePhotoOfTheDocument, result -> {
            if (result) {
                currentPictureFlag = 2;
                dialogType();
            }
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

        getPermisson(handHeldPassportPhoto, result -> {
            if (result) {
                currentPictureFlag = 3;
                dialogType();

            }
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void commit() {
        realNames = et_realName.getText().toString().trim();
        idCards = idCard.getText().toString().trim();
        if (TextUtils.isEmpty(realNames)) {
            ToastUtils.showShort(R.string.edit_real_name);
            return;
        }

        if (otherIdCardType.equals("")) {
            ToastUtils.showShort(getString(R.string.please_select_the_type_of_document));
            return;
        }

        if (TextUtils.isEmpty(idCards)) {
            ToastUtils.showShort(R.string.edit_id_cards);
            return;
        }

        if (TextUtils.isEmpty(url1)) {
            ToastUtils.showShort(R.string.verified_realname1);
            return;
        }

        if (TextUtils.isEmpty(url2)) {
            ToastUtils.showShort(R.string.verified_realname2);
            return;
        }

        if (TextUtils.isEmpty(url3)) {
            ToastUtils.showShort(R.string.verified_realname3);
            return;
        }

        VerifiedBean bean = new VerifiedBean();

        bean.setNumber(idCards);
        bean.setRealName(realNames);
        bean.setType(otherIdCardType);
        bean.setPictureFront(url1);
        bean.setPictureHand(url3);
        bean.setPictureReverse(url2);
        certification(AesUtil.getInstance().encrypt(GsonUtils.toJson(bean)));
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
            }
        }

        if (!TextUtils.isEmpty(filePath)) {
            zipFile(Collections.singletonList(filePath), files -> {
                File file = files.get(0);
                OssUtils.uploadFile(file.getAbsolutePath(), url -> {
                    if (currentPictureFlag == 1) {
                        url1 = url;
                        frontPhotoOfTheDocumentEdit.setVisibility(View.VISIBLE);
                        GlideUtil.loadCornerImg(frontPhotoOfTheDocument, url, 5);
                        return;
                    }
                    if (currentPictureFlag == 2) {
                        url2 = url;
                        reversePhotoOfTheDocumentEdit.setVisibility(View.VISIBLE);
                        GlideUtil.loadCornerImg(reversePhotoOfTheDocument, url, 5);
                        return;
                    }
                    if (currentPictureFlag == 3) {
                        url3 = url;
                        handHeldPassportPhotoEdit.setVisibility(View.VISIBLE);
                        GlideUtil.loadCornerImg(handHeldPassportPhoto, url, 5);
                    }
                });
            });
        }
    }

    public void certification(String data) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .certification(data)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    Constant.currentUser.setIsAuthentication("2");
                    Constant.currentUser.setRealname(realNames);
                    MMKVUtils.getInstance().enCode("login", Constant.currentUser);
                    SPUtils.getInstance().put("realNames", realNames);
                    ToastUtils.showShort(R.string.verified_success);
                    finish();
                }, this::handleApiError);
    }

    //底部弹窗dialog 拍照、选择相册、取消
    private void dialogType() {
        NiceDialog.init().setLayoutId(R.layout.layout_general_dialog6).setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                //拍照
                holder.setOnClickListener(R.id.tv_photograph, v -> {
                    dialog.dismiss();
                    TakePicUtil.takePicture(VerifiedActivity.this, REQUEST_TAKE);
                });
                //相册选择
                holder.setOnClickListener(R.id.tv_photo_select, v -> {
                    dialog.dismiss();
                    TakePicUtil.albumPhoto(VerifiedActivity.this, REQUEST_ALBUM);
                });
                //取消
                holder.setOnClickListener(R.id.tv_cancel, v -> dialog.dismiss());

            }
        }).setShowBottom(true)
                .setOutCancel(true)
                .setDimAmount(0.5f)
                .show(getSupportFragmentManager());
    }

}
