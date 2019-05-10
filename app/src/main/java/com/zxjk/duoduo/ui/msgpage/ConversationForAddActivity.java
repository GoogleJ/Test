package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
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
 * description: 个人信息  添加到通讯录
 */
@SuppressLint({"CheckResult", "SetTextI18n"})
public class ConversationForAddActivity extends BaseActivity {


    String userId;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_for_add);
        ButterKnife.bind(this);
        userId = getIntent().getStringExtra("friendInfoResponses");
        tvTitle.setText(getString(R.string.personal_details));
        getFriendInfoById();
    }


    public void getFriendInfoById() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendInfoById(userId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendInfoResponse -> {
                    tvNickname.setText(friendInfoResponse.getNick());
                    tvDuoDuoNumber.setText(getString(R.string.duoduo_acount) + " " + friendInfoResponse.getDuoduoId());
                    tvDistrict.setText(getString(R.string.district) + " " + friendInfoResponse.getAddress());
                    tvSignature.setText(friendInfoResponse.getSignature());
                    GlideUtil.loadCornerImg(ivHeadPortrait, friendInfoResponse.getHeadPortrait(), 3);
                    if ("0".equals(friendInfoResponse.getSex())) {
                        ivGender.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
                    } else {
                        ivGender.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
                    }
                });

    }


    @OnClick({R.id.rl_back, R.id.tv_addAddressBook})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_addAddressBook:
                Intent intent = new Intent(this, VerificationActivity.class);
                intent.putExtra("ConversationForAdd", userId);
                intent.putExtra("intentType", 0);
                startActivity(intent);
                break;
        }
    }
}
