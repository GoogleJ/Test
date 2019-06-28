package com.zxjk.duoduo.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Application;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.export.ExportConfirmOrderActivty;
import com.zxjk.duoduo.export.ExportLoginActivty;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.MMKVUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * author L
 * create at 2019/5/13
 * description: 启动页
 */
public class WelcomeActivity extends BaseActivity {

    @SuppressLint("CheckResult")
    public void checkUserState() {
        if (MMKVUtils.getInstance().decodeBool("isLogin")) {
            goLoginByServer();
        } else {
            Observable.timer(2000, TimeUnit.MILLISECONDS)
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.ioObserver())
                    .subscribe(aLong -> {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    });
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (TextUtils.isEmpty(Constant.phoneUuid)) {
            Constant.phoneUuid = TextUtils.isEmpty(DeviceUtils.getMacAddress()) ? DeviceUtils.getAndroidID() : DeviceUtils.getMacAddress();
        }
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action != null && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        ScreenUtils.setFullScreen(this);
        setContentView(R.layout.activity_welcome);

        if (getIntent().getBooleanExtra("export", false)) {
            long date2 = TimeUtils.getNowMills();
            long date1 = MMKVUtils.getInstance().decodeLong("date1");
            if (MMKVUtils.getInstance().decodeBool("isLogin") &&
                    ((date2 - date1) / (24 * 60 * 60 * 1000) < 7)) {
                Constant.currentUser =
                        MMKVUtils.getInstance().decodeParcelable("login");
                Constant.token = Constant.currentUser.getToken();
                Constant.userId = Constant.currentUser.getId();
                getIntent().setClass(this, ExportConfirmOrderActivty.class);
            } else {
                getIntent().setClass(this, ExportLoginActivty.class);
            }

            Observable.timer(2000, TimeUnit.MILLISECONDS)
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.ioObserver())
                    .subscribe(l -> {
                        startActivity(getIntent());
                        finish();
                    });
            return;
        }

        checkUserState();
    }

    private void goLoginByServer() {
        long date2 = TimeUtils.getNowMills();
        long date1 = MMKVUtils.getInstance().decodeLong("date1");
        if ((date2 - date1) / (24 * 60 * 60 * 1000) < 7) {
            Constant.currentUser =
                    MMKVUtils.getInstance().decodeParcelable("login");
            Constant.token = Constant.currentUser.getToken();
            Constant.userId = Constant.currentUser.getId();
            // 连接融云
            if (getApplicationInfo().packageName.equals(Application.getCurProcessName(getApplicationContext()))) {
                RongIM.connect(Constant.currentUser.getRongToken(), new RongIMClient.ConnectCallback() {

                    @Override
                    public void onTokenIncorrect() {
                        MMKVUtils.getInstance().enCode("isLogin", false);
                        Constant.clear();
                        ToastUtils.showShort(getString(R.string.login_again));
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onSuccess(String userid) {
                        UserInfo userInfo = new UserInfo(userid, Constant.currentUser.getNick(), Uri.parse(Constant.currentUser.getHeadPortrait()));
                        RongIM.getInstance().setCurrentUserInfo(userInfo);
                        startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        MMKVUtils.getInstance().enCode("isLogin", false);
                        Constant.clear();
                        ToastUtils.showShort(getString(R.string.login_again));
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        } else {
            ToastUtils.showShort(getString(R.string.login_again));
            MMKVUtils.getInstance().enCode("isLogin", false);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

}
