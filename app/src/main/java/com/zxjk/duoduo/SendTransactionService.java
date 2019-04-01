package com.zxjk.duoduo;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import androidx.annotation.Nullable;
import io.reactivex.schedulers.Schedulers;

public class SendTransactionService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("CheckResult")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String arg1 = intent.getStringExtra("arg1");
        String arg2 = intent.getStringExtra("arg2");
        ServiceFactory.getInstance().getBaseService(Api.class)
                .sendTransaction(arg1, arg2)
                .subscribeOn(Schedulers.io())
                .subscribe(s -> stopSelf(), t -> stopSelf());
        return super.onStartCommand(intent, flags, startId);
    }
}
