package com.zxjk.duoduo.ui.minepage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.MyQrCodeActivity;
import com.zxjk.duoduo.ui.widget.dialog.ChooseSexDialog;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.utils.MMKVUtils;
import com.zxjk.duoduo.utils.OssUtils;
import com.zxjk.duoduo.utils.TakePicUtil;

import java.io.File;
import java.util.Collections;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * author L
 * create at 2019/5/7
 * description: 个人资料
 */
@SuppressLint("CheckResult")
public class UserInfoActivity extends BaseActivity {

    private TextView tvUserInfoSex;
    private TextView tv_DuoDuoNumber;
    private TextView tv_realName;
    private TextView tv_phoneNumber;
    private TextView tv_personalizedSignature;
    private TextView tv_email;
    private TextView tv_walletAddress;


    private ImageView iv_headPortrait;
    private TextView tv_nickname;


    private static final int REQUEST_TAKE = 1;
    private static final int REQUEST_ALBUM = 2;
    String type = "type";
    int changeNick = 2;
    int changeSign = 1;
    int changeEmail = 3;

    private ChooseSexDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.userinfo));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());

        findViews();

        dialog = new ChooseSexDialog(this, sex -> {
            if (sex.equals(Constant.currentUser.getSex())) {
                return;
            }
            LoginResponse update = new LoginResponse(Constant.currentUser.getId());
            update.setSex(sex);
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .updateUserInfo(GsonUtils.toJson(update))
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .subscribe(s -> {
                        Constant.currentUser.setSex(sex);
                        MMKVUtils.getInstance().enCode("login", Constant.currentUser);
                        tvUserInfoSex.setText(CommonUtils.getSex(Constant.currentUser.getSex()));
                    }, this::handleApiError);
        });
    }

    @SuppressLint("SetTextI18n")
    private void bindData() {
        GlideUtil.loadCornerImg(iv_headPortrait, Constant.currentUser.getHeadPortrait(), 5);
        String mobile = Constant.currentUser.getMobile();
        tv_nickname.setText(Constant.currentUser.getNick());
        tvUserInfoSex.setText(CommonUtils.getSex(Constant.currentUser.getSex()));
        tv_DuoDuoNumber.setText(Constant.currentUser.getDuoduoId());
        tv_realName.setText(TextUtils.isEmpty(Constant.currentUser.getRealname()) ? "暂未认证" : Constant.currentUser.getRealname());
        tv_phoneNumber.setText(mobile.substring(0, 3) + "****" + mobile.substring(7, 11));
        tv_personalizedSignature.setText(TextUtils.isEmpty(Constant.currentUser.getSignature()) ? "暂无" : Constant.currentUser.getSignature());
        tv_email.setText(TextUtils.isEmpty(Constant.currentUser.getEmail()) ? "暂无" : Constant.currentUser.getEmail());
        tv_walletAddress.setText(Constant.currentUser.getWalletAddress());
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAuthentication();
        bindData();
    }

    private void isAuthentication() {
        if (!Constant.currentUser.getIsAuthentication().equals("0")) {
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getCustomerAuth()
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .subscribe(s -> {
                        Constant.currentUser.setIsAuthentication(s);
                        MMKVUtils.getInstance().enCode("login", Constant.currentUser);
                        switch (s) {
                            case "0":
                                tv_realName.setText("已认证");
                                break;
                            case "2":
                                tv_realName.setText("认证审核中");
                                break;
                            case "1":
                                tv_realName.setText("认证未通过");
                                break;
                            default:
                                tv_realName.setText("未认证");
                                break;
                        }
                    }, this::handleApiError);
        }
    }

    private void findViews() {
        RelativeLayout rl_walletAddress = findViewById(R.id.rl_walletAddress);
        if (Constant.isVerifyVerision) {
            rl_walletAddress.setVisibility(View.GONE);
        }
        iv_headPortrait = findViewById(R.id.iv_headPortrait);
        RelativeLayout rl_headPortrait = findViewById(R.id.rl_headPortrait);
        tv_nickname = findViewById(R.id.tv_nickname);
        tvUserInfoSex = findViewById(R.id.tvUserInfoSex);
        tv_DuoDuoNumber = findViewById(R.id.tv_DuoDuoNumber);
        tv_realName = findViewById(R.id.tv_realName);
        tv_phoneNumber = findViewById(R.id.tv_phoneNumber);
        tv_personalizedSignature = findViewById(R.id.tv_personalizedSignature);
        tv_email = findViewById(R.id.tv_email);
        tv_walletAddress = findViewById(R.id.tv_walletAddress);

        getPermisson(rl_headPortrait, result -> {
            if (result) {
                dialogType();
            }
        }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    //修改昵称
    public void changeNick(View view) {
        Intent intent = new Intent(this, UpdateUserInfoActivity.class);
        intent.putExtra(type, changeNick);
        startActivity(intent);
    }

    //修改手机
    public void changeMobile(View view) {
        startActivity(new Intent(this, ChangePhoneActivity.class));
    }

    //修改性别
    public void changeSex(View view) {
        dialog.show();
    }

    //我的二维码
    public void QRCode(View view) {
        startActivity(new Intent(this, MyQrCodeActivity.class));
    }

    //修改个性签名
    public void changeSign(View view) {
        Intent intent = new Intent(this, UpdateUserInfoActivity.class);
        intent.putExtra(type, changeSign);
        startActivity(intent);
    }

    //修改Email
    public void changeEmail(View view) {
        Intent intent = new Intent(this, UpdateUserInfoActivity.class);
        intent.putExtra(type, changeEmail);
        startActivity(intent);
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
                    LoginResponse update = new LoginResponse(Constant.userId);
                    update.setHeadPortrait(url);
                    ServiceFactory.getInstance().getBaseService(Api.class)
                            .updateUserInfo(GsonUtils.toJson(update))
                            .compose(bindToLifecycle())
                            .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(UserInfoActivity.this)))
                            .compose(RxSchedulers.normalTrans())
                            .subscribe(response -> {
                                Constant.currentUser.setHeadPortrait(url);
                                MMKVUtils.getInstance().enCode("login", Constant.currentUser);
                                GlideUtil.loadCornerImg(iv_headPortrait, url, 5);
                                UserInfo userInfo = new UserInfo(Constant.userId, Constant.currentUser.getNick(), Uri.parse(Constant.currentUser.getHeadPortrait()));
                                RongIM.getInstance().setCurrentUserInfo(userInfo);
                                ToastUtils.showShort(R.string.update_head_portrail);
                            }, UserInfoActivity.this::handleApiError);
                });
            });
        }
    }

    private void dialogType() {
        KeyboardUtils.hideSoftInput(UserInfoActivity.this);
        NiceDialog.init().setLayoutId(R.layout.layout_general_dialog6).setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                //拍照
                holder.setOnClickListener(R.id.tv_photograph, v -> {
                    dialog.dismiss();
                    TakePicUtil.takePicture(UserInfoActivity.this, REQUEST_TAKE);
                });
                //相册选择
                holder.setOnClickListener(R.id.tv_photo_select, v -> {
                    dialog.dismiss();
                    TakePicUtil.albumPhoto(UserInfoActivity.this, REQUEST_ALBUM);
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
