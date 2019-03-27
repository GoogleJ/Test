package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.BaseResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import androidx.annotation.RequiresApi;
import io.reactivex.functions.Consumer;
import okhttp3.Response;

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

    LinearLayout verifiedLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getVerified();
        verifiedLayout=findViewById(R.id.verified);
        tvSettingAuthenticate = findViewById(R.id.tvSettingAuthenticate);
        tvSettingPayment = findViewById(R.id.tvSettingPayment);
        tvSettingNick = findViewById(R.id.tvSettingNick);
        ivSettingAuthen = findViewById(R.id.ivSettingAuthen);

        if (Constant.currentUser.getRealname().equals("")) {
            tvSettingAuthenticate.setText(R.string.authen_false);
        } else {
            tvSettingAuthenticate.setText(R.string.authen_true);
        }
        tvSettingNick.setText(Constant.currentUser.getNick());

        initView();
    }

    private void initView() {
        verifiedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this,VerifiedActivity.class));
            }
        });
    }

    String actionReceiver = "com.zxjk.duoduo.logout";

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
        startActivity(new Intent(this, BillingMessageActivity.class));
    }

    /**
     * 退出登录
     *
     * @param view
     */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void loginOut(View view) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .loginOut()
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    ToastUtils.showShort(R.string.login_out);
                    Intent intent = new Intent(actionReceiver);
                    sendBroadcast(intent);
//                    ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//                    boolean res = am.clearApplicationUserData();
//                    if (!res) {
//                    }
                }, this::handleApiError);
    }

    /**
     * 跳转客服
     *
     * @param view
     */
    public void onlineService(View view) {
        Uri uri = Uri.parse(Constant.currentUser.getOnlineService());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    public void back(View view) {
        finish();
    }
    public void getVerified(){
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getCustomerAuth()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {

                    //已认证
                    String verified="0";
                    //审核中
                    String underReview="2";
                    String notVerified="1";
                    Constant.verifiedBean.setState(s);
                    if (verified.equals(s)){
                        verifiedLayout.setClickable(false);
                        ivSettingAuthen.setVisibility(View.VISIBLE);
                        tvSettingAuthenticate.setText(R.string.verified_successful);
                    }else if (underReview.equals(s)){
                        verifiedLayout.setClickable(false);
                        tvSettingAuthenticate.setText(R.string.under_review);
                    }else if (notVerified.equals(s)){
                        //其他均为未认证
                        verifiedLayout.setClickable(true);
                        tvSettingAuthenticate.setText(R.string.not_verified);
                    }else{
                        //其他均为未认证
                        verifiedLayout.setClickable(true);
                        tvSettingAuthenticate.setText(R.string.authen_false);
                    }

                },this::handleApiError);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getVerified();
    }
}
