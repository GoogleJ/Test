package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.HomeActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.widget.CommonPopupWindow;
import com.zxjk.duoduo.ui.msgpage.widget.dialog.DeleteFriendInformationDialog;
import com.zxjk.duoduo.ui.widget.TitleBar;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

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


    /**
     * 0是男1是女
     */
    String sex = "0";

    private CommonPopupWindow popupWindow;

    DeleteFriendInformationDialog dialog;
    FriendInfoResponse friendInfoResponse;
    FriendInfoResponse friendInfo;
    FriendInfoResponse contactResponse;
    int intentType = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_information);
        ButterKnife.bind(this);
        initUI();

        initFriendIntent();

    }

    private void initFriendIntent() {
        //这个模块需要添加数据绑定
        intentType = getIntent().getIntExtra("intentType", 0);
        friendInfoResponse = (FriendInfoResponse) getIntent().getSerializableExtra("searchFriendDetails");
        friendInfo = (FriendInfoResponse) getIntent().getSerializableExtra("globalSearchFriendDetails");
        contactResponse = (FriendInfoResponse) getIntent().getSerializableExtra("contactResponse");

        if (intentType == 0 && friendInfoResponse == null) {
            String friendId = getIntent().getStringExtra("friendId");
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getFriendInfoById(friendId)
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .compose(RxSchedulers.normalTrans())
                    .subscribe(r -> {
                        friendInfoResponse = r;
                        handData();
                    }, this::handleApiError);
            return;
        }
        handData();
    }

    private void handData() {
        if (intentType == 0) {
            if (friendInfoResponse.getId().equals(Constant.userId)) {
                titleBar.setRightImageVisibility(View.GONE);
            }
            GlideUtil.loadCornerImg(heardIcon, friendInfoResponse.getHeadPortrait(), 2);
            userNameText.setText(friendInfoResponse.getNick());
            duoduoId.setText(friendInfoResponse.getDuoduoId());
            areaText.setText(friendInfoResponse.getAddress());
            phoneText.setText(friendInfoResponse.getMobile());
            emailText.setText(friendInfoResponse.getEmail());
            sinatureText.setText(friendInfoResponse.getSignature());
            walletAddressText.setText(friendInfoResponse.getWalletAddress());
            if (sex.equals(friendInfoResponse.getSex())) {
                //男
                genderIcon.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
            } else {
                genderIcon.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
            }
        } else if (intentType == 1) {
            if (friendInfo.getId().equals(Constant.userId)) {
                titleBar.setRightImageVisibility(View.GONE);
            }
            GlideUtil.loadCornerImg(heardIcon, friendInfo.getHeadPortrait(), 2);
            userNameText.setText(friendInfo.getNick());
            duoduoId.setText(friendInfo.getDuoduoId());
            areaText.setText(friendInfo.getAddress());
            phoneText.setText(friendInfo.getMobile());
            emailText.setText(friendInfo.getEmail());
            sinatureText.setText(friendInfo.getSignature());
            walletAddressText.setText(friendInfo.getWalletAddress());
            if (sex.equals(friendInfo.getSex())) {
                //男
                genderIcon.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
            } else {
                genderIcon.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
            }
        } else {
            if (contactResponse.getId().equals(Constant.userId)) {
                titleBar.setRightImageVisibility(View.GONE);
            }
            GlideUtil.loadCornerImg(heardIcon, contactResponse.getHeadPortrait(), 2);
            userNameText.setText(contactResponse.getNick());
            duoduoId.setText(contactResponse.getDuoduoId());
            areaText.setText(contactResponse.getAddress());
            phoneText.setText(contactResponse.getMobile());
            emailText.setText(contactResponse.getEmail());
            sinatureText.setText(contactResponse.getSignature());
            walletAddressText.setText(contactResponse.getWalletAddress());
            if (sex.equals(contactResponse.getSex())) {
                //男
                genderIcon.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
            } else {
                genderIcon.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
            }
        }
    }

    private void initUI() {
        titleBar.getLeftImageView().setOnClickListener(v -> finish());

        titleBar.getRightImageView().setOnClickListener(v -> {
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
        });
    }

    @OnClick({R.id.m_people_information_send_to_message})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_people_information_send_to_message:
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                if (intentType == 0) {
                    RongIM.getInstance().startPrivateChat(this, friendInfoResponse.getId(), friendInfoResponse.getNick());
                } else if (intentType == 1) {
                    RongIM.getInstance().startPrivateChat(this, friendInfo.getId(), friendInfo.getNick());
                } else {
                    RongIM.getInstance().startPrivateChat(this, contactResponse.getId(), contactResponse.getNick());
                }
                break;
            case R.id.update_rename:
                Intent intent1 = new Intent(FriendDetailsActivity.this, ModifyNotesActivity.class);
                String id;
                if (intentType == 0) {
                    id = friendInfoResponse.getId();
                } else if (intentType == 1) {
                    id = friendInfo.getId();
                } else {
                    id = contactResponse.getId();
                }
                intent1.putExtra("friendId", id);
                startActivity(intent1);
                break;
            case R.id.recommend_to_friend:
                Intent intentCard = new Intent(FriendDetailsActivity.this, SelectContactForCardActivity.class);
                if (intentType == 0) {
                    intentCard.putExtra("userType", 1);
                    intentCard.putExtra("intentType", 1);
                    intentCard.putExtra("friendInfoResponseId", friendInfoResponse.getId());
                    startActivity(intentCard);
                    return;
                } else if (intentType == 1) {
                    intentCard.putExtra("userType", 2);
                    intentCard.putExtra("intentType", 1);
                    intentCard.putExtra("friendInfoId", friendInfo.getId());
                    startActivity(intentCard);
                } else {
                    intentCard.putExtra("userType", 3);
                    intentCard.putExtra("intentType", 1);
                    intentCard.putExtra("contactResponseId", contactResponse.getId());
                    startActivity(intentCard);
                }
                break;
            case R.id.delete_friend:
                dialog = new DeleteFriendInformationDialog(this);
                dialog.setOnClickListener(() -> {
                    if (intentType == 0) {
                        FriendDetailsActivity.this.deleteFriend(friendInfoResponse.getId());
                    } else if (intentType == 1) {
                        FriendDetailsActivity.this.deleteFriend(friendInfo.getId());
                    } else {
                        FriendDetailsActivity.this.deleteFriend(contactResponse.getId());
                    }
                    dialog.dismiss();
                    FriendDetailsActivity.this.finish();
                });

                if (intentType == 0) {
                    dialog.show(friendInfoResponse.getNick());
                } else if (intentType == 1) {
                    dialog.show(friendInfo.getNick());
                } else {
                    dialog.show(contactResponse.getNick());
                }
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
                .subscribe(s -> {
                    ToastUtils.showShort(getString(R.string.the_friend_has_been_deleted));
                    dialog.dismiss();
                }, this::handleApiError);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }
}
