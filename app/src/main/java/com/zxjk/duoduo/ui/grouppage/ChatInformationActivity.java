package com.zxjk.duoduo.ui.grouppage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.HomeActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.CreateGroupActivity;
import com.zxjk.duoduo.ui.widget.TitleBar;
import com.zxjk.duoduo.ui.widget.dialog.ConfirmDialog;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;
import androidx.annotation.Nullable;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;


/**
 * @author Administrator
 * @// TODO: 2019\3\29 0029 关于群组详情的实现
 */
public class ChatInformationActivity extends BaseActivity {
    TitleBar titleBar;

    private ImageView m_chat_information_header;
    private TextView m_chat_information_user_name;
    private TextView m_chat_information_signature_label;
    private UserInfo userInfo;

    @SuppressLint("CheckResult")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_information);
        titleBar = findViewById(R.id.m_fragment_chat_information_title_bar);
        m_chat_information_header = findViewById(R.id.m_chat_information_header);
        m_chat_information_user_name = findViewById(R.id.m_chat_information_user_name);
        m_chat_information_signature_label = findViewById(R.id.m_chat_information_signature_label);
        titleBar.getLeftImageView().setOnClickListener(v -> finish());

        userInfo = getIntent().getParcelableExtra("bean");
        GlideUtil.loadCornerImg(m_chat_information_header, userInfo.getPortraitUri().toString(), 2);
        m_chat_information_user_name.setText(userInfo.getName());

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getCustomerInfoById(userInfo.getUserId())
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(response -> m_chat_information_signature_label.setText(response.getSignature()), this::handleApiError);
    }

    public void clearChatHistory(View view) {
        ConfirmDialog dialog = new ConfirmDialog(this, "提示", "确定要清空当前聊天记录么", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIMClient.getInstance().cleanHistoryMessages(Conversation.ConversationType.PRIVATE
                        , userInfo.getUserId(), 0, false, new RongIMClient.OperationCallback() {
                            @Override
                            public void onSuccess() {
                                Intent intent = new Intent(ChatInformationActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        });
            }
        });
        dialog.show();
    }

    public void createGroup(View view) {
        Intent intent = new Intent(this, CreateGroupActivity.class);
        intent.putExtra("eventType", 1);
        startActivity(intent);
    }
}
