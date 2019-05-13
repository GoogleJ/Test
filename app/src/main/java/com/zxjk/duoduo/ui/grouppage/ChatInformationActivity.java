package com.zxjk.duoduo.ui.grouppage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.skin.SkinReportActivity;
import com.zxjk.duoduo.ui.HomeActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.CreateGroupActivity;
import com.zxjk.duoduo.ui.msgpage.FriendDetailsActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * author L
 * create at 2019/5/7
 * description:  个人聊天信息
 */
@SuppressLint("CheckResult")
public class ChatInformationActivity extends BaseActivity {

    private TextView tv_qm;
    private UserInfo userInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_information);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.lqxx));
        ImageView iv_head = findViewById(R.id.iv_head);
        TextView tv_name = findViewById(R.id.tv_name);
        tv_qm = findViewById(R.id.tv_qm);
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());

        userInfo = getIntent().getParcelableExtra("bean");
        GlideUtil.loadCornerImg(iv_head, userInfo.getPortraitUri().toString(), 5);
        tv_name.setText(userInfo.getName());

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getCustomerInfoById(userInfo.getUserId())
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(response -> tv_qm.setText(response.getSignature()), this::handleApiError);


        findViewById(R.id.rl_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatInformationActivity.this, FriendDetailsActivity.class);
                intent.putExtra("friendId", userInfo.getUserId());
                startActivity(intent);
            }
        });
        findViewById(R.id.rl_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatInformationActivity.this, CreateGroupActivity.class);
                intent.putExtra("eventType", 1);
                startActivity(intent);
            }
        });
        findViewById(R.id.rl_qk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NiceDialog.init().setLayoutId(R.layout.layout_general_dialog).setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        holder.setText(R.id.tv_title, "提示");
                        holder.setText(R.id.tv_content, "确定要清空当前聊天记录吗？");
                        holder.setText(R.id.tv_cancel, "取消");
                        holder.setText(R.id.tv_notarize, "确认");
                        holder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                            }
                        });

                        holder.setOnClickListener(R.id.tv_notarize, new View.OnClickListener() {
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
                                                ToastUtils.showShort(R.string.function_fail);
                                            }
                                        });
                            }
                        });

                    }
                }).setDimAmount(0.5f).setOutCancel(false).show(getSupportFragmentManager());
            }
        });
        findViewById(R.id.rl_juBao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatInformationActivity.this, SkinReportActivity.class));
            }
        });


    }


}
