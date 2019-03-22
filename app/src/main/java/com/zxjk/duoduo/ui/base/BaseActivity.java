package com.zxjk.duoduo.ui.base;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.rx.RxException;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

@SuppressLint({"CheckResult", "Registered"})
public class BaseActivity extends RxAppCompatActivity {

    public void handleApiError(Throwable throwable) {
        if (throwable instanceof RxException.DuplicateLoginExcepiton) {
            //TODO code601，后续考虑如何处理(弹对话框、强制退出)
            LogUtils.e("已经登录，提示用户退出");
            Constant.currentUser = new LoginResponse();
            return;
        }

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
                    if (onFinish != null) onFinish.onFinish(result);
                });
    }

}
