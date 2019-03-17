package com.zxjk.duoduo.network.rx;

import android.app.Dialog;

import com.zxjk.duoduo.network.response.BaseResponse;
import com.zxjk.duoduo.utils.CommonUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.zxjk.duoduo.Constant.CODE_SUCCESS;
import static com.zxjk.duoduo.Constant.CODE_UNLOGIN;

public class RxSchedulers {
    public static <T> ObservableTransformer<T, T> ioObserver() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<BaseResponse<T>, T> normalTrans() {
        return upstream -> upstream.flatMap((Function<BaseResponse<T>, ObservableSource<T>>) response -> {
            if (response.code == CODE_SUCCESS) {
                return Observable.just(response.data);
            } else if (response.code == CODE_UNLOGIN) {
                return Observable.error(new RxException.DuplicateLoginExcepiton("重复登录"));
            }
            return Observable.error(new RxException.ParamsException(response.msg));
        });
    }

    public static <T> ObservableTransformer<T, T> showLoading(Dialog d) {
        return upstream -> upstream.doOnSubscribe(disposable -> d.show()).doOnError(throwable -> {
            if (d != null) CommonUtils.destoryDialog();
        }).doOnNext(t -> {
            if (d != null) CommonUtils.destoryDialog();
        });
    }

}
