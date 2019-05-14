package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.dialog.SelectPopupWindow;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.MD5Utils;

/**
 * author L
 * create at 2019/5/8
 * description: 下分
 */
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
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.downscore));
        TextView tv_end = findViewById(R.id.tv_end);
        tv_end.setVisibility(View.GONE);
        tv_end.setText(getString(R.string.jilu));

        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        //记录
        tv_end.setOnClickListener(v -> {

        });


        et = findViewById(R.id.et);
        tv = findViewById(R.id.tv);

        groupId = getIntent().getStringExtra("groupId");

        selectPopupWindow = new SelectPopupWindow(this, (psw, complete) -> {
            if (complete) {
                ServiceFactory.getInstance().getBaseService(Api.class)
                        .upPoints(groupId, et.getText().toString().trim(), MD5Utils.getMD5(psw))
                        .compose(RxSchedulers.normalTrans())
                        .compose(bindToLifecycle())
                        .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                        .subscribe(s -> {
                            ToastUtils.showShort(R.string.input_downscore2);
                            finish();
                        }, this::handleApiError);
            }
        });

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
        if (TextUtils.isEmpty(et.getText().toString().trim())) {
            ToastUtils.showShort(R.string.input_downscore);
            return;
        }
        if (Double.parseDouble(total) < Double.parseDouble(et.getText().toString().trim())) {
            ToastUtils.showShort(R.string.input_downscore1);
            return;
        }
        KeyboardUtils.hideSoftInput(this);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        selectPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
    }


}
