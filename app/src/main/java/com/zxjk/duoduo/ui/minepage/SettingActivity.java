package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.zxjk.duoduo.ui.msgpage.AuthenticationActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.MMKVUtils;

import io.rong.imkit.RongIM;

/**
 * author L
 * create at 2019/5/7
 * description:设置
 */

@SuppressLint("CheckResult")
public class SettingActivity extends BaseActivity {

    private TextView tv_perfection;
    private ImageView iv_authentication;
    private TextView tv_authentication;
    private String otherIdCardType = "";

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

    private void initView() {
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("设置");
        RelativeLayout rl_back = findViewById(R.id.rl_back);

        tv_authentication = findViewById(R.id.tv_authentication);
        iv_authentication = findViewById(R.id.iv_authentication);
        boolean hasCompletePay = SPUtils.getInstance().getBoolean(Constant.currentUser.getId(), false);
        tv_perfection = findViewById(R.id.tv_perfection);
        tv_perfection.setText(hasCompletePay ? R.string.complete_payinfo : R.string.uncomplete_payinfo);

        //返回
        rl_back.setOnClickListener(v -> finish());

        //账号
        findViewById(R.id.rl_account_number).setOnClickListener(v ->
                startActivity(new Intent(SettingActivity.this, AccountActivity.class)));

        //隐私
        findViewById(R.id.rl_privicy).setOnClickListener(v ->
                startActivity(new Intent(SettingActivity.this, PrivicyActivity.class)));

        //新消息通知
        findViewById(R.id.rl_newMessage).setOnClickListener(v -> {
            Intent mItent = new Intent();
            mItent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mItent.setData(Uri.fromParts("package", getPackageName(), null));
            startActivity(mItent);
        });

        //实名认证
        findViewById(R.id.rl_realNameAuthentication).setOnClickListener(v -> {
            if (Constant.currentUser.getIsAuthentication().equals("2")) {
                ToastUtils.showShort(R.string.verifying_pleasewait);
            } else if (Constant.currentUser.getIsAuthentication().equals("0")) {
                ToastUtils.showShort(R.string.authen_true);
            } else {
                NiceDialog.init().setLayoutId(R.layout.layout_general_dialog11).setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        otherIdCardType = "";
                        ImageView iv_idCard = holder.getView(R.id.iv_idCard);
                        ImageView iv_passport = holder.getView(R.id.iv_passport);
                        ImageView iv_other = holder.getView(R.id.iv_other);
                        holder.setOnClickListener(R.id.ll_idCard, v13 -> {
                            iv_idCard.setImageResource(R.drawable.ic_radio_select);
                            iv_passport.setImageResource(R.drawable.ic_radio_unselect);
                            iv_other.setImageResource(R.drawable.ic_radio_unselect);
                            otherIdCardType = "1";
                        });
                        holder.setOnClickListener(R.id.ll_passport, v14 -> {
                            iv_idCard.setImageResource(R.drawable.ic_radio_unselect);
                            iv_passport.setImageResource(R.drawable.ic_radio_select);
                            iv_other.setImageResource(R.drawable.ic_radio_unselect);
                            otherIdCardType = "2";
                        });
                        holder.setOnClickListener(R.id.ll_other, v16 -> {
                            iv_idCard.setImageResource(R.drawable.ic_radio_unselect);
                            iv_passport.setImageResource(R.drawable.ic_radio_unselect);
                            iv_other.setImageResource(R.drawable.ic_radio_select);
                            otherIdCardType = "3";
                        });
                        holder.setOnClickListener(R.id.tv_confirm, v15 -> {
                            if (!TextUtils.isEmpty(otherIdCardType)) {
                                dialog.dismiss();
                                if (otherIdCardType.equals("1")) {
                                    Intent intent = new Intent(SettingActivity.this, AuthenticationActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(SettingActivity.this, VerifiedActivity.class);
                                    intent.putExtra("otherIdCardType", otherIdCardType);
                                    startActivity(intent);
                                }
                            } else {
                                ToastUtils.showShort("请选择证件类型");
                            }
                        });
                    }
                }).setDimAmount(0.5f).setOutCancel(true).show(getSupportFragmentManager());
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
        //退出
        findViewById(R.id.tv_login).setOnClickListener(v -> NiceDialog.init().setLayoutId(R.layout.layout_general_dialog).setConvertListener(new ViewConvertListener() {
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
                                RongIM.getInstance().logout();
                                MMKVUtils.getInstance().enCode("isLogin", false);
                                Constant.clear();
                                ToastUtils.showShort(R.string.login_out);
                                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }, SettingActivity.this::handleApiError);
                });

            }
        }).setDimAmount(0.5f).setOutCancel(false).show(getSupportFragmentManager()));
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
                        MMKVUtils.getInstance().enCode("login", Constant.currentUser);
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
