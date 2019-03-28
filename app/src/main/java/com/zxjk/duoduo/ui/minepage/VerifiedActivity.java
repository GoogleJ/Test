package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
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
import com.zxjk.duoduo.weight.TitleBar;
import com.zxjk.duoduo.weight.dialog.DocumentSelectionDialog;

import java.io.File;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import io.reactivex.functions.Consumer;

import static com.zxjk.duoduo.utils.PermissionUtils.cameraPremissions;
import static com.zxjk.duoduo.utils.PermissionUtils.verifyStoragePermissions;

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

    String url;
    ImageView handHeldPassportPhotoEdit;
    DocumentSelectionDialog dialog;
    String realNames;
    String idCards;

    private TakePopWindow selectPicPopWindow;

    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_CODE2 = 2;
    private static final int REQUEST_CODE3 = 3;

    int type1 ;
    int type2;
    int type3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified);
        initView();

    }



    @SuppressLint("WrongViewCast")
    private void initView() {

        titleBar=findViewById(R.id.title_bar);
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
        frontPhotoOfTheDocumentEdit.setOnClickListener(this);
        reversePhotoOfTheDocument.setOnClickListener(this);
        reversePhotoOfTheDocumentEdit.setOnClickListener(this);
        handHeldPassportPhoto.setOnClickListener(this);
        handHeldPassportPhotoEdit.setOnClickListener(this);
        realNames = realName.getText().toString();
        idCards = idCard.getText().toString();
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
        switch (v.getId()) {
            case R.id.verified_commit:
                if (!realNames.isEmpty()) {
                    ToastUtils.showShort(R.string.edit_real_name);
                    return;
                }
                if (!idCards.isEmpty()) {
                    ToastUtils.showShort(R.string.edit_id_cards);
                    return;
                }

                String idCards=getString(R.string.id_card);
                String passport=getString(R.string.passport);
                String otherIdCard=getString(R.string.other_identity_card);


                Constant.verifiedBean.setNumber(idCard.getText().toString());
                Constant.verifiedBean.setRealName(realName.getText().toString());

                if (idCards.equals(cardType.getText().toString())){
                    String idCardType="1";
                    Constant.verifiedBean.setType(idCardType);
                }else if (passport.equals(cardType.getText().toString())){
                    String passportType="2";
                    Constant.verifiedBean.setType(passportType);
                }else{
                    String otherIdCardType="3";
                    Constant.verifiedBean.setType(otherIdCardType);
                }

                Constant.verifiedBean.setPictureFront(url);
                Constant.verifiedBean.setPictureHand(url);
                Constant.verifiedBean.setPictureReverse(url);
                Constant.verifiedBean.getState();
                certification(AesUtil.getInstance().encrypt(GsonUtils.toJson(Constant.verifiedBean)));
                break;
            case R.id.front_photo_of_the_document:
//                证件正面照
                type1=1;
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
                type2=2;
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
                type3=3;
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
            zipFile(Collections.singletonList(filePath), new OnZipFileFinish() {
                @Override
                public void onFinish(List<File> files) {
                    File file = files.get(0);
                    OssUtils.uploadFile(file.getAbsolutePath(), new OssUtils.OssCallBack() {
                        @Override
                        public void onSuccess(String url) {
                            VerifiedActivity.this.url = url;
                            if (type1 == REQUEST_CODE) {
                                GlideUtil.loadCornerImg(frontPhotoOfTheDocument, url, 3);
                                return;
                            }
                            if (type2 == REQUEST_CODE2) {
                                GlideUtil.loadCornerImg(reversePhotoOfTheDocument, url, 3);
                                return;
                            }
                            if (type3 == REQUEST_CODE3) {
                                GlideUtil.loadCornerImg(handHeldPassportPhoto, url, 3);
                                return;
                            }


                        }
                    });
                }
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
                        ToastUtils.showShort(R.string.verified_success);
                        finish();
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
            default:
                break;
        }
    }
}
