package com.zxjk.duoduo.ui;

import android.Manifest;
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
import com.liang.permission.annotation.Permission;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.response.UpdateCustomerInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.base.ContentActivity;
import com.zxjk.duoduo.ui.widget.TakePopWindow;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.utils.OssUtils;
import com.zxjk.duoduo.utils.TakePicUtil;
import com.zxjk.duoduo.weight.TitleBar;

import java.io.File;
import java.util.Collections;

import androidx.annotation.Nullable;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

import static com.zxjk.duoduo.utils.PermissionUtils.verifyStoragePermissions;

/**
 * @author Administrator
 * @// TODO: 2019\3\18 0018 个人实名界面
 */
public class EditPersonalInformationFragment extends BaseActivity implements View.OnClickListener, TakePopWindow.OnItemClickListener {

    public static final int REQUEST_TAKE = 1;
    public static final int REQUEST_ALBUM = 2;

    /**
     * 这是关于标题栏的控件
     */
    TitleBar titleBar;
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
    private TakePopWindow selectPicPopWindow;

    private void initData() {
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        titleBar = findViewById(R.id.m_edit_information_title_bar);
        commitBtn = findViewById(R.id.m_edit_information_btn);
        imageSearchBtn = findViewById(R.id.m_edit_information_header_icon);
        editArea = findViewById(R.id.m_edit_information_name_edit);
        editNickName = findViewById(R.id.m_edit_information_area_edit);
        selectPicPopWindow = new TakePopWindow(this);
        selectPicPopWindow.setOnItemClickListener(this);
        imageSearchBtn.setOnClickListener(this);
        commitBtn.setOnClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_personal_information);
        initView();
        initData();
    }

    @Permission({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    @OnClick(R.id.m_edit_information_btn)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_edit_information_btn:
                if (editNickName.getText().toString().isEmpty()) {
                    ToastUtils.showShort("昵称不能为空");
                    return;
                }
                if (editArea.getText().toString().isEmpty()) {
                    ToastUtils.showShort("地区不能为空");
                    return;
                }
                LoginResponse update = new LoginResponse(Constant.userId);
                update.setHeadPortrait(url);
                update.setNick(editNickName.getText().toString());
                update.setAddress(editArea.getText().toString());

                updateCustomerInfo(GsonUtils.toJson(update));

                break;
            case R.id.m_edit_information_header_icon:
                verifyStoragePermissions(this);

                selectPicPopWindow.showAtLocation(this.findViewById(android.R.id.content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                break;
            default:
                break;
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
            }
        }

        if (!TextUtils.isEmpty(filePath)) {
            zipFile(Collections.singletonList(filePath), files -> {
                File file = files.get(0);
                OssUtils.uploadFile(file.getAbsolutePath(), url -> {
                    GlideUtil.loadCornerImg(imageSearchBtn, url, R.drawable.ic_launcher, CommonUtils.dip2px(this, 2));
                    LoginResponse update = new LoginResponse(Constant.userId);
                    update.setHeadPortrait(url);
                    GlideUtil.loadCornerImg(imageSearchBtn, url, R.drawable.ic_launcher, CommonUtils.dip2px(this, 2));

                    this.url = url;

                });
            });
        }

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

    public void updateCustomerInfo(String customerInfo) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .updateUserInfo(customerInfo)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<UpdateCustomerInfoResponse>() {
                    @Override
                    public void accept(UpdateCustomerInfoResponse response) throws Exception {
                        GlideUtil.loadCornerImg(imageSearchBtn, url, R.drawable.ic_launcher, CommonUtils.dip2px(EditPersonalInformationFragment.this, 2));
                        ToastUtils.showShort("更新头像成功");
                        startActivity(new Intent(EditPersonalInformationFragment.this, SetUpPaymentPwdFragment.class));
                    }
                }, this::handleApiError);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}