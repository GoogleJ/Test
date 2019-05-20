package com.zxjk.duoduo.ui.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.jakewharton.rxbinding3.view.RxView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.network.rx.RxException;
import com.zxjk.duoduo.ui.LoginActivity;
import com.zxjk.duoduo.ui.widget.dialog.ReLoginDialog;
import com.zxjk.duoduo.utils.MMKVUtils;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.rong.imkit.RongIM;
import top.zibin.luban.Luban;

@SuppressLint({"CheckResult", "Registered"})
public class BaseActivity extends RxAppCompatActivity {
    public RxPermissions rxPermissions = new RxPermissions(this);

    public interface PermissionResult {
        void onResult(boolean granted);
    }

    public void getPermisson(PermissionResult result, String... permissions) {
        getPermisson(null, result, permissions);
    }

    public void getPermisson(View view, PermissionResult result, String... permissions) {
        if (view != null) {
            RxView.clicks(view)
                    .compose(rxPermissions.ensureEachCombined(permissions))
                    .subscribe(permission -> {
                        if (!permission.granted) {
                            ToastUtils.showShort("请开启相关权限");
                        }
                        if (null != result) {
                            result.onResult(permission.granted);
                        }
                    });
            return;
        }
        rxPermissions.request(permissions)
                .compose(rxPermissions.ensureEachCombined(permissions))
                .subscribe(granted -> {
                    if (!granted.granted) {
                        ToastUtils.showShort("请开启相关权限");
                    }
                    if (null != result) {
                        result.onResult(granted.granted);
                    }
                });
    }

    public void handleApiError(Throwable throwable) {
        if (throwable.getCause() instanceof RxException.DuplicateLoginExcepiton ||
                throwable instanceof RxException.DuplicateLoginExcepiton) {
            // 重复登录，挤掉线
            RongIM.getInstance().disconnect();
            Constant.clear();
            MMKVUtils.getInstance().enCode("isLogin", false);

            ReLoginDialog reLoginDialog = new ReLoginDialog(this);
            reLoginDialog.setOnClickListener(() -> {
                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
            reLoginDialog.show();
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
                    if (onFinish != null) {
                        onFinish.onFinish(result);
                    }
                });
    }
}
