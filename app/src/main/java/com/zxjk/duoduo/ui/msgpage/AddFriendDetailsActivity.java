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
import com.zxjk.duoduo.ui.widget.TitleBar;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("CheckResult")
public class AddFriendDetailsActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.m_information_title_bar)
    TitleBar titleBar;
    @BindView(R.id.m_personal_information_user_name_text)
    TextView userName;
    @BindView(R.id.m_personal_information_dudu_id)
    TextView duduId;
    @BindView(R.id.m_personal_information_address)
    TextView address;
    @BindView(R.id.m_personal_information_signature_text)
    TextView signtureText;
    @BindView(R.id.m_personal_information_add_contact_btn)
    TextView addContact;
    @BindView(R.id.m_personal_information_icon)
    ImageView heardImage;
    @BindView(R.id.m_personal_information_gender_icon)
    ImageView genderImage;

    Intent intent;
    FriendInfoResponse newFriend;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        ButterKnife.bind(this);
        initUI();

        newFriend = (FriendInfoResponse) getIntent().getSerializableExtra("newFriend");
        if (newFriend == null) {
            String newFriendId = getIntent().getStringExtra("newFriendId");
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getCustomerInfoById(newFriendId)
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .subscribe(response -> {
                        userName.setText(response.getNick());
                        duduId.setText(response.getDuoduoId());
                        address.setText(response.getAddress());
                        signtureText.setText(TextUtils.isEmpty(response.getSignature()) ? getString(R.string.none) : response.getSignature());
                        GlideUtil.loadCornerImg(heardImage, response.getHeadPortrait(), 3);
                        if ("0".equals(response.getSex())) {
                            genderImage.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
                        } else {
                            genderImage.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
                        }
                    }, this::handleApiError);
        } else {
            userName.setText(newFriend.getNick());
            duduId.setText(newFriend.getDuoduoId());
            address.setText(newFriend.getAddress());
            signtureText.setText(newFriend.getSignature());
            GlideUtil.loadImg(heardImage, newFriend.getHeadPortrait());
            String sex = "0";
            if (sex.equals(newFriend.getSex())) {
                genderImage.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
            } else {
                genderImage.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
            }
        }
    }

    protected void initUI() {
        boolean fromwaiting = getIntent().getBooleanExtra("fromwaiting", false);
        if (fromwaiting) {
            addContact.setText(R.string.tongguoyanzheng);
        }
        titleBar.getLeftImageView().setOnClickListener(v -> finish());
    }

    @OnClick(R.id.m_personal_information_add_contact_btn)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.m_personal_information_add_contact_btn:
                //添加好友的跳转逻辑
                intent = new Intent(this, VerificationActivity.class);
                intent.putExtra("addFriend", newFriend == null ? getIntent().getStringExtra("newFriendId")
                        : newFriend.getId());
                intent.putExtra("intentType", 1);
                startActivity(intent);
            default:
        }
    }
}
