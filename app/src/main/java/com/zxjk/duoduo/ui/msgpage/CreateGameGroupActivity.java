package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import io.rong.imkit.RongIM;

public class CreateGameGroupActivity extends BaseActivity implements SelectPopupWindow.OnPopWindowClickListener {

    private RecyclerView recycler;
    private CreateGameGroupAdapter adapter;
    private SelectPopupWindow selectPopupWindow;

    private String gameType, playId, pumpingRate, proportionOfFees, typeName, commission;

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
                    adapter = new CreateGameGroupAdapter(response);
                    recycler.setAdapter(adapter);
                    adapter.setOnCreateGameGroupClick((gameType, playId, pumpingRate, proportionOfFees, typeName, commission) -> {
                        this.gameType = gameType;
                        this.playId = playId;
                        this.pumpingRate = pumpingRate;
                        this.proportionOfFees = proportionOfFees;
                        this.typeName = typeName;
                        this.commission = commission;

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
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .makeGameGroup(gameType, playId, pumpingRate, MD5Utils.getMD5(psw), proportionOfFees, typeName, commission, "")
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver())
                    .subscribe(response -> {
                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        RongIM.getInstance().startGroupChat(this, response.getId(), response.getGroupNikeName());
                    }, this::handleApiError);
        }
    }
}
