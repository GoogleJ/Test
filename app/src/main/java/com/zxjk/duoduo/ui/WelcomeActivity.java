package com.zxjk.duoduo.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.WindowUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * 这里是欢迎首页
 *
 * @author Administrator
 */
public class WelcomeActivity extends BaseActivity {

    @SuppressLint("CheckResult")
    public void checkUserState() {
        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .subscribe(aLong -> goLoginByServer());
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
        Constant.isVerifyVerision = SPUtils.getInstance().getBoolean("isVerifyVerision", true);
        if (Constant.isVerifyVerision) {
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getAppVersion()
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.ioObserver())
                    .subscribe(response -> {
                        if (response.equals("0.0.1")) {
                            Constant.isVerifyVerision = true;
                        } else {
                            Constant.isVerifyVerision = false;
                            SPUtils.getInstance().put("isVerifyVerision", false);
                        }
                    }, t -> {
                    });
        }
        WindowUtils.hideBottomUIMenu(this);
        checkUserState();
    }

    private void goLoginByServer() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
