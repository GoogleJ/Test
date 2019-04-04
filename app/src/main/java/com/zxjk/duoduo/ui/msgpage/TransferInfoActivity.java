package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.TransferResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.TransferMessage;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.weight.TitleBar;

import java.text.SimpleDateFormat;

import androidx.annotation.Nullable;
import io.reactivex.functions.Consumer;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * @author Administrator
 * @// TODO: 2019\4\3 0003 转账详情
 */
public class TransferInfoActivity extends BaseActivity {
    TitleBar titleBar;
    TextView commitBtn;
    TextView m_transfer_info_money_text;
    TextView m_transfer_info_caveat_text;
    TextView m_transfer_info_pending_payment_text;
    TextView tvZhuanZhangTime;
    TextView tvShouKuanTime;
    ImageView m_transfer_info_icon;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_info);

        Intent intent = getIntent();
        boolean fromSelf = intent.getBooleanExtra("fromSelf", false);
        TransferMessage msg = intent.getParcelableExtra("msg");
        titleBar = findViewById(R.id.m_transfer_info_title_bar);
        commitBtn = findViewById(R.id.m_transfer_info_commit_btn);
        m_transfer_info_money_text = findViewById(R.id.m_transfer_info_money_text);
        m_transfer_info_caveat_text = findViewById(R.id.m_transfer_info_caveat_text);
        m_transfer_info_pending_payment_text = findViewById(R.id.m_transfer_info_pending_payment_text);
        tvZhuanZhangTime = findViewById(R.id.tvZhuanZhangTime);
        tvShouKuanTime = findViewById(R.id.tvShouKuanTime);
        m_transfer_info_icon = findViewById(R.id.m_transfer_info_icon);

        m_transfer_info_money_text.setText(msg.getHk());
        titleBar.getLeftImageView().setOnClickListener(v -> finish());
        commitBtn.setOnClickListener(v -> ServiceFactory.getInstance().getBaseService(Api.class)
                .collect(msg.getTransferId())
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(transferResponse -> {
                    msg.setType("2");
                    msg.setRemarks("已收钱");
                    Message obtain = Message.obtain(msg.getUserInfo().getUserId(), Conversation.ConversationType.PRIVATE, msg);
                    RongIM.getInstance().sendMessage(obtain, null, null, new IRongCallback.ISendMessageCallback() {
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
                    msg.setRemarks("已被领取");
                    Message obtain1 = Message.obtain(Constant.currentUser.getId(), Conversation.ConversationType.PRIVATE, msg);
                    RongIMClient.getInstance().setMessageReceivedStatus(obtain1.getMessageId(), obtain1.getReceivedStatus(), null);
                }, this::handleApiError));

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getTransferInfo(msg.getTransferId())
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(transferResponse -> {
                    String status = transferResponse.getStatus();
                    if (status.equals("0") && fromSelf) {
                        commitBtn.setVisibility(View.GONE);
                        m_transfer_info_pending_payment_text.setText("待" + msg.getUserInfo().getName() + "确认领取");
                        m_transfer_info_caveat_text.setText(R.string.m_transfer_pending_return_text);
                        tvShouKuanTime.setVisibility(View.GONE);
                        tvZhuanZhangTime.setText("转账时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transferResponse.getCreateTime()));
                    } else if (status.equals("0") && !fromSelf) {
                        m_transfer_info_caveat_text.setText(R.string.m_transfer_info_caveat_text);
                    } else if (status.equals("1") && fromSelf) {
                        commitBtn.setVisibility(View.GONE);
                        m_transfer_info_icon.setImageResource(R.drawable.icon_transfer_successful);
                        tvZhuanZhangTime.setText("转账时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transferResponse.getCreateTime()));
                        tvShouKuanTime.setText("收账时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transferResponse.getCloseTime()));
                        m_transfer_info_pending_payment_text.setText(R.string.receive1);
                    } else if (status.equals("1") && !fromSelf) {
                        commitBtn.setVisibility(View.GONE);
                        m_transfer_info_icon.setImageResource(R.drawable.icon_transfer_successful);
                        tvZhuanZhangTime.setText("转账时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transferResponse.getCreateTime()));
                        tvShouKuanTime.setText("收账时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transferResponse.getCloseTime()));
                        m_transfer_info_pending_payment_text.setText(msg.getUserInfo().getName() + "已领取");
                        m_transfer_info_caveat_text.setText(R.string.receive3);
                    } else if (status.equals("2") && fromSelf) {
                        m_transfer_info_pending_payment_text.setText(R.string.yituihuan);
                        commitBtn.setVisibility(View.GONE);
                        tvZhuanZhangTime.setText("转账时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transferResponse.getCreateTime()));
                        tvShouKuanTime.setText("退还时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transferResponse.getCloseTime()));
                    } else {
                        m_transfer_info_pending_payment_text.setText(R.string.yiguoqi);
                        commitBtn.setVisibility(View.GONE);
                        tvZhuanZhangTime.setText("转账时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transferResponse.getCreateTime()));
                        tvShouKuanTime.setText("退还时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transferResponse.getCloseTime()));
                    }
                }, this::handleApiError);
    }
}
