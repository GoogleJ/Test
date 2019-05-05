package com.zxjk.duoduo.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.TimeUtils;
import com.zxjk.duoduo.Application;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.MMKVUtils;
import com.zxjk.duoduo.utils.WindowUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * 这里是欢迎首页
 *
 * @author Administrator
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
        if (!isTaskRoot()) {
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
        setContentView(R.layout.activity_welcome);
//        Constant.isVerifyVerision = SPUtils.getInstance().getBoolean("isVerifyVerision", true);
//        ServiceFactory.getInstance().getBaseService(Api.class)
//                .getAppVersion()
//                .compose(bindToLifecycle())
//                .compose(RxSchedulers.ioObserver())
//                .subscribe(response -> {
//                    if (response.data.equals("0.0.1")) {
//                        Constant.isVerifyVerision = true;
//                    } else {
//                        Constant.isVerifyVerision = false;
//                        SPUtils.getInstance().put("isVerifyVerision", false);
//                    }
//                }, t -> {
//                });
        WindowUtils.hideBottomUIMenu(this);
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

                    }
                });
            }

        } else {
            MMKVUtils.getInstance().enCode("isLogin", false);
            startActivity(new Intent(this, LoginActivity.class));
        }

    }
}
