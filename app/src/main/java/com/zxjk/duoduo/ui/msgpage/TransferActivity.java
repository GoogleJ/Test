package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.response.TransferResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.TransferMessage;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.utils.MD5Utils;
import com.zxjk.duoduo.weight.TitleBar;
import com.zxjk.duoduo.weight.dialog.SelectPopupWindow;

import androidx.annotation.Nullable;
import io.reactivex.functions.Consumer;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * @author Administrator
 * @// TODO: 2019\4\3 0003 转账的activity
 */
public class TransferActivity extends BaseActivity implements SelectPopupWindow.OnPopWindowClickListener {

    TitleBar titleBar;
    TextView commitBtn;
    TextView m_transfer_nick_name;
    EditText etTransferInfo;
    EditText m_transfer_money_text;
    ImageView m_transfer_heard_icon;

    SelectPopupWindow selectPopupWindow;
    UserInfo targetUser;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        selectPopupWindow = new SelectPopupWindow(this, this);

        titleBar = findViewById(R.id.m_transfer_title_bar);
        titleBar.getLeftImageView().setOnClickListener(v -> finish());
        etTransferInfo = findViewById(R.id.etTransferInfo);
        m_transfer_money_text = findViewById(R.id.m_transfer_money_text);
        m_transfer_heard_icon = findViewById(R.id.m_transfer_heard_icon);
        m_transfer_nick_name = findViewById(R.id.m_transfer_nick_name);
        commitBtn = findViewById(R.id.m_transfer_commit_btn);

        commitBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(m_transfer_money_text.getText().toString().trim())) {
                ToastUtils.showShort(R.string.inputzhuanzhangmoney);
                return;
            }
            if (Double.valueOf(m_transfer_money_text.getText().toString().trim()) == 0) {
                ToastUtils.showShort(R.string.inputzhuanzhangmoney1);
                return;
            }
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int winHeight = getWindow().getDecorView().getHeight();
            selectPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
        });

        targetUser = getIntent().getParcelableExtra("user");
        if (null == targetUser) {
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getCustomerInfoById(getIntent().getStringExtra("userId"))
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .compose(RxSchedulers.normalTrans())
                    .subscribe(response -> {
                        targetUser = new UserInfo(response.getId(), response.getNick(), Uri.parse(response.getHeadPortrait()));
                        GlideUtil.loadCornerImg(m_transfer_heard_icon, response.getHeadPortrait(), 3);
                        m_transfer_nick_name.setText(response.getNick());
                    }, this::handleApiError);
        } else {
            GlideUtil.loadCornerImg(m_transfer_heard_icon, targetUser.getPortraitUri().toString(), 3);
            m_transfer_nick_name.setText(targetUser.getName());
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (complete) {
            String zhuanzhangInfo = etTransferInfo.getText().toString().trim();
            String remarks = TextUtils.isEmpty(zhuanzhangInfo) ? ("转账给" + targetUser.getName()) : zhuanzhangInfo;
            String payPsd = MD5Utils.getMD5(psw);
            String hk = m_transfer_money_text.getText().toString().trim();
            String toId = targetUser.getUserId();

            ServiceFactory.getInstance().getBaseService(Api.class)
                    .transfer(toId, hk, payPsd, remarks)
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(TransferActivity.this)))
                    .compose(RxSchedulers.normalTrans())
                    .subscribe(s -> {
                        TransferMessage message = new TransferMessage();
                        message.setRemarks(remarks);
                        message.setPayPwd(payPsd);
                        message.setHk(hk);
                        message.setTransferId(s.getId());

                        Message message1 = Message.obtain(targetUser.getUserId(), Conversation.ConversationType.PRIVATE, message);
                        RongIM.getInstance().sendMessage(message1, null, null, new IRongCallback.ISendMessageCallback() {
                            @Override
                            public void onAttached(Message message) {
                            }
                            @Override
                            public void onSuccess(Message message) {
                                Intent intent = new Intent(TransferActivity.this, TransferSuccessActivity.class);
                                intent.putExtra("money", hk);
                                intent.putExtra("name", targetUser.getName());
                                startActivity(intent);
                                finish();
                            }
                            @Override
                            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                            }
                        });
                        startActivity(new Intent(this, TransferSuccessActivity.class));
                    }, this::handleApiError);
        }
    }
}
