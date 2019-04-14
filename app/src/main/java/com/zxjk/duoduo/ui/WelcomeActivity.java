package com.zxjk.duoduo.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.WindowUtils;
import java.util.concurrent.TimeUnit;
import androidx.annotation.Nullable;
import io.reactivex.Observable;

/**
 * 这里是欢迎首页
 *
 * @author Administrator
 */
public class WelcomeActivity extends BaseActivity {

    @SuppressLint("CheckResult")
    public void checkUserState() {
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .subscribe(aLong -> goLoginByServer());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        WindowUtils.hideBottomUIMenu(this);
        checkUserState();
    }

    private void goLoginByServer() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
