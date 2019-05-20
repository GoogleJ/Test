package com.zxjk.duoduo.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.Lifecycle;

import com.blankj.utilcode.util.ToastUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

public class WeChatShareUtil {

    public static IWXAPI wxShare;

    @SuppressLint("CheckResult")
    public static void shareImg(BaseActivity activity, int targetScene) {
        if (!wxShare.isWXAppInstalled()) {
            ToastUtils.showShort("您还没有安装微信");
            return;
        }

        LifecycleProvider<Lifecycle.Event> provider = AndroidLifecycle.createLifecycleProvider(activity);

        Observable.create((ObservableOnSubscribe<SendMessageToWX.Req>) emitter -> {
            Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.shareimg_wechat);
            Bitmap mBp = Bitmap.createScaledBitmap(bmp, 540, 960, true);

            WXImageObject imgObj = new WXImageObject(mBp);
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObj;
            msg.title = "多多优社";
            bmp.recycle();
            mBp.recycle();
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = targetScene;

            emitter.onNext(req);
        })
                .compose(provider.bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(activity, "处理图片中")))
                .subscribe(req -> wxShare.sendReq(req));
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
