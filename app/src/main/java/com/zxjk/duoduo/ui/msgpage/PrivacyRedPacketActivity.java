package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.RedPackageResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.RedPacketMessage;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.MD5Utils;
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
 * @// TODO: 2019\4\2 0002  跳转到发送红包的界面
 */
public class PrivacyRedPacketActivity extends BaseActivity implements SelectPopupWindow.OnPopWindowClickListener {

    TextView sendMessageBtn;
    EditText m_red_envelopes_money_edit;
    EditText m_red_envelopes_label;
    TextView m_red_envelopes_money_text;

    SelectPopupWindow selectPopupWindow;

    String money;
    UserInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_envelopes);

        selectPopupWindow = new SelectPopupWindow(this, this);

        m_red_envelopes_money_edit = findViewById(R.id.m_red_envelopes_money_edit);
        m_red_envelopes_label = findViewById(R.id.m_red_envelopes_label);
        m_red_envelopes_money_text = findViewById(R.id.m_red_envelopes_money_text);
        sendMessageBtn = findViewById(R.id.m_red_envelopes_commit_btn);

        userInfo = getIntent().getParcelableExtra("user");

        sendMessageBtn.setOnClickListener(v -> {
            money = m_red_envelopes_money_edit.getText().toString().trim();
            if (TextUtils.isEmpty(money)) {
                ToastUtils.showShort(R.string.input_redmoney);
                return;
            }

            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int winHeight = getWindow().getDecorView().getHeight();
            selectPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (!complete) {
            return;
        }

        SendRedSingleRequest redSingleRequest = new SendRedSingleRequest();
        redSingleRequest.setMessage(m_red_envelopes_money_text.getText().toString().trim());
        redSingleRequest.setMoney(Double.parseDouble(money));
        redSingleRequest.setReceiveCustomerId(userInfo.getUserId());
        redSingleRequest.setPayPwd(MD5Utils.getMD5(psw));

        ServiceFactory.getInstance().getBaseService(Api.class)
                .sendSingleRedPackage(new Gson().toJson(redSingleRequest))
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(PrivacyRedPacketActivity.this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(redPackageResponse -> {
                    RedPacketMessage message = new RedPacketMessage();
                    message.setFromCustomer(Constant.currentUser.getId());
                    message.setRemark(m_red_envelopes_label.getText().toString().trim());
                    message.setRedId(redPackageResponse.getRedPackageId());
                    Message message1 = Message.obtain(userInfo.getUserId(), Conversation.ConversationType.PRIVATE, message);
                    RongIM.getInstance().sendMessage(message1, null, null, new IRongCallback.ISendMessageCallback() {
                        @Override
                        public void onAttached(Message message) {
                        }

                        @Override
                        public void onSuccess(Message message) {
                            finish();
                        }

                        @Override
                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }, this::handleApiError);
    }
}