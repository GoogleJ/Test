package com.zxjk.duoduo.ui.msgpage;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.response.SearchCustomerInfoResponse;
import com.zxjk.duoduo.network.response.SearchResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
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


    private PopupWindow mPopupWindow;

    Intent intent;

    SearchResponse searchResponse;
    SearchCustomerInfoResponse searchCustomerInfoResponse;
    int searchAddType;
    //这个是接收类型
    int type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        ButterKnife.bind(this);
        initUI();


        searchResponse = (SearchResponse) getIntent().getSerializableExtra("globalAddUserDetails");
        searchCustomerInfoResponse = (SearchCustomerInfoResponse) getIntent().getSerializableExtra("searchAddFriendDetails");
        type = getIntent().getIntExtra("searchAddType", searchAddType);
        if (type == 1) {
            userName.setText(searchCustomerInfoResponse.getRealname());
            duduId.setText(searchCustomerInfoResponse.getDuoduoId());
            address.setText(searchCustomerInfoResponse.getAddress());
            signtureText.setText(searchCustomerInfoResponse.getSignature());
            GlideUtil.loadImg(heardImage, searchCustomerInfoResponse.getHeadPortrait());
            String sex = "0";
            if (sex.equals(searchCustomerInfoResponse.getSex())) {
                genderImage.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
            } else {
                genderImage.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
            }
        } else {
            userName.setText(searchResponse.getRealname());
            duduId.setText(searchResponse.getDuoduoId());
            address.setText(searchResponse.getAddress());
            signtureText.setText(searchResponse.getSignature());
            GlideUtil.loadImg(heardImage, searchResponse.getHeadPortrait());
            String sex = "0";
            if (sex.equals(searchResponse.getSex())) {
                genderImage.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
            } else {
                genderImage.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
            }
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
                String searchAddUserId=getIntent().getStringExtra("searchAddUserId");
                String searchUserId=getIntent().getStringExtra("searchUserId");

                if (type == 1) {
                    intent = new Intent(this, VerificationActivity.class);
                    intent.putExtra("searchAddFriendId", searchAddUserId);
                    intent.putExtra("addFriendType",1);
                    startActivity(intent);
                }else{
                    intent=new Intent(this,VerificationActivity.class);
                    intent.putExtra("globalAddUserId",searchUserId);
                    intent.putExtra("addFriendType",0);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }
}
