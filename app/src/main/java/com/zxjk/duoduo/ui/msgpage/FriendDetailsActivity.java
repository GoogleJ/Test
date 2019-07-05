package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;

import com.blankj.utilcode.util.ToastUtils;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.bean.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.HomeActivity;
import com.zxjk.duoduo.ui.ZoomActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.widget.CommonPopupWindow;
import com.zxjk.duoduo.ui.msgpage.widget.dialog.DeleteFriendInformationDialog;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;

import java.util.Iterator;

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

    String imageUrl;
    String sex = "0";
    private CommonPopupWindow popupWindow;
    DeleteFriendInformationDialog dialog;
    FriendInfoResponse friendInfoResponse;
    private RelativeLayout rl_end;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_information);

        initUI();

        initFriendIntent();
    }

    private void initFriendIntent() {
        friendInfoResponse = (FriendInfoResponse) getIntent().getSerializableExtra("friendResponse");
        if (friendInfoResponse == null) {
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
        } else {
            handData();
        }
    }

    @SuppressLint("SetTextI18n")
    private void handData() {
        imageUrl = friendInfoResponse.getHeadPortrait();
        GlideUtil.loadCornerImg(ivHeadPortrait, friendInfoResponse.getHeadPortrait(), 5);
        tvDuoDuoNumber.setText(getString(R.string.duoduo_acount) + " " + friendInfoResponse.getDuoduoId());
        if (friendInfoResponse.getIsShowRealname().equals("0")) {
            tvRealName.setText(R.string.real_name1);
        } else {
            tvRealName.setText(getString(R.string.real_name) + " " + friendInfoResponse.getRealname());
        }
        tvDistrict.setText(getString(R.string.district) + " " + friendInfoResponse.getAddress());
        String mobile = friendInfoResponse.getMobile();
        tvPhoneNumber.setText(mobile.substring(0, 3) + "****" + mobile.substring(7));
        tvEmail.setText(TextUtils.isEmpty(friendInfoResponse.getEmail()) ? "暂无" : friendInfoResponse.getEmail());
        tvSignature.setText(TextUtils.isEmpty(friendInfoResponse.getSignature()) ? "暂无" : friendInfoResponse.getSignature());
        tvWalletAddress.setText(TextUtils.isEmpty(friendInfoResponse.getWalletAddress()) ? "暂无" : friendInfoResponse.getWalletAddress());
        if (sex.equals(friendInfoResponse.getSex())) {
            ivGender.setImageDrawable(getDrawable(R.drawable.icon_gender_man));
        } else {
            ivGender.setImageDrawable(getDrawable(R.drawable.icon_gender_woman));
        }
        if (friendInfoResponse.getId().equals(Constant.userId)) {
            rl_end.setVisibility(View.INVISIBLE);
            tvNickname.setText(friendInfoResponse.getNick());
        } else {
            tvNickname.setText(TextUtils.isEmpty(friendInfoResponse.getRemark()) ? friendInfoResponse.getNick() : friendInfoResponse.getRemark());
        }
    }

    private void initUI() {
        ButterKnife.bind(this);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.personal_details));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        rl_end = findViewById(R.id.rl_end);
        rl_end.setVisibility(View.VISIBLE);
        rl_end.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                return;
            }
            popupWindow = new CommonPopupWindow.Builder(FriendDetailsActivity.this)
                    .setView(R.layout.popup_window_people_information)
                    .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setAnimationStyle(R.style.AnimDown)
                    .setBackGroundLevel(1.0f)
                    .setViewOnclickListener(FriendDetailsActivity.this)
                    .setOutsideTouchable(true)
                    .create();
            popupWindow.showAsDropDown(rl_end);
        });
    }

    @OnClick({R.id.tv_sendMessage, R.id.iv_duplication, R.id.iv_headPortrait})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_headPortrait:
                Intent intent5 = new Intent(this, ZoomActivity.class);
                intent5.putExtra("image", imageUrl);
                startActivity(intent5,
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                                ivHeadPortrait, "12").toBundle());
                break;
            case R.id.iv_duplication:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(tvWalletAddress.getText().toString());
                ToastUtils.showShort(getString(R.string.duplicated_to_clipboard));
                break;
            case R.id.tv_sendMessage:
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                RongIM.getInstance().startPrivateChat(this, friendInfoResponse.getId(), friendInfoResponse.getNick());
                break;
            case R.id.update_rename:
                popupWindow.dismiss();
                Intent intent1 = new Intent(FriendDetailsActivity.this, ModifyNotesActivity.class);
                intent1.putExtra("friendId", friendInfoResponse.getId());
                startActivityForResult(intent1, 1);
                break;
            case R.id.recommend_to_friend:
                popupWindow.dismiss();
                Intent intentCard = new Intent(FriendDetailsActivity.this, SelectContactForCardActivity.class);
                intentCard.putExtra("userId", friendInfoResponse.getId());
                startActivity(intentCard);
                break;
            case R.id.delete_friend:
                popupWindow.dismiss();
                NiceDialog.init().setLayoutId(R.layout.layout_general_dialog).setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        holder.setText(R.id.tv_title, "删除联系人");
                        TextView textView = holder.getView(R.id.tv_content);
                        textView.setText(String.format(getResources().getString(R.string.m_delete_friend_label), friendInfoResponse.getNick()));
                        holder.setText(R.id.tv_cancel, "取消");
                        holder.setText(R.id.tv_notarize, "删除");
                        holder.setOnClickListener(R.id.tv_cancel, v1 -> dialog.dismiss());
                        holder.setOnClickListener(R.id.tv_notarize, v12 -> deleteFriend(friendInfoResponse.getId(), dialog));
                    }
                }).setDimAmount(0.5f).setOutCancel(false).show(getSupportFragmentManager());
                break;
        }
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        if (layoutResId == R.layout.popup_window_people_information) {
            view.findViewById(R.id.update_rename).setOnClickListener(this);
            view.findViewById(R.id.recommend_to_friend).setOnClickListener(this);
            view.findViewById(R.id.delete_friend).setOnClickListener(this);
        }
    }

    private void deleteFriend(String friendId, BaseNiceDialog dialog) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .deleteFriend(friendId)
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .compose(bindToLifecycle())
                .subscribe(s -> {
                    Iterator<FriendInfoResponse> iterator = Constant.friendsList.iterator();
                    while (iterator.hasNext()) {
                        FriendInfoResponse next = iterator.next();
                        if (next.getId().equals(friendId)) {
                            iterator.remove();
                            break;
                        }
                    }
                    dialog.dismiss();
                    ToastUtils.showShort(getString(R.string.the_friend_has_been_deleted));
                    RongIM.getInstance().clearMessages(Conversation.ConversationType.PRIVATE,
                            friendId, null);
                    RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE
                            , friendId, null);
                    CommandMessage commandMessage = CommandMessage.obtain("deleteFriend", "");
                    Message deleteFriend = Message.obtain(friendId, Conversation.ConversationType.PRIVATE, commandMessage);
                    RongIM.getInstance().sendMessage(deleteFriend, "", "", new IRongCallback.ISendMessageCallback() {
                        @Override
                        public void onAttached(Message message) {
                        }

                        @Override
                        public void onSuccess(Message message) {

                        }

                        @Override
                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                        }
                    });
                    Intent intent = new Intent(FriendDetailsActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }, this::handleApiError);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            String remark = data.getStringExtra("remark");
            if (!TextUtils.isEmpty(remark))
                tvNickname.setText(remark);
        }
    }
}
