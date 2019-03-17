package com.zxjk.duoduo.ui.minepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

public class UserInfoActivity extends BaseActivity {

    private ImageView ivUserInfoHead;
    private TextView tvUserInfoNick;
    private TextView tvUserInfoSex;
    private TextView tvUserInfoDUDUNum;
    private TextView tvUserInfoRealName;
    private TextView tvUserInfoPhone;
    private TextView tvUserInfoSign;
    private TextView tvUserInfoEmail;
    private TextView tvUserInfoWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

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

}
