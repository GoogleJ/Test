package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.VerifiedBean;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.TakePopWindow;
import com.zxjk.duoduo.utils.AesUtil;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.utils.OssUtils;
import com.zxjk.duoduo.utils.TakePicUtil;
import com.zxjk.duoduo.weight.TitleBar;
import com.zxjk.duoduo.weight.dialog.DocumentSelectionDialog;

import java.io.File;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import io.reactivex.functions.Consumer;

import static com.zxjk.duoduo.utils.PermissionUtils.cameraPremissions;

/**
 * @author Administrator
 * @// TODO: 2019\3\25 0025 实名认证界面
 */
@SuppressLint("CheckResult")
public class VerifiedActivity extends BaseActivity implements View.OnClickListener, TakePopWindow.OnItemClickListener {
    public static final int REQUEST_TAKE = 1;
    public static final int REQUEST_ALBUM = 2;
    /**
     * 证件类型
     */
    TextView cardType;
    /**
     * 提交
     */
    TextView verifiedCommit;
    /**
     * 真实姓名
     */
    EditText realName;
    /**
     * 开户账号
     */
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
    TitleBar titleBar;

    String url1;
    String url2;
    String url3;
    ImageView handHeldPassportPhotoEdit;
    DocumentSelectionDialog dialog;
    String realNames;
    String idCards;

    private TakePopWindow selectPicPopWindow;

    private int currentPictureFlag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified);
        initView();
    }


    @SuppressLint("WrongViewCast")
    private void initView() {

        titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftImageView().setOnClickListener(v -> finish());
        selectPicPopWindow = new TakePopWindow(this);
        selectPicPopWindow.setOnItemClickListener(this);
        cardType = findViewById(R.id.card_type);
        verifiedCommit = findViewById(R.id.verified_commit);
        realName = findViewById(R.id.real_name);
        idCard = findViewById(R.id.id_card);
        frontPhotoOfTheDocument = findViewById(R.id.front_photo_of_the_document);
        frontPhotoOfTheDocumentEdit = findViewById(R.id.front_photo_of_the_document_edit);
        reversePhotoOfTheDocument = findViewById(R.id.reverse_photo_of_the_document);
        reversePhotoOfTheDocumentEdit = findViewById(R.id.reverse_photo_of_the_document_edit);
        handHeldPassportPhoto = findViewById(R.id.hand_held_passport_photo);
        handHeldPassportPhotoEdit = findViewById(R.id.hand_held_passport_photo_edit);
        verifiedCommit.setOnClickListener(this);
        frontPhotoOfTheDocument.setOnClickListener(this);
        reversePhotoOfTheDocument.setOnClickListener(this);
        handHeldPassportPhoto.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    public void pleaseSelectTheTypeOfDocument(View view) {
        dialog = new DocumentSelectionDialog(this);
        dialog.setOnClickListener(new DocumentSelectionDialog.OnClickListener() {

            @Override
            public void onSelectedIdCard(String idCard) {
                //点击身份证的时候赋值
                cardType.setTextColor(getColor(R.color.themecolor));
                cardType.setText(idCard);
                dialog.dismiss();
            }

            @Override
            public void onSelectedPassport(String passport) {
                //点击护照的时候给赋值
                cardType.setTextColor(getColor(R.color.themecolor));
                cardType.setText(passport);
                dialog.dismiss();
            }

            @Override
            public void onSelectedOther(String other) {
                //点击其他的时候给赋值
                cardType.setTextColor(getColor(R.color.themecolor));
                cardType.setText(other);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        realNames = realName.getText().toString().trim();
        idCards = idCard.getText().toString().trim();

        switch (v.getId()) {
            case R.id.verified_commit:
                if (TextUtils.isEmpty(realNames)) {
                    ToastUtils.showShort(R.string.edit_real_name);
                    return;
                }

                String s = cardType.getText().toString();
                String otherIdCardType;
                if (s.equals(getString(R.string.id_card))) {
                    otherIdCardType = "1";
                } else if (s.equals(getString(R.string.passport))) {
                    otherIdCardType = "2";
                } else if (s.equals(getString(R.string.other_identity_card))) {
                    otherIdCardType = "3";
                } else {
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
                bean.setPictureHand(url2);
                bean.setPictureReverse(url3);
                certification(AesUtil.getInstance().encrypt(GsonUtils.toJson(bean)));
                break;
            case R.id.front_photo_of_the_document:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                证件正面照
                currentPictureFlag = 1;
                cameraPremissions(this);
                selectPicPopWindow.showAtLocation(this.findViewById(android.R.id.content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.reverse_photo_of_the_document:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                //证件反面照
                currentPictureFlag = 2;
                cameraPremissions(this);
                selectPicPopWindow.showAtLocation(this.findViewById(android.R.id.content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.hand_held_passport_photo:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                //手持证件照
                currentPictureFlag = 3;
                cameraPremissions(this);
                selectPicPopWindow.showAtLocation(this.findViewById(android.R.id.content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            default:
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
            }
        }

        if (!TextUtils.isEmpty(filePath)) {
            zipFile(Collections.singletonList(filePath), files -> {
                File file = files.get(0);
                OssUtils.uploadFile(file.getAbsolutePath(), url -> {
                    if (currentPictureFlag == 1) {
                        url1 = url;
                        frontPhotoOfTheDocumentEdit.setVisibility(View.VISIBLE);
                        GlideUtil.loadCornerImg(frontPhotoOfTheDocument, url, 3);
                        return;
                    }
                    if (currentPictureFlag == 2) {
                        url2 = url;
                        reversePhotoOfTheDocumentEdit.setVisibility(View.VISIBLE);
                        GlideUtil.loadCornerImg(reversePhotoOfTheDocument, url, 3);
                        return;
                    }
                    if (currentPictureFlag == 3) {
                        url3 = url;
                        handHeldPassportPhotoEdit.setVisibility(View.VISIBLE);
                        GlideUtil.loadCornerImg(handHeldPassportPhoto, url, 3);
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
                    SPUtils.getInstance().put("realNames",realNames);
                    ToastUtils.showShort(R.string.verified_success);
                    finish();
                }, this::handleApiError);
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
            default:
                break;
        }
    }
}
