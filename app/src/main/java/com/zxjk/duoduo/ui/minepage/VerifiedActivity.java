package com.zxjk.duoduo.ui.minepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
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
import com.zxjk.duoduo.weight.dialog.DocumentSelectionDialog;

import java.io.File;
import java.util.Collections;

import androidx.annotation.Nullable;
import io.reactivex.functions.Consumer;

import static com.zxjk.duoduo.ui.EditPersonalInformationFragment.REQUEST_ALBUM;
import static com.zxjk.duoduo.ui.EditPersonalInformationFragment.REQUEST_TAKE;
import static com.zxjk.duoduo.utils.PermissionUtils.cameraPremissions;
import static com.zxjk.duoduo.utils.PermissionUtils.verifyStoragePermissions;


/**
 * @author Administrator
 * @// TODO: 2019\3\25 0025 实名认证界面
 */
public class VerifiedActivity extends BaseActivity implements View.OnClickListener,TakePopWindow.OnItemClickListener {
    /**
     * 身份证类型
     */
    TextView idCardType;
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
    String url;
    ImageView handHeldPassportPhotoEdit;
    DocumentSelectionDialog dialog;
    String realNames;
    String idCards;
    private TakePopWindow selectPicPopWindow;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified);
        initView();
    }

    private void initView() {
        selectPicPopWindow = new TakePopWindow(this);
        selectPicPopWindow.setOnItemClickListener(this);
        idCardType = findViewById(R.id.id_card_type);
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
        frontPhotoOfTheDocumentEdit.setOnClickListener(this);
        reversePhotoOfTheDocument.setOnClickListener(this);
        reversePhotoOfTheDocumentEdit.setOnClickListener(this);
        handHeldPassportPhoto.setOnClickListener(this);
        handHeldPassportPhotoEdit.setOnClickListener(this);

        realNames=realName.getText().toString();
        idCards=idCard.getText().toString();
    }

    public void pleaseSelectTheTypeOfDocument(View view) {
        dialog = new DocumentSelectionDialog(this);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verified_commit:

                if (!realNames.isEmpty()){
                    ToastUtils.showShort(R.string.edit_real_name);
                    return;
                }
                if (!idCards.isEmpty()){
                    ToastUtils.showShort(R.string.edit_id_cards);
                    return;
                }
                String result="2";

                Constant.verifiedBean.setNumber(idCard.getText().toString());
                Constant.verifiedBean.setRealName(realName.getText().toString());
//                Constant.verifiedBean.setType(idCardType.getText().toString());
                Constant.verifiedBean.setType("身份证");
                Constant.verifiedBean.setPictureFront(url);
                Constant.verifiedBean.setPictureHand(url);
                Constant.verifiedBean.setPictureReverse(url);
                certification(AesUtil.getInstance().encrypt(GsonUtils.toJson(Constant.verifiedBean)));
                break;
            case R.id.front_photo_of_the_document:
                //证件正面照
                verifyStoragePermissions(this);
                cameraPremissions(this);
                selectPicPopWindow.showAtLocation(this.findViewById(android.R.id.content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.front_photo_of_the_document_edit:

                //证件正面照编辑
                verifyStoragePermissions(this);
                cameraPremissions(this);
                selectPicPopWindow.showAtLocation(this.findViewById(android.R.id.content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.reverse_photo_of_the_document:
                //证件反面照
                verifyStoragePermissions(this);
                cameraPremissions(this);
                selectPicPopWindow.showAtLocation(this.findViewById(android.R.id.content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.reverse_photo_of_the_document_edit:
                //证件反面照编辑
                verifyStoragePermissions(this);
                cameraPremissions(this);
                selectPicPopWindow.showAtLocation(this.findViewById(android.R.id.content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.hand_held_passport_photo:
                //手持证件照
                verifyStoragePermissions(this);
                cameraPremissions(this);
                selectPicPopWindow.showAtLocation(this.findViewById(android.R.id.content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.hand_held_passport_photo_edit:
                //手持证件照编辑
                verifyStoragePermissions(this);
                cameraPremissions(this);
                selectPicPopWindow.showAtLocation(this.findViewById(android.R.id.content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            default:
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
            }
        }

        if (!TextUtils.isEmpty(filePath)) {
            zipFile(Collections.singletonList(filePath), files -> {
                File file = files.get(0);
                OssUtils.uploadFile(file.getAbsolutePath(), url -> {
                    GlideUtil.loadCornerImg(frontPhotoOfTheDocument, url, CommonUtils.dip2px(this, 2));
                    GlideUtil.loadCornerImg(reversePhotoOfTheDocument, url, CommonUtils.dip2px(this, 2));
                    GlideUtil.loadCornerImg(handHeldPassportPhoto, url, CommonUtils.dip2px(this, 2));
                    this.url=url;

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
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtils.d("DEBUG", "成功" + s);
                    }
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
        }
    }
}