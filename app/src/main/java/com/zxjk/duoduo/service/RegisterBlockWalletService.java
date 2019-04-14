package com.zxjk.duoduo.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.response.CreateWalletResponse;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.BaseResponse;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.rx.RxException;
import com.zxjk.duoduo.network.rx.RxSchedulers;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RegisterBlockWalletService extends Service {
    private String walletAddress = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("CheckResult")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .createWallet(Constant.currentUser.getDuoduoId())
                .compose(RxSchedulers.normalTrans())
                .subscribeOn(Schedulers.io())
                .flatMap((Function<CreateWalletResponse, ObservableSource<BaseResponse<LoginResponse>>>) s -> {
                    walletAddress = s.getWalletAddress();
                    LoginResponse loginResponse = new LoginResponse(Constant.currentUser.getId());
                    loginResponse.setWalletAddress(walletAddress);
                    return ServiceFactory.getInstance().getBaseService(Api.class)
                            .updateUserInfo(new Gson().toJson(loginResponse));
                })
                .subscribe(s -> {
                    Constant.currentUser.setWalletAddress(walletAddress);
                    stopSelf();
                }, t -> {
                    Observable.timer(20, TimeUnit.SECONDS)
                            .subscribe(aLong -> startService(intent), throwable -> {
                            });
                });

        return super.onStartCommand(intent, flags, startId);
    }
}
