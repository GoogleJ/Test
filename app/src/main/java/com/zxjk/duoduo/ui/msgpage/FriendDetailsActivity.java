package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
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
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.CommandMessage;

/**
 * author L
 * create at 2019/5/7
 * description: 个人信息
 */
@SuppressLint("CheckResult")
public class FriendDetailsActivity extends BaseActivity implements View.OnClickListener, CommonPopupWindow.ViewInterface {

    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_realName)
    TextView tvRealName;
    @BindView(R.id.tv_DuoDuoNumber)
    TextView tvDuoDuoNumber;
    @BindView(R.id.iv_gender)
    ImageView ivGender;
    @BindView(R.id.iv_headPortrait)
    ImageView ivHeadPortrait;
    @BindView(R.id.tv_district)
    TextView tvDistrict;
    @BindView(R.id.tv_phoneNumber)
    TextView tvPhoneNumber;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_signature)
    TextView tvSignature;
    @BindView(R.id.tv_walletAddress)
    TextView tvWalletAddress;

    String sex = "0";
    private CommonPopupWindow popupWindow;
    DeleteFriendInformationDialog dialog;
    FriendInfoResponse friendInfoResponse;
    FriendInfoResponse friendInfo;
    FriendInfoResponse contactResponse;
    int intentType = 0;
    private RelativeLayout rl_end;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_information);
        ButterKnife.bind(this);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.personal_details));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        rl_end = findViewById(R.id.rl_end);
        rl_end.setVisibility(View.VISIBLE);
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

    @SuppressLint("SetTextI18n")
    private void handData() {
        if (intentType == 0) {
            if (friendInfoResponse.getId().equals(Constant.userId)) {
                rl_end.setVisibility(View.GONE);
            }
            GlideUtil.loadCornerImg(ivHeadPortrait, friendInfoResponse.getHeadPortrait(), 5);
            tvNickname.setText(friendInfoResponse.getNick());
            tvDuoDuoNumber.setText(getString(R.string.duoduo_acount) + " " + friendInfoResponse.getDuoduoId());
            tvRealName.setText(getString(R.string.real_name) + " " + friendInfoResponse.getRealname());
            tvDistrict.setText(getString(R.string.district) + " " + friendInfoResponse.getAddress());
            tvPhoneNumber.setText(friendInfoResponse.getMobile());
            tvEmail.setText(friendInfoResponse.getEmail());
            tvSignature.setText(friendInfoResponse.getSignature());
            tvWalletAddress.setText(friendInfoResponse.getWalletAddress());
            if (sex.equals(friendInfoResponse.getSex())) {
                //男
                ivGender.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
            } else {
                ivGender.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
            }
        } else if (intentType == 1) {
            if (friendInfo.getId().equals(Constant.userId)) {
                rl_end.setVisibility(View.GONE);
            }
            GlideUtil.loadCornerImg(ivHeadPortrait, friendInfo.getHeadPortrait(), 5);
            tvNickname.setText(friendInfo.getNick());
            tvDuoDuoNumber.setText(getString(R.string.duoduo_acount) + " " + friendInfo.getDuoduoId());
            tvRealName.setText(getString(R.string.real_name) + " " + friendInfo.getRealname());
            tvDistrict.setText(getString(R.string.district) + " " + friendInfo.getAddress());
            tvPhoneNumber.setText(friendInfo.getMobile());
            tvEmail.setText(friendInfo.getEmail());
            tvSignature.setText(friendInfo.getSignature());
            tvWalletAddress.setText(friendInfo.getWalletAddress());
            if (sex.equals(friendInfo.getSex())) {
                //男
                ivGender.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
            } else {
                ivGender.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
            }
        } else {
            if (contactResponse.getId().equals(Constant.userId)) {
                rl_end.setVisibility(View.GONE);
            }
            GlideUtil.loadCornerImg(ivHeadPortrait, contactResponse.getHeadPortrait(), 5);
            tvNickname.setText(contactResponse.getNick());
            tvDuoDuoNumber.setText(getString(R.string.duoduo_acount) + " " + contactResponse.getDuoduoId());
            tvRealName.setText(getString(R.string.real_name) + " " + contactResponse.getRealname());
            tvDistrict.setText(getString(R.string.district) + " " + contactResponse.getAddress());
            tvPhoneNumber.setText(contactResponse.getMobile());
            tvEmail.setText(contactResponse.getEmail());
            tvSignature.setText(contactResponse.getSignature());
            tvWalletAddress.setText(contactResponse.getWalletAddress());
            if (sex.equals(contactResponse.getSex())) {
                //男
                ivGender.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
            } else {
                ivGender.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
            }
        }
    }

    private void initUI() {
        rl_end.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                return;
            }
            popupWindow = new CommonPopupWindow.Builder(FriendDetailsActivity.this)
                    .setView(R.layout.popup_window_people_information)
                    .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setAnimationStyle(R.style.AnimDown)
                    .setBackGroundLevel(1.0f)
                    .setViewOnclickListener(FriendDetailsActivity.this::getChildView)
                    .setOutsideTouchable(true)
                    .create();
            popupWindow.showAsDropDown(rl_end);
        });
    }

    @OnClick({R.id.tv_sendMessage, R.id.iv_duplication})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_duplication:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(tvWalletAddress.getText().toString());
                ToastUtils.showShort(getString(R.string.duplicated_to_clipboard));
                break;

            case R.id.tv_sendMessage:
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
                popupWindow.dismiss();
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
                popupWindow.dismiss();
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
                popupWindow.dismiss();
                NiceDialog.init().setLayoutId(R.layout.layout_general_dialog).setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        holder.setText(R.id.tv_title, "删除联系人");
                        TextView textView = holder.getView(R.id.tv_content);
                        if (intentType == 0) {
                            textView.setText(String.format(getResources().getString(R.string.m_delete_friend_label), friendInfoResponse.getNick()));
                        } else if (intentType == 1) {
                            textView.setText(String.format(getResources().getString(R.string.m_delete_friend_label), friendInfo.getNick()));
                        } else {
                            textView.setText(String.format(getResources().getString(R.string.m_delete_friend_label), contactResponse.getNick()));
                        }
                        holder.setText(R.id.tv_cancel, "取消");
                        holder.setText(R.id.tv_notarize, "删除");
                        holder.setOnClickListener(R.id.tv_cancel, v1 -> dialog.dismiss());
                        holder.setOnClickListener(R.id.tv_notarize, v12 -> {
                            dialog.dismiss();
                            if (intentType == 0) {
                                FriendDetailsActivity.this.deleteFriend(friendInfoResponse.getId());
                            } else if (intentType == 1) {
                                FriendDetailsActivity.this.deleteFriend(friendInfo.getId());
                            } else {
                                FriendDetailsActivity.this.deleteFriend(contactResponse.getId());
                            }
                            FriendDetailsActivity.this.finish();
                        });
                    }
                }).setDimAmount(0.5f).setOutCancel(false).show(getSupportFragmentManager());
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
                    Message myMessage = Message.obtain(friendId, Conversation.ConversationType.PRIVATE, CommandMessage.obtain("deleteFriend", ""));
                    RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
                        @Override
                        public void onAttached(Message message) {

                        }

                        @Override
                        public void onSuccess(Message message) {
                            ToastUtils.showShort(getString(R.string.the_friend_has_been_deleted));
                            dialog.dismiss();
                            RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE
                                    , friendId, null);
                            RongIMClient.getInstance().cleanHistoryMessages(Conversation.ConversationType.PRIVATE,
                                    friendId, 0, false, null);
                        }

                        @Override
                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                        }
                    });
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
