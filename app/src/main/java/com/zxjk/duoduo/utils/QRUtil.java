package com.zxjk.duoduo.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;

import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

@SuppressLint("CheckResult")
public class QRUtil {

    public static void decode(BaseActivity activity, Bitmap b, Consumer<String> consumer) {
        Observable.create((ObservableOnSubscribe<String>) e -> e.onNext(QRCodeDecoder.syncDecodeQRCode(b)))
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(activity)))
                .subscribe(consumer);
    }

    public static void decode(BaseActivity activity, String url, Consumer<String> consumer) {
        Observable.create((ObservableOnSubscribe<String>) e -> {
            FutureTarget<Bitmap> futureBitmap = Glide.with(activity)
                    .asBitmap()
                    .load(url)
                    .submit();
            e.onNext(QRCodeDecoder.syncDecodeQRCode(futureBitmap.get()));
        }).compose(RxSchedulers.ioObserver(CommonUtils.initDialog(activity))).subscribe(consumer);
    }
}
