package com.zxjk.duoduo.ui.msgpage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.base.ContentActivity;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.weight.TitleBar;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 个人信息页面，仿微信的
 */
public class PersonalInformationActivity extends BaseActivity implements View.OnClickListener {
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

    String userIds;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        ButterKnife.bind(this);
        initUI();
        Intent intent=getIntent();
        userIds= intent.getStringExtra("userId");
        getFriendInfoById(userIds);
    }


    protected void initUI() {

        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.m_personal_information_add_contact_btn)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_personal_information_add_contact_btn:
                Intent intent = new Intent(PersonalInformationActivity.this, VerificationActivity.class);
                intent.putExtra("userIdAddFriend", userIds);
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
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<FriendInfoResponse>() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void accept(FriendInfoResponse data) throws Exception {
                        userName.setText(data.getRealname());
                        duduId.setText(data.getDuoduoId());
                        address.setText(data.getAddress());
                        signtureText.setText(data.getSignature());
                        GlideUtil.loadImg(heardImage,data.getHeadPortrait());
                        String sex="0";
                        if (sex.equals(data.getSex())){
                            genderImage.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
                        }else{
                            genderImage.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
                        }





                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.d("DEBUG", throwable.getMessage());
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
