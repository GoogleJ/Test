package com.zxjk.duoduo.ui.msgpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.weight.TitleBar;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 个人信息详情页，包含删除的dialog等部分
 */
public class PeopleInformationActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.m_people_information_title_bar)
    TitleBar titleBar;
    @BindView(R.id.m_people_information_send_to_message)
    TextView sendMsgBtn;
    @BindView(R.id.m_people_information_voice_calls)
    TextView voiceBtn;
    @BindView(R.id.m_personal_information_icon)
    ImageView heardIcon;
    @BindView(R.id.m_personal_information_user_name_text)
    TextView userNameText;
    @BindView(R.id.m_personal_information_gender_icon)
    ImageView genderIcon;
    @BindView(R.id.m_personal_information_dudu_id)
    TextView duoduoId;
    @BindView(R.id.m_personal_information_address)
    TextView areaText;
    @BindView(R.id.m_people_information_phone_text)
    TextView phoneText;
    @BindView(R.id.m_people_information_email_text)
    TextView emailText;
    @BindView(R.id.m_people_information_signature_text)
    TextView sinatureText;
    @BindView(R.id.m_people_information_wallet_address_text)
    TextView walletAddressText;
    @BindView(R.id.m_people_information_copy_wallet_address)
    ImageView copyBtn;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_information);
        ButterKnife.bind(this);
        initUI();
        Intent intent=getIntent();
        String userId=intent.getStringExtra("peopleInformatinoUserId");
        getFriendInfoInfoById(userId);
    }

    private void initUI() {
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.m_people_information_send_to_message,R.id.m_people_information_voice_calls})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.m_people_information_send_to_message:
                ToastUtils.showShort("此功能暂未实现");
                break;
            case R.id.m_people_information_voice_calls:
                ToastUtils.showShort("此功能暂未实现");
                break;
        }
    }
    public void getFriendInfoInfoById(String friendId){
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendInfoById(friendId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<FriendInfoResponse>() {
                    @Override
                    public void accept(FriendInfoResponse friendInfoResponse) throws Exception {
                        ToastUtils.showShort("DEBUG",friendInfoResponse.toString());
                        //这个模块需要添加数据绑定

                    }
                },this::handleApiError);

    }
}
