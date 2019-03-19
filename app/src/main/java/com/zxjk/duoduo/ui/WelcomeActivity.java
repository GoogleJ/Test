package com.zxjk.duoduo.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.liang.permission.annotation.Permission;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.RxUtils;

import com.zxjk.duoduo.utils.UserPreferencesUtils;
import com.zxjk.duoduo.utils.WindowUtils;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 这里是欢迎首页
 * @author Administrator
 */
public class WelcomeActivity extends BaseActivity {

    private Disposable disposable;
    private boolean isGoPermission;
    @Override
    protected void onResume() {
        super.onResume();
        if (isGoPermission) {
            return;
        }
        isGoPermission = true;
        checkUserState();
    }
    @Permission({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE})
    public void checkUserState() {
        Log.e("checkUserState", "checkUserState: ...");
        disposable = Flowable.timer(1000, TimeUnit.MILLISECONDS)
                .compose(RxUtils.<Long>flyableTransformer())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) {
                        String token = UserPreferencesUtils.getInstance(WelcomeActivity.this).getUserToken();
                        String rongToken = UserPreferencesUtils.getInstance(WelcomeActivity.this).getRongToken();
                        if (token.isEmpty() || rongToken.isEmpty()) {
                            goLoginByServer();
                        } else {
                            goLoginByIM(rongToken);
                        }
                    }
                });

        RxUtils.addDisposable(this, disposable);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void goLoginByIM(String rongToken) {
//        RongIM.connect(rongToken, SealAppContext.getInstance().getConnectCallback(this));
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        WindowUtils.hideBottomUIMenu(this);
    }

    @NonNull
    private void goLoginByServer() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        getWindow().setBackgroundDrawable(null);
    }


}
