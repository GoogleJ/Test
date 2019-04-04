package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.HomeActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.widget.CommonPopupWindow;
import com.zxjk.duoduo.ui.msgpage.widget.dialog.DeleteFriendInformationDialog;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.weight.TitleBar;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import io.rong.imkit.RongIM;

/**
 * @author Administrator
 * @// TODO: 2019\4\4 0004 这个是对于好友详情做出的一个分支，好友详情逻辑梳理好，加进去容易出错，所以建立分支达到效果
 */
@SuppressLint("CheckResult")
public class ConversationDetailsActivity extends BaseActivity implements View.OnClickListener {
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
    private CommonPopupWindow popupWindow;

    DeleteFriendInformationDialog dialog;

    /**
     * 0是男1是女
     */
    String sex = "0";




    FriendInfoResponse friendInfoResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_details);
        ButterKnife.bind(this);
        String  userId=getIntent().getStringExtra("UserId");
        getFriendInfoById(userId);
    }






    @OnClick({R.id.m_people_information_send_to_message, R.id.m_people_information_voice_calls})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_people_information_send_to_message:
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
//                if (conversationType == 0) {
//                    RongIM.getInstance().startPrivateChat(this, friendInfoResponse.getId(), friendInfoResponse.getNick());
//                    voiceBtn.setVisibility(View.GONE);
//                } else {
//                    voiceBtn.setVisibility(View.VISIBLE);
//                    RongIM.getInstance().startPrivateChat(this, friendInfoResponse.getId(), friendInfoResponse.getNick());
//                }
                break;
            case R.id.m_people_information_voice_calls:
                ToastUtils.showShort("此功能暂未实现");
                break;
            case R.id.update_rename:
                startActivity(new Intent(this, ModifyNotesActivity.class));
                break;
            case R.id.recommend_to_friend:
                ToastUtils.showShort("暂未实现");
                break;
            case R.id.delete_friend:
                dialog = new DeleteFriendInformationDialog(this);
                dialog.setOnClickListener(() -> {

                        ToastUtils.showShort(getString(R.string.del_my_message));
                        dialog.dismiss();

                });
                dialog.show(friendInfoResponse.getNick());

                break;
            default:
                break;
        }
    }

    public void getFriendInfoById(String userId) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendInfoById(userId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendInfoResponse -> {

//                    GlideUtil.loadCornerImg(heardIcon, friendInfoResponse.getHeadPortrait(), 2);
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
                }, this::handleApiError);

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

}
