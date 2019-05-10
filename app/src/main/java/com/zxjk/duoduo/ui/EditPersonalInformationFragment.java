package com.zxjk.duoduo.ui;

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
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.dialog.BottomDialog;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.utils.OssUtils;
import com.zxjk.duoduo.utils.TakePicUtil;

import java.io.File;
import java.util.Collections;

import butterknife.OnClick;

/**
 * author L
 * create at 2019/5/7
 * description: 编辑个人信息
 */
public class EditPersonalInformationFragment extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_TAKE = 1;
    public static final int REQUEST_ALBUM = 2;

    /**
     * 这是关于点击完成按钮的控件
     */
    TextView commitBtn;
    /**
     * 这是关于点击图片，选择头像的按钮
     */
    ImageView imageSearchBtn;
    /**
     * 这里是输入昵称的输入框
     */
    EditText editNickName;
    /**
     * 这里是输入地区的输入框
     */
    EditText editArea;
    private String url;
    private LoginResponse update;

    private void initData() {
    }

    private void initView() {
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.m_edit_information_title_bar));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        commitBtn = findViewById(R.id.m_edit_information_btn);
        imageSearchBtn = findViewById(R.id.m_edit_information_header_icon);
        editNickName = findViewById(R.id.m_edit_information_name_edit);
        editArea = findViewById(R.id.m_edit_information_area_edit);


        imageSearchBtn.setOnClickListener(this);
        getPermisson(imageSearchBtn, granted -> {
            BottomDialog.dialogType(this, REQUEST_TAKE, REQUEST_ALBUM);

        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

        commitBtn.setOnClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_information);
        initView();
        initData();
    }

    @OnClick({R.id.m_edit_information_btn, R.id.m_edit_information_header_icon})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_edit_information_btn:
                if (TextUtils.isEmpty(url)) {
                    ToastUtils.showShort(getString(R.string.please_upload_an_avatar));
                    return;
                }
                if (TextUtils.isEmpty(editNickName.getText().toString())) {
                    ToastUtils.showShort(getString(R.string.username_can_not_be_blank));
                    return;
                }
                if (TextUtils.isEmpty(editArea.getText().toString())) {
                    ToastUtils.showShort(getString(R.string.area_can_not_be_empty));
                    return;
                }

                update = new LoginResponse(Constant.userId);
                update.setHeadPortrait(url);
                update.setNick(editNickName.getText().toString());
                update.setAddress(editArea.getText().toString());

                updateCustomerInfo(GsonUtils.toJson(update));

                break;
            default:
        }
    }

    /**
     * 把用户选择好的图片显示在ImageView上
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    break;
            }
        }
        if (!TextUtils.isEmpty(filePath)) {
            zipFile(Collections.singletonList(filePath), files -> {
                File file = files.get(0);
                OssUtils.uploadFile(file.getAbsolutePath(), url -> {
                    LoginResponse update = new LoginResponse(Constant.userId);
                    update.setHeadPortrait(url);
                    GlideUtil.loadCornerImg(imageSearchBtn, url, 3);
                    this.url = url;
                });
            });
        }

    }


    @SuppressLint("CheckResult")
    public void updateCustomerInfo(String customerInfo) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .updateUserInfo(customerInfo)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(response -> {
                    GlideUtil.loadCornerImg(imageSearchBtn, url, 3);
                    Constant.currentUser.setHeadPortrait(update.getHeadPortrait());
                    Constant.currentUser.setNick(update.getNick());
                    Constant.currentUser.setAddress(update.getAddress());

                    Intent intent = new Intent(EditPersonalInformationFragment.this, SetUpPaymentPwdActivity.class);
                    intent.putExtra("firstLogin", true);
                    startActivity(intent);
                    finish();
                }, this::handleApiError);
    }
}
