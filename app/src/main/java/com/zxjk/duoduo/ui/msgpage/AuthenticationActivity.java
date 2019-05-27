package com.zxjk.duoduo.ui.msgpage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.AuditCertificationBean;
import com.zxjk.duoduo.bean.BitMapBean;
import com.zxjk.duoduo.bean.VerifiedBean;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxException;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.dialog.BottomDialog;
import com.zxjk.duoduo.utils.AesUtil;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.utils.MMKVUtils;
import com.zxjk.duoduo.utils.OssUtils;
import com.zxjk.duoduo.utils.TakePicUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.RequestBody;

import static com.zxjk.duoduo.Constant.APP_CODE;

/**
 * *********************
 * Administrator
 * *********************
 * 2019/5/23
 * *********************
 * 实名认证
 * 大陆大陆身份证信息
 * *********************
 */
public class AuthenticationActivity extends BaseActivity {
    //标题
    @BindView(R.id.tv_title)
    TextView tvTitle;
    //身份证正面
    @BindView(R.id.iv_cardFront)
    ImageView ivCardFront;
    //身份证正面默认图
    @BindView(R.id.iv_default1)
    ImageView ivDefault1;
    //姓名
    @BindView(R.id.et_name)
    EditText etName;
    //身份证号码
    @BindView(R.id.et_idCard)
    EditText etIdCard;
    //正面信息可隐藏
    @BindView(R.id.rl_front)
    RelativeLayout rlFront;
    //身份证反面
    @BindView(R.id.iv_cardReverse)
    ImageView ivCardReverse;
    //身份证反面默认图
    @BindView(R.id.iv_default2)
    ImageView ivDefault2;
    //签发机关
    @BindView(R.id.et_issuingAuthority)
    EditText etIssuingAuthority;
    //有效期
    @BindView(R.id.et_validTerm)
    EditText etValidTerm;
    //反面信息可隐藏
    @BindView(R.id.rl_reverse)
    RelativeLayout rlReverse;
    //正面
    String frontUrl;
    //反面
    String reverseUrl;
    String body = "";
    Bitmap bitmap;
    public static final int REQUEST_TAKE = 1;

    public static final int REQUEST_ALBUM = 2;

    private int currentPictureFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("实名认证");
        initData();
    }

    private void initData() {
        getPermisson(ivCardFront, result -> {
            if (result) {
                currentPictureFlag = 1;
                BottomDialog.dialogType(this, REQUEST_TAKE, REQUEST_ALBUM);

            }
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

        getPermisson(ivCardReverse, result -> {
            if (result) {
                currentPictureFlag = 2;
                BottomDialog.dialogType(this, REQUEST_TAKE, REQUEST_ALBUM);
            }
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path = "";

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE:
                    path = TakePicUtil.file.getAbsolutePath();
                    break;
                case REQUEST_ALBUM:
                    path = TakePicUtil.getPath(this, data.getData());
                    break;
            }
        }
        if (!TextUtils.isEmpty(path)) {
            String p = path;
            zipFile(Collections.singletonList(path), files -> {
                File file = files.get(0);
                OssUtils.uploadFile(file.getAbsolutePath(), url -> {
                    if (currentPictureFlag == 1) {
                        frontUrl = url;
                        ivDefault1.setVisibility(View.GONE);
                        GlideUtil.loadCornerImg(ivCardFront, frontUrl, 5);
//                        bitmap = ImageUtils.getBitmap(file);
//                        if (bitmap == null) {
//                            ToastUtils.showShort("图片有误");
//                            return;
//                        }
                        data("face", url);
                        cardFront();
                    }
                    if (currentPictureFlag == 2) {
                        reverseUrl = url;
                        ivDefault2.setVisibility(View.GONE);
                        GlideUtil.loadCornerImg(ivCardReverse, reverseUrl, 5);
//                        bitmap = ImageUtils.getBitmap(file);
//                        if (bitmap == null) {
//                            ToastUtils.showShort("图片有误");
//                            return;
//                        }
                        data("back", url);
                        cardBack();
                    }
                });


            });
        }
    }

    private void data(String s, String url) {
        BitMapBean bitMapBean = new BitMapBean();
        BitMapBean.ConfigBean configBean = new BitMapBean.ConfigBean();
        bitMapBean.setImage(url);
        configBean.setSide(s);
        bitMapBean.setConfigure(configBean);
        body = new Gson().toJson(bitMapBean);
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    private void cardBack() {
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), body);
        Api api = ServiceFactory.getInstance().getNormalService("https://dm-51.data.aliyun.com/", Api.class);
        api.getOCRBackResult(requestBody, "APP_CODE " + APP_CODE)
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(bindToLifecycle())
                .subscribe(cardBackBean -> {
                    if (cardBackBean.isSuccess()) {
                        etIssuingAuthority.setText(cardBackBean.getIssue());
                        etValidTerm.setText(cardBackBean.getStart_date() + " - " + cardBackBean.getEnd_date());
                        rlReverse.setVisibility(View.VISIBLE);
                    }
                }, t -> ToastUtils.showShort(R.string.unknowerror));
    }

    @SuppressLint("CheckResult")
    private void cardFront() {
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), body);
        Api api = ServiceFactory.getInstance().getNormalService("https://dm-51.data.aliyun.com/", Api.class);
        api.getOCRResult(requestBody, "APP_CODE " + APP_CODE)
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(bindToLifecycle())
                .subscribe(cardFaceBean -> {
                    if (cardFaceBean.isSuccess()) {
                        etName.setText(cardFaceBean.getName());
                        etIdCard.setText(cardFaceBean.getNum());
                        rlFront.setVisibility(View.VISIBLE);
                    }
                }, t -> ToastUtils.showShort(R.string.unknowerror));
    }

    /**
     * 将Bitmap转换成Base64字符串
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        //转换来的base64码需要加前缀，必须是NO_WRAP参数，表示没有空格。
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    @OnClick({R.id.rl_back, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.rl_back:
                finish();
                break;
            //提交
            case R.id.tv_submit:
                commit();
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void commit() {
        if (TextUtils.isEmpty(etIdCard.getText().toString()) ||
                TextUtils.isEmpty(etName.getText().toString()) ||
                TextUtils.isEmpty(frontUrl) ||
                TextUtils.isEmpty(reverseUrl) || TextUtils.isEmpty(etIssuingAuthority.getText().toString()) ||
                TextUtils.isEmpty(etValidTerm.getText().toString())) {
            ToastUtils.showShort("请检查信息是否完整");
            return;
        }
        VerifiedBean bean = new VerifiedBean();
        bean.setNumber(etIdCard.getText().toString());
        bean.setRealName(etName.getText().toString());
        bean.setType("1");
        bean.setPictureFront(frontUrl);
        bean.setPictureHand("");
        bean.setPictureReverse(reverseUrl);
        String dataStr = AesUtil.getInstance().encrypt(GsonUtils.toJson(bean));
        ServiceFactory.getInstance().getNormalService("http://phonethird.market.alicloudapi.com/", Api.class)
                .getCertification(etIdCard.getText().toString(), Constant.currentUser.getMobile(), etName.getText().toString(), "APP_CODE " + APP_CODE)
                .flatMap((Function<AuditCertificationBean, Observable<String>>) auditCertificationBean -> {
                    if (auditCertificationBean.getStatus().equals("01")) {
                        return ServiceFactory.getInstance().getBaseService(Api.class)
                                .certification(dataStr)
                                .compose(RxSchedulers.normalTrans());
                    } else {
                        RxException.ParamsException exception = new RxException.ParamsException();
                        switch (auditCertificationBean.getStatus()) {
                            case "02":
                                exception.setMsg("实名认证不通过");
                                break;
                            case "202":
                                exception.setMsg("无法验证");
                                break;
                            case "204":
                                exception.setMsg("姓名错误");
                                break;
                            case "205":
                                exception.setMsg("身份证号错误");
                                break;
                            case "206":
                                exception.setMsg("电话号码错误");
                                break;
                            default:
                                exception.setMsg("未知异常");
                        }
                        throw exception;
                    }
                })
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(bindToLifecycle())
                .subscribe(response -> {
                    Constant.currentUser.setIsAuthentication("2");
                    Constant.currentUser.setRealname(etName.getText().toString());
                    MMKVUtils.getInstance().enCode("login", Constant.currentUser);
                    SPUtils.getInstance().put("realNames", etName.getText().toString());
                    ToastUtils.showShort(R.string.verified_success);
                    finish();
                }, this::handleApiError);
    }
}
