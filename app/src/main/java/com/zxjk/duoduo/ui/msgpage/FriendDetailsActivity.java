package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.response.FriendListResponse;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.response.SearchCustomerInfoResponse;
import com.zxjk.duoduo.network.response.SearchResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.widget.CommonPopupWindow;
import com.zxjk.duoduo.ui.msgpage.widget.dialog.DeleteFriendInformationDialog;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.weight.TitleBar;

import java.io.Serializable;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import io.rong.imkit.RongIM;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 个人信息详情页，包含删除的dialog等部分
 */
@SuppressLint("CheckResult")
public class FriendDetailsActivity extends BaseActivity implements View.OnClickListener, CommonPopupWindow.ViewInterface {
    /**
     * 标题布局
     */
    @BindView(R.id.m_people_information_title_bar)
    TitleBar titleBar;
    /**
     * 发送消息按钮
     */
    @BindView(R.id.m_people_information_send_to_message)
    TextView sendMsgBtn;
    /**
     * 语音通话按钮
     */
    @BindView(R.id.m_people_information_voice_calls)
    TextView voiceBtn;
    /**
     * 头像
     */
    @BindView(R.id.m_personal_information_icon)
    ImageView heardIcon;
    /**
     * 用户名
     */
    @BindView(R.id.m_personal_information_user_name_text)
    TextView userNameText;
    /**
     * 性别图标
     */
    @BindView(R.id.m_personal_information_gender_icon)
    ImageView genderIcon;
    /**
     * 多多账号
     */
    @BindView(R.id.m_personal_information_dudu_id)
    TextView duoduoId;
    /**
     * 地区
     */
    @BindView(R.id.m_personal_information_address)
    TextView areaText;
    /**
     * 电话
     */
    @BindView(R.id.m_people_information_phone_text)
    TextView phoneText;
    /**
     * 邮箱地址
     */
    @BindView(R.id.m_people_information_email_text)
    TextView emailText;
    /**
     * 个性签名
     */
    @BindView(R.id.m_people_information_signature_text)
    TextView sinatureText;
    /**
     * 钱包地址
     */
    @BindView(R.id.m_people_information_wallet_address_text)
    TextView walletAddressText;
    /**
     * copy按钮
     */
    @BindView(R.id.m_people_information_copy_wallet_address)
    ImageView copyBtn;

    private SearchResponse user;
    /**
     * 0是男1是女
     */
    String sex = "0";

    private CommonPopupWindow popupWindow;

    DeleteFriendInformationDialog dialog;
    SearchResponse searchResponse;
    SearchCustomerInfoResponse searchCustomerInfoResponse;
    FriendListResponse friendListResponse;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_information);
        ButterKnife.bind(this);

        initUI();
        int searchDetailsType = 0;
        int type;//接受类型

        searchResponse = (SearchResponse) getIntent().getSerializableExtra("globalUserDetails");
        type=getIntent().getIntExtra("searchDetailsType",searchDetailsType);
        searchCustomerInfoResponse= (SearchCustomerInfoResponse) getIntent().getSerializableExtra("searchFriendDetails");

        if (1==type){
            //这个模块需要添加数据绑定
            GlideUtil.loadCornerImg(heardIcon, searchCustomerInfoResponse.getHeadPortrait(), 2);
            userNameText.setText(searchCustomerInfoResponse.getRealname());
            duoduoId.setText(searchCustomerInfoResponse.getDuoduoId());
            areaText.setText(searchCustomerInfoResponse.getAddress());
            phoneText.setText(searchCustomerInfoResponse.getMobile());
            emailText.setText(searchCustomerInfoResponse.getEmail());
            sinatureText.setText(searchCustomerInfoResponse.getSignature());
            walletAddressText.setText(searchCustomerInfoResponse.getWalletAddress());
            if (sex.equals(searchCustomerInfoResponse.getSex())) {
                //男
                genderIcon.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
            } else {
                genderIcon.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
            }
        }else if ( 0==type){
            //这个模块需要添加数据绑定
            GlideUtil.loadCornerImg(heardIcon, searchResponse.getHeadPortrait(), 2);
            userNameText.setText(searchResponse.getRealname());
            duoduoId.setText(searchResponse.getDuoduoId());
            areaText.setText(searchResponse.getAddress());
            phoneText.setText(searchResponse.getMobile());
            emailText.setText(searchResponse.getEmail());
            sinatureText.setText(searchResponse.getSignature());
            walletAddressText.setText(searchResponse.getWalletAddress());
            if (sex.equals(searchResponse.getSex())) {
                //男
                genderIcon.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
            } else {
                genderIcon.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
            }
        }else{
            friendListResponse= (FriendListResponse) getIntent().getSerializableExtra("friendListResponse");
            GlideUtil.loadCornerImg(heardIcon, friendListResponse.getHeadPortrait(), 2);
            userNameText.setText(friendListResponse.getRealname());
            duoduoId.setText(friendListResponse.getDuoduoId());
            areaText.setText(friendListResponse.getAddress());
            phoneText.setText(friendListResponse.getMobile());
            emailText.setText(friendListResponse.getEmail());
            sinatureText.setText(friendListResponse.getSignature());
            walletAddressText.setText(friendListResponse.getWalletAddress());
            if (sex.equals(friendListResponse.getSex())) {
                //男
                genderIcon.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
            } else {
                genderIcon.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
            }
        }


    }

    private void initUI() {
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    return;
                }
                popupWindow = new CommonPopupWindow.Builder(FriendDetailsActivity.this)
                        .setView(R.layout.popup_window_people_information)
                        .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setAnimationStyle(R.style.AnimDown)
                        .setBackGroundLevel(0.5f)
                        .setViewOnclickListener(FriendDetailsActivity.this::getChildView)
                        .setOutsideTouchable(true)
                        .create();
                popupWindow.showAsDropDown(titleBar.getRightImageView());
            }
        });
    }

    @OnClick({R.id.m_people_information_send_to_message, R.id.m_people_information_voice_calls})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_people_information_send_to_message:
                RongIM.getInstance().startPrivateChat(this, "31", "标题");
                break;
            case R.id.m_people_information_voice_calls:
                ToastUtils.showShort("此功能暂未实现");
                break;
            case R.id.update_rename:
                Intent intent = new Intent(FriendDetailsActivity.this, ModifyNotesActivity.class);
                intent.putExtra("peopelUserId", user.getId());
                startActivity(intent);
                break;
            case R.id.recommend_to_friend:
                ToastUtils.showShort("暂未实现");
                break;
            case R.id.delete_friend:
                dialog = new DeleteFriendInformationDialog(this);
                dialog.setOnClickListener(() -> deleteFriend(user.getId()));
                dialog.show();
                break;
            default:
                break;
        }
    }
    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.popup_window_people_information:
                view.findViewById(R.id.update_rename).setOnClickListener(this);
                view.findViewById(R.id.recommend_to_friend).setOnClickListener(this);
                view.findViewById(R.id.delete_friend).setOnClickListener(this);
                break;
                default:
                    break;
        }
    }
    public void deleteFriend(String friendId) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .deleteFriend(friendId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ToastUtils.showShort("删除成功");
                        dialog.dismiss();
                    }
                }, this::handleApiError);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
