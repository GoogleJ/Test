package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.dialog.SelectPopupWindow;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.utils.MD5Utils;

/**
 * author L
 * create at 2019/5/8
 * description: 上分
 */
public class GameUpScoreActivity extends BaseActivity {

    private ImageView ivHead;
    private TextView tvName;
    private EditText et;
    private String groupId;
    private SelectPopupWindow selectPopupWindow;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_up_score);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.upscore));
        TextView tv_end = findViewById(R.id.tv_end);
        tv_end.setVisibility(View.VISIBLE);
        tv_end.setText(getString(R.string.jilu));

        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        //记录
        tv_end.setOnClickListener(v -> {

        });
        groupId = getIntent().getStringExtra("groupId");

        ivHead = findViewById(R.id.ivHead);
        tvName = findViewById(R.id.tvName);
        et = findViewById(R.id.et);

        GlideUtil.loadCornerImg(ivHead, Constant.currentUser.getHeadPortrait(), 5);
        tvName.setText(Constant.currentUser.getNick());

        selectPopupWindow = new SelectPopupWindow(this, (psw, complete) -> {
            if (complete) {
                ServiceFactory.getInstance().getBaseService(Api.class)
                        .onPoints(groupId, et.getText().toString().trim(), MD5Utils.getMD5(psw))
                        .compose(bindToLifecycle())
                        .compose(RxSchedulers.normalTrans())
                        .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                        .subscribe(s -> {
                            Intent intent = new Intent(this, GameUpScoreConfirmActivity.class);
                            intent.putExtra("hk", et.getText().toString().trim());
                            startActivity(intent);
                            finish();
                        }, this::handleApiError);
            }
        });
    }


    //确认上分
    public void commit(View view) {
        String trim = et.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            ToastUtils.showShort(R.string.inputupscore);
            return;
        }

        KeyboardUtils.hideSoftInput(this);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        selectPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
    }
}
