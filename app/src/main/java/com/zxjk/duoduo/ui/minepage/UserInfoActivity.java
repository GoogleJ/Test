package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.TakePopWindow;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.utils.OssUtils;
import com.zxjk.duoduo.utils.TakePicUtil;
import java.io.File;
import java.util.Collections;
import androidx.annotation.Nullable;

@SuppressLint("CheckResult")
public class UserInfoActivity extends BaseActivity implements TakePopWindow.OnItemClickListener {

    private ImageView ivUserInfoHead;
    private TextView tvUserInfoNick;
    private TextView tvUserInfoSex;
    private TextView tvUserInfoDUDUNum;
    private TextView tvUserInfoRealName;
    private TextView tvUserInfoPhone;
    private TextView tvUserInfoSign;
    private TextView tvUserInfoEmail;
    private TextView tvUserInfoWallet;

    private TakePopWindow selectPicPopWindow;
    private static final int REQUEST_TAKE = 1;
    private static final int REQUEST_ALBUM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        selectPicPopWindow = new TakePopWindow(this);
        selectPicPopWindow.setOnItemClickListener(this);

        findViews();
    }

    private void findViews() {
        ivUserInfoHead = findViewById(R.id.ivUserInfoHead);
        tvUserInfoNick = findViewById(R.id.tvUserInfoNick);
        tvUserInfoSex = findViewById(R.id.tvUserInfoSex);
        tvUserInfoDUDUNum = findViewById(R.id.tvUserInfoDUDUNum);
        tvUserInfoRealName = findViewById(R.id.tvUserInfoRealName);
        tvUserInfoPhone = findViewById(R.id.tvUserInfoPhone);
        tvUserInfoSign = findViewById(R.id.tvUserInfoSign);
        tvUserInfoEmail = findViewById(R.id.tvUserInfoEmail);
        tvUserInfoWallet = findViewById(R.id.tvUserInfoWallet);
    }

    //修改头像
    public void changeHeadImg(View view) {
        selectPicPopWindow.showAtLocation(findViewById(android.R.id.content),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //修改昵称
    public void changeNick(View view) {

    }

    //修改性别
    public void changeSex(View view) {

    }

    //我的二维码
    public void myQRCode(View view) {

    }

    //修改个性签名
    public void changeSign(View view) {
        startActivity(new Intent(this, ChangeSignActivity.class));
    }

    //修改Email
    public void changeEmail(View view) {

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
                            .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                            .compose(RxSchedulers.normalTrans())
                            .subscribe(response -> {
                                GlideUtil.loadCornerImg(ivUserInfoHead, url, R.drawable.ic_launcher, CommonUtils.dip2px(this, 2));
                                ToastUtils.showShort("更新头像成功");
                            }, this::handleApiError);
                });
            });
        }
    }
}
