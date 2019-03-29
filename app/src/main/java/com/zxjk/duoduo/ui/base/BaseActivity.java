package com.zxjk.duoduo.ui.base;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.rx.RxException;
import com.zxjk.duoduo.utils.ActivityCollector;
import com.zxjk.duoduo.utils.LoginOutBroadcastReceiver;

import java.io.File;
import java.util.List;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

@SuppressLint({"CheckResult", "Registered"})
public class BaseActivity extends RxAppCompatActivity {

    protected LoginOutBroadcastReceiver locallReceiver;
    public void handleApiError(Throwable throwable) {
        if (throwable instanceof RxException.DuplicateLoginExcepiton) {
            //TODO code601，后续考虑如何处理(弹对话框、强制退出)
            LogUtils.e("已经登录，提示用户退出");
            Constant.currentUser = new LoginResponse();
            return;
        }
        LogUtils.d("DEBUG",throwable.getMessage());

        ToastUtils.showShort(RxException.getMessage(throwable));
    }

    public interface OnZipFileFinish {
        void onFinish(List<File> result);
    }

    public void zipFile(List<String> files, OnZipFileFinish onFinish) {
        Observable.just(files)
                .observeOn(Schedulers.io())
                .map(origin -> Luban.with(Utils.getApp()).load(origin).get())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(result -> {
                    if (onFinish != null) {
                        onFinish.onFinish(result);
                    }
                });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 注册广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.zxjk.duoduo.logout");
        locallReceiver = new LoginOutBroadcastReceiver();
        registerReceiver(locallReceiver, intentFilter);

    }
    @Override
    protected void onPause() {
        super.onPause();

        // 取消注册广播接收器
        unregisterReceiver(locallReceiver);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 销毁活动时，将其从管理器中移除
        ActivityCollector.removeActivity(this);
    }


}
