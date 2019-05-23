package com.zxjk.duoduo.ui.msgpage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.dialog.BottomDialog;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.utils.OssUtils;
import com.zxjk.duoduo.utils.TakePicUtil;

import java.io.File;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
            zipFile(Collections.singletonList(path), files -> {
                File file = files.get(0);
                OssUtils.uploadFile(file.getAbsolutePath(), url -> {
                    if (currentPictureFlag == 1) {
                        frontUrl = url;
                        ivDefault1.setVisibility(View.GONE);
                        GlideUtil.loadCornerImg(ivCardFront, url, 5);
                        rlFront.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (currentPictureFlag == 2) {
                        reverseUrl = url;
                        ivDefault2.setVisibility(View.GONE);
                        GlideUtil.loadCornerImg(ivCardReverse, url, 5);
                        rlReverse.setVisibility(View.VISIBLE);
                    }

                });
            });
        }

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
                break;
        }
    }
}
