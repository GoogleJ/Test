package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.LoginActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.dialog.ConfirmDialog;
import com.zxjk.duoduo.utils.CommonUtils;

import io.rong.imkit.RongIM;

/**
 * 这里是关于设置的activity
 *
 * @author Administrator
 */
@SuppressLint("CheckResult")
public class SettingActivity extends BaseActivity {

    private TextView tvSettingAuthenticate;
    private TextView tvSettingPayment;
    private TextView tvSettingNick;

    private ImageView ivSettingAuthen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();

        isAuthentication();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvSettingAuthenticate.setText(CommonUtils.getAuthenticate(Constant.currentUser.getIsAuthentication()));
        ivSettingAuthen.setVisibility(Constant.currentUser.getIsAuthentication().equals("0") ? View.VISIBLE : View.GONE);
    }

    public void gotoVerivy(View view) {
        if (Constant.currentUser.getIsAuthentication().equals("2")) {
            ToastUtils.showShort(R.string.verifying_pleasewait);
        } else if (Constant.currentUser.getIsAuthentication().equals("1")) {
            startActivity(new Intent(this, VerifiedActivity.class));
        }
    }

    private void isAuthentication() {
        if (!Constant.currentUser.getIsAuthentication().equals("0")) {
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getCustomerAuth()
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .subscribe(s -> {
                        Constant.currentUser.setIsAuthentication(s);
                        if (s.equals("0")) {
                            tvSettingAuthenticate.setText("已认证");
                        } else if (s.equals("2")) {
                            tvSettingAuthenticate.setText("认证审核中");
                        } else {
                            tvSettingAuthenticate.setText("未认证");
                        }
                    }, this::handleApiError);
        }
    }

    private void initView() {
        boolean hasCompletePay = SPUtils.getInstance().getBoolean(Constant.currentUser.getId(), false);
        tvSettingAuthenticate = findViewById(R.id.tvSettingAuthenticate);
        tvSettingPayment = findViewById(R.id.tvSettingPayment);
        tvSettingNick = findViewById(R.id.tvSettingNick);
        ivSettingAuthen = findViewById(R.id.ivSettingAuthen);
        tvSettingNick.setText(Constant.currentUser.getNick());
        tvSettingPayment.setText(hasCompletePay ? R.string.complete_payinfo : R.string.uncomplete_payinfo);

    }


    //账户
    public void jump2Account(View view) {
        startActivity(new Intent(this, AccountActivity.class));
    }

    /**
     * 意见反馈
     *
     * @param view
     */
    public void feedback(View view) {
        startActivity(new Intent(this, FeedbackActivity.class));
    }

    /**
     * 账号切换
     *
     * @param view
     */
    public void accountSwitch(View view) {
        startActivity(new Intent(this, AccountSwitchActivity.class));
    }

    /**
     * 关于嘟嘟界面
     *
     * @param view
     */
    public void aboutDuoDuo(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }

    public void jump2Help(View view) {
        startActivity(new Intent(this, HelpActivity.class));
    }

    /**
     * 隐私界面的跳转
     *
     * @param view
     */
    public void privacy(View view) {
        startActivity(new Intent(this, PrivacyActivity.class));
    }

    /***
     * 收款信息页面跳转
     * @param view
     */
    public void billingMessage(View view) {
        String isAuthentication = Constant.currentUser.getIsAuthentication();
        if ("0".equals(isAuthentication)) {
            startActivity(new Intent(this, BillingMessageActivity.class));
        } else if ("1".equals(isAuthentication)) {
            ToastUtils.showShort(R.string.notAuthentication);
        } else {
            ToastUtils.showShort(R.string.waitAuthentication);
        }
    }

    /**
     * 退出登录
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void loginOut(View view) {
        ConfirmDialog dialog = new ConfirmDialog(this, "提示", "您将退出登录", v -> ServiceFactory.getInstance().getBaseService(Api.class)
                .loginOut()
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    RongIM.getInstance().disconnect();
                    Constant.clear();
                    ToastUtils.showShort(R.string.login_out);
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }, this::handleApiError));
        dialog.show();
    }

    /**
     * 跳转客服
     *
     * @param view
     */
    public void onlineService(View view) {
        startActivity(new Intent(this, OnlineServiceActivity.class));
    }

    public void back(View view) {
        finish();
    }


}
