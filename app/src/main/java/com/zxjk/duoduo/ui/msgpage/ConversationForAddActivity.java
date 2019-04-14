package com.zxjk.duoduo.ui.msgpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.ui.widget.TitleBar;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Administrator
 * @// TODO: 2019\4\5 0005 从名片跳转到添加好友的详情模块
 */

public class ConversationForAddActivity extends BaseActivity implements View.OnClickListener {

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

    String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_for_add);
        ButterKnife.bind(this);
        userId = getIntent().getStringExtra("friendInfoResponses");
        getFriendInfoById(userId);
    }


    @OnClick(R.id.m_personal_information_add_contact_btn)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_personal_information_add_contact_btn:
                Intent intent = new Intent(this, VerificationActivity.class);
                intent.putExtra("ConversationForAdd", userId);
                intent.putExtra("intentType", 0);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void getFriendInfoById(String friendId) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendInfoById(friendId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendInfoResponse -> {
                    titleBar.getLeftImageView().setOnClickListener(v -> finish());
                    userName.setText(friendInfoResponse.getNick());
                    duduId.setText(friendInfoResponse.getDuoduoId());
                    address.setText(friendInfoResponse.getAddress());
                    signtureText.setText(friendInfoResponse.getSignature());
                    GlideUtil.loadCornerImg(heardImage, friendInfoResponse.getHeadPortrait(), 2);
                    if ("0".equals(friendInfoResponse.getSex())) {
                        genderImage.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
                    } else {
                        genderImage.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
                    }
                });

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
