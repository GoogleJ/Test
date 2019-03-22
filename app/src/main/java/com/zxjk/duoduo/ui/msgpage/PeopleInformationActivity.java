package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.widget.CommonPopupWindow;
import com.zxjk.duoduo.ui.msgpage.widget.dialog.DeleteFriendInformationDialog;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.weight.TitleBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 个人信息详情页，包含删除的dialog等部分
 */
public class PeopleInformationActivity extends BaseActivity implements View.OnClickListener, CommonPopupWindow.ViewInterface {
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
    String userId;
    String newFriendUserId;
    /**
     * 0是男1是女
     */
    String sex = "0";

    private CommonPopupWindow popupWindow;

    DeleteFriendInformationDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_information);
        ButterKnife.bind(this);
        initUI();
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        getFriendInfoInfoById(userId);
        Intent intent1=getIntent();
        newFriendUserId=intent1.getStringExtra("peopleInformatinoUserId");
        getFriendInfoInfoById(newFriendUserId);
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
                popupWindow = new CommonPopupWindow.Builder(PeopleInformationActivity.this)
                        .setView(R.layout.popup_window_people_information)
                        .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setAnimationStyle(R.style.AnimDown)
                        .setBackGroundLevel(0.5f)
                        .setViewOnclickListener(PeopleInformationActivity.this::getChildView)
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
                ToastUtils.showShort("此功能暂未实现");
                break;
            case R.id.m_people_information_voice_calls:
                ToastUtils.showShort("此功能暂未实现");
                break;
            case R.id.update_rename:
                Intent intent=new Intent(PeopleInformationActivity.this,ModifyNotesActivity.class);
                intent.putExtra("peopelUserId",userId);
                startActivity(intent);
                break;
            case R.id.recommend_to_friend:
                ToastUtils.showShort("暂未实现");
                break;
            case R.id.delete_friend:
                dialog = new DeleteFriendInformationDialog(this);
                dialog.show();
                dialog.setOnClickListener(new DeleteFriendInformationDialog.OnClickListener() {
                    @Override
                    public void onDel() {
//                        deleteFriend(userId);
                    }
                });
                break;
            default:
                break;
        }
    }

    @SuppressLint("CheckResult")
    public void getFriendInfoInfoById(String friendId) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendInfoById(friendId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<FriendInfoResponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void accept(FriendInfoResponse data) throws Exception {

                        //这个模块需要添加数据绑定
                        GlideUtil.loadImg(heardIcon, data.getHeadPortrait());
                        userNameText.setText(data.getRealname());
                        duoduoId.setText(data.getDuoduoId());
                        areaText.setText(data.getAddress());
                        phoneText.setText(data.getMobile());
                        emailText.setText(data.getEmail());
                        sinatureText.setText(data.getSignature());
                        walletAddressText.setText(data.getWalletAddress());
                        if (sex.equals(data.getSex())) {
                            //男
                            genderIcon.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
                        } else {
                            genderIcon.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
                        }

                    }
                }, this::handleApiError);
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.popup_window_people_information:
                view.findViewById(R.id.update_rename).setOnClickListener(this);
                view.findViewById(R.id.recommend_to_friend).setOnClickListener(this);
                view.findViewById(R.id.delete_friend).setOnClickListener(this);
                break;
        }

    }

//    public void deleteFriend(String friendId) {
//        ServiceFactory.getInstance().getBaseService(Api.class)
//                .deleteFriend(friendId)
//                .compose(bindToLifecycle())
//                .compose(RxSchedulers.ioObserver())
//                .compose(RxSchedulers.normalTrans())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        LogUtils.d("DEBUG", s);
//                        ToastUtils.showShort("删除成功");
//                        dialog.dismiss();
//
//                    }
//                }, this::handleApiError);
//
//    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
