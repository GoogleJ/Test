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

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint({"CheckResult", "SetTextI18n"})
public class GameUpScoreActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ivHead)
    ImageView ivHead;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.et)
    EditText et;
    @BindView(R.id.tv_balance)
    TextView tvBalance;

    private String balance;
    private String groupId;
    private SelectPopupWindow selectPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_up_score);
        ButterKnife.bind(this);
        initView();
        initData();
    }


    private void initView() {
        tvTitle.setText(getString(R.string.upscore));
        groupId = getIntent().getStringExtra("groupId");
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


    private void initData() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getBalanceHk()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(b -> {
                    tvBalance.setText("当前可上分总额 " + b.getBalanceHk() + " HK");
                    balance = b.getBalanceHk();
                }, this::handleApiError);
    }


    @OnClick({R.id.rl_back, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_confirm:
                String trim = et.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    ToastUtils.showShort(R.string.inputupscore);
                    return;
                }
                if (Double.parseDouble(trim) == 0) {
                    ToastUtils.showShort(R.string.inputupscore1);
                    return;
                }
                BigDecimal data1 = new BigDecimal(et.getText().toString());
                BigDecimal data2 = new BigDecimal(balance);
                if (data1.compareTo(data2) > 0) {
                    ToastUtils.showShort("上分金额不能大于当前可上分总额");
                    return;
                }

                KeyboardUtils.hideSoftInput(this);
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                int winHeight = getWindow().getDecorView().getHeight();
                selectPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);


                break;
        }
    }
}
