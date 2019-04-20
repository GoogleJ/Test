package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.dialog.SelectPopupWindow;
import com.zxjk.duoduo.utils.CommonUtils;

@SuppressLint("CheckResult")
public class GameDownScoreActivity extends BaseActivity {

    private EditText et;
    private TextView tv;
    private String total;

    private String groupId;

    private SelectPopupWindow selectPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_down_score);

        selectPopupWindow = new SelectPopupWindow(this, (psw, complete) -> {

        });

        groupId = getIntent().getStringExtra("groupId");

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getGroupMemberPoints(groupId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(s -> {
                    total = s;
                    tv.setText("当前可下分总额" + s + "HK");
                }, this::handleApiError);
    }

    //下分
    public void commit(View view) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .upPoints()
    }

    //记录
    public void record(View view) {

    }

    //返回
    public void back(View view) {
        finish();
    }

}
