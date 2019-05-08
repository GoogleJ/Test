package com.zxjk.duoduo.ui.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.jakewharton.rxbinding3.view.RxView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle3.components.support.RxFragment;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.network.rx.RxException;
import com.zxjk.duoduo.ui.LoginActivity;
import com.zxjk.duoduo.ui.widget.dialog.ReLoginDialog;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.autosize.internal.CustomAdapt;
import top.zibin.luban.Luban;

/**
 * @author Administrator
 */
@SuppressLint("CheckResult")
public class BaseFragment extends RxFragment  {


    public View rootView;

    public RxPermissions rxPermissions;

    public void getPermisson(String... permissions) {
        getPermisson(null, null, permissions);
    }

    public void getPermisson(View view, BaseActivity.PermissionResult result, String... permissions) {
        if (view != null) {
            RxView.clicks(view)
                    .compose(rxPermissions.ensureEachCombined(permissions))
                    .compose(bindToLifecycle())
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
                .compose(bindToLifecycle())
                .compose(rxPermissions.ensure(permissions))
                .subscribe(granted -> {
                    if (!granted) {
                        ToastUtils.showShort("请开启相关权限");
                    }
                    if (null != result) {
                        result.onResult(granted);
                    }
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        rxPermissions = new RxPermissions(this);
    }

    public void handleApiError(Throwable throwable) {
        if (throwable.getCause() instanceof RxException.DuplicateLoginExcepiton ||
                throwable instanceof RxException.DuplicateLoginExcepiton) {
            // 重复登录，挤掉线
            ReLoginDialog reLoginDialog = new ReLoginDialog(getActivity());
            reLoginDialog.setOnClickListener(() -> {
                Constant.clear();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    public void zipFile(List<String> files, BaseActivity.OnZipFileFinish onFinish) {
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
