package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.LoginActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;

import io.rong.imkit.RongIM;

/**
 * 这里是关于设置的activity
 *
 * @author Administrator
 */
@SuppressLint("CheckResult")
public class SettingActivity extends BaseActivity {

    private TextView tv_perfection;
    private ImageView iv_authentication;
    private TextView tv_authentication;

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
        tv_authentication.setText(CommonUtils.getAuthenticate(Constant.currentUser.getIsAuthentication()));
        iv_authentication.setVisibility(Constant.currentUser.getIsAuthentication().equals("0") ? View.VISIBLE : View.GONE);

        if (SPUtils.getInstance().getBoolean(Constant.currentUser.getId(), false)) {
            tv_perfection.setText(getString(R.string.complete_payinfo));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initView() {
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("设置");
        RelativeLayout rl_back = findViewById(R.id.rl_back);
        tv_authentication = findViewById(R.id.tv_authentication);
        iv_authentication = findViewById(R.id.iv_authentication);
        TextView tv_nickName = findViewById(R.id.tv_nickName);
        tv_nickName.setText(Constant.currentUser.getNick());
        boolean hasCompletePay = SPUtils.getInstance().getBoolean(Constant.currentUser.getId(), false);
        tv_perfection = findViewById(R.id.tv_perfection);
        tv_perfection.setText(hasCompletePay ? R.string.complete_payinfo : R.string.uncomplete_payinfo);
        //返回
        rl_back.setOnClickListener(v -> finish());
        //账号
        findViewById(R.id.rl_account_number).setOnClickListener(v ->
                startActivity(new Intent(SettingActivity.this, AccountActivity.class)));
        //新消息通知
        findViewById(R.id.rl_newMessage).setOnClickListener(v -> {

        });
        //隐私
        findViewById(R.id.rl_intimacy).setOnClickListener(v ->
                startActivity(new Intent(SettingActivity.this, PrivacyActivity.class)));
        //实名认证
        findViewById(R.id.rl_realNameAuthentication).setOnClickListener(v -> {
            if (Constant.currentUser.getIsAuthentication().equals("2")) {
                ToastUtils.showShort(R.string.verifying_pleasewait);
            } else if (!Constant.currentUser.getIsAuthentication().equals("0") &&
                    !Constant.currentUser.getIsAuthentication().equals("1")) {
                startActivity(new Intent(SettingActivity.this, VerifiedActivity.class));
            }
        });
        //收款信息
        findViewById(R.id.rl_collectionInformation).setOnClickListener(v -> {
            String isAuthentication = Constant.currentUser.getIsAuthentication();
            if ("0".equals(isAuthentication)) {
                startActivity(new Intent(SettingActivity.this, BillingMessageActivity.class));
            } else if ("2".equals(isAuthentication)) {
                ToastUtils.showShort(R.string.waitAuthentication);
            } else {
                ToastUtils.showShort(R.string.notAuthentication);
            }
        });
        //帮助中心
        findViewById(R.id.rl_helpCenter).setOnClickListener(v ->
                startActivity(new Intent(SettingActivity.this, HelpActivity.class)));
        //意见反馈
        findViewById(R.id.rl_feedback).setOnClickListener(v ->
                startActivity(new Intent(SettingActivity.this, FeedbackActivity.class)));
        //关于多多
        findViewById(R.id.rl_aboutDuoDuo).setOnClickListener(v ->
                startActivity(new Intent(SettingActivity.this, AboutActivity.class)));
        //语言切换
        findViewById(R.id.rl_languageSwitch).setOnClickListener(v -> {

        });
        //在线客服
        findViewById(R.id.rl_onlineCustomerService).setOnClickListener(v ->
                startActivity(new Intent(SettingActivity.this, OnlineServiceActivity.class)));
        //切换账号
        findViewById(R.id.rl_switchAccount).setOnClickListener(v ->
                startActivity(new Intent(SettingActivity.this, AccountSwitchActivity.class)));
        //退出
        findViewById(R.id.rl_logout).setOnClickListener(v -> NiceDialog.init().setLayoutId(R.layout.layout_general_dialog).setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                holder.setText(R.id.tv_title, "提示");
                holder.setText(R.id.tv_content, "您将退出登录");
                holder.setText(R.id.tv_cancel, "取消");
                holder.setText(R.id.tv_notarize, "确认");
                holder.setOnClickListener(R.id.tv_cancel, v1 -> dialog.dismiss());
                holder.setOnClickListener(R.id.tv_notarize, v12 -> {
                    dialog.dismiss();
                    ServiceFactory.getInstance().getBaseService(Api.class)
                            .loginOut()
                            .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(SettingActivity.this)))
                            .compose(RxSchedulers.normalTrans())
                            .subscribe(s -> {
                                RongIM.getInstance().disconnect();
                                Constant.clear();
                                ToastUtils.showShort(R.string.login_out);
                                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }, SettingActivity.this::handleApiError);
                });


            }
        }).setOutCancel(false).show(getSupportFragmentManager()));

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
                        switch (s) {
                            case "0":
                                tv_authentication.setText("已认证");
                                iv_authentication.setVisibility(View.VISIBLE);
                                break;
                            case "2":
                                tv_authentication.setText("认证审核中");
                                iv_authentication.setVisibility(View.GONE);
                                break;
                            case "1":
                                tv_authentication.setText("认证未通过");
                                iv_authentication.setVisibility(View.GONE);
                                break;
                            default:
                                tv_authentication.setText("未认证");
                                iv_authentication.setVisibility(View.GONE);
                                break;
                        }
                    }, this::handleApiError);
        }
    }


}
