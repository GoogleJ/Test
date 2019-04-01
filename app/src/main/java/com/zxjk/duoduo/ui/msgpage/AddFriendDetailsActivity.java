package com.zxjk.duoduo.ui.msgpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.weight.TitleBar;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 个人信息页面，仿微信的
 */
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
    int intentAddType=0;
    FriendInfoResponse friendInfoResponse;
    FriendInfoResponse newFriend;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        ButterKnife.bind(this);
        initUI();
        int type = 0;
        intentAddType= getIntent().getIntExtra("intentAddType", type);
        friendInfoResponse = (FriendInfoResponse) getIntent().getSerializableExtra("searchAddFriendDetails");
        newFriend = (FriendInfoResponse) getIntent().getSerializableExtra("newFriend");
        if (intentAddType == 0) {
            userName.setText(friendInfoResponse.getNick());
            duduId.setText(friendInfoResponse.getDuoduoId());
            address.setText(friendInfoResponse.getAddress());
            signtureText.setText(friendInfoResponse.getSignature());
            GlideUtil.loadImg(heardImage, friendInfoResponse.getHeadPortrait());
            String sex = "0";
            if (sex.equals(friendInfoResponse.getSex())) {
                genderImage.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
            } else {
                genderImage.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
            }
            return;
        }else if (intentAddType==1){
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
            return;
        }


    }

    protected void initUI() {
        titleBar.getLeftImageView().setOnClickListener(v -> finish());
    }

    @OnClick(R.id.m_personal_information_add_contact_btn)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.m_personal_information_add_contact_btn:
                //添加好友的跳转逻辑
                if (intentAddType==0){
                    intent=new Intent(this,VerificationActivity.class);
                    intent.putExtra("addFriend",friendInfoResponse.getId());
                    startActivity(intent);
                    return;
                }else if (intentAddType==1){
                    intent=new Intent(this,VerificationActivity.class);
                    intent.putExtra("addFriend",newFriend.getId());
                    startActivity(intent);
                    return;
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
