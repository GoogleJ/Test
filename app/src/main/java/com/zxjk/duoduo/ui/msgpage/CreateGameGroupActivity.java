package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.HomeActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.CreateGameGroupAdapter;
import com.zxjk.duoduo.ui.widget.dialog.SelectPopupWindow;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.MD5Utils;

import java.text.DecimalFormat;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.InformationNotificationMessage;

public class CreateGameGroupActivity extends BaseActivity implements SelectPopupWindow.OnPopWindowClickListener {

    private RecyclerView recycler;
    private CreateGameGroupAdapter adapter;
    private SelectPopupWindow selectPopupWindow;

    private String gameType, playId, pumpingRate, proportionOfFees, typeName, commission, duobao;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game_group);

        selectPopupWindow = new SelectPopupWindow(this, this);

        ((TextView) findViewById(R.id.tv_title)).setText(R.string.create_game_group);

        findViewById(R.id.rl_back).setOnClickListener(v -> finish());

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getGameClass()
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(bindToLifecycle())
                .subscribe(response -> {
                    adapter = new CreateGameGroupAdapter(response, CreateGameGroupActivity.this);
                    recycler.setAdapter(adapter);
                    adapter.setOnCreateGameGroupClick((gameType, playId, pumpingRate, proportionOfFees, typeName, commission, duobao) -> {
                        this.gameType = gameType;
                        this.playId = playId;
                        this.pumpingRate = pumpingRate;
                        this.proportionOfFees = proportionOfFees;
                        this.typeName = typeName;
                        this.commission = commission;
                        this.duobao = duobao;

                        Rect rect = new Rect();
                        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                        int winHeight = getWindow().getDecorView().getHeight();
                        selectPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
                    });
                }, this::handleApiError);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (complete) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .makeGameGroup(gameType, playId, pumpingRate, MD5Utils.getMD5(psw), proportionOfFees, typeName, commission, decimalFormat.format(Float.parseFloat(duobao)))
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(CreateGameGroupActivity.this)))
                    .subscribe(response -> {
                        InformationNotificationMessage message = InformationNotificationMessage.obtain(getString(R.string.notice_creategamegroup));
                        RongIM.getInstance().sendDirectionalMessage(Conversation.ConversationType.GROUP, response.getId(), message, new String[]{Constant.userId}, null, null, new IRongCallback.ISendMessageCallback() {
                            @Override
                            public void onAttached(Message message) {

                            }

                            @Override
                            public void onSuccess(Message message) {
                                ToastUtils.showShort(R.string.create_game_group_success);
                                Intent intent = new Intent(CreateGameGroupActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                RongIM.getInstance().startGroupChat(CreateGameGroupActivity.this, response.getId(), response.getGroupNikeName());
                            }

                            @Override
                            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                                ToastUtils.showShort(R.string.function_fail);
                            }
                        });
                    }, this::handleApiError);
        }
    }
}
