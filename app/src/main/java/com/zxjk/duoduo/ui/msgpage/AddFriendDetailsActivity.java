package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author L
 * create at 2019/5/10
 * description: 个人信息【添加到通讯录】
 */
@SuppressLint("CheckResult")
public class AddFriendDetailsActivity extends BaseActivity {
    Intent intent;
    FriendInfoResponse newFriend;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.iv_gender)
    ImageView ivGender;
    @BindView(R.id.iv_headPortrait)
    ImageView ivHeadPortrait;
    @BindView(R.id.tv_DuoDuoNumber)
    TextView tvDuoDuoNumber;
    @BindView(R.id.tv_district)
    TextView tvDistrict;
    @BindView(R.id.tv_signature)
    TextView tvSignature;
    @BindView(R.id.tv_addAddressBook)
    TextView tvAddAddressBook;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText(getString(R.string.personal_details));
        boolean fromwaiting = getIntent().getBooleanExtra("fromwaiting", false);
        if (fromwaiting) {
            tvAddAddressBook.setText(R.string.tongguoyanzheng);
        }
        newFriend = (FriendInfoResponse) getIntent().getSerializableExtra("newFriend");
        if (newFriend == null) {
            String newFriendId = getIntent().getStringExtra("newFriendId");
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getCustomerInfoById(newFriendId)
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .subscribe(response -> {
                        tvNickname.setText(response.getNick());
                        tvDuoDuoNumber.setText(getString(R.string.duoduo_acount) + " " + response.getDuoduoId());
                        tvDistrict.setText(getString(R.string.district) + " " + response.getAddress());
                        tvSignature.setText(TextUtils.isEmpty(response.getSignature()) ? getString(R.string.none) : response.getSignature());
                        GlideUtil.loadCornerImg(ivHeadPortrait, response.getHeadPortrait(), 5);
                        if ("0".equals(response.getSex())) {
                            ivGender.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
                        } else {
                            ivGender.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
                        }
                    }, this::handleApiError);
        } else {
            tvNickname.setText(newFriend.getNick());
            tvDuoDuoNumber.setText(getString(R.string.duoduo_acount) + " " + newFriend.getDuoduoId());
            tvDistrict.setText(getString(R.string.district) + " " + newFriend.getAddress());
            tvSignature.setText(newFriend.getSignature());
            GlideUtil.loadCornerImg(ivHeadPortrait, newFriend.getHeadPortrait(), 5);
            String sex = "0";
            if (sex.equals(newFriend.getSex())) {
                ivGender.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
            } else {
                ivGender.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
            }
        }

    }

    @OnClick({R.id.rl_back, R.id.tv_addAddressBook})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_addAddressBook:
                intent = new Intent(this, VerificationActivity.class);
                intent.putExtra("addFriend", newFriend == null ? getIntent().getStringExtra("newFriendId")
                        : newFriend.getId());
                intent.putExtra("intentType", 1);
                startActivity(intent);
                break;
        }
    }
}
