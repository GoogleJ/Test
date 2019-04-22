package com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.zxjk.duoduo.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class GameStartPlugin implements IPluginModule {
    private volatile boolean finish = true;
    private String textContent;

    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.ic_plugin_game_start);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.game_start);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        if (!finish) {
            return;
        }
        finish = false;

        Observable.interval(1, TimeUnit.SECONDS)
                .take(4)
                .subscribe(aLong -> {
                    if (aLong == 3) {
                        textContent = "开始下注";
                    } else {
                        textContent = "开始下注了!!!倒计时" + (3 - aLong);
                    }
                    TextMessage myTextMessage = TextMessage.obtain(textContent);
                    if (aLong == 3) {
                        myTextMessage.setExtra("pass");
                        finish = true;
                    }
                    Message myMessage = Message.obtain(rongExtension.getTargetId(), Conversation.ConversationType.GROUP, myTextMessage);
                    RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
                        @Override
                        public void onAttached(Message message) {

                        }

                        @Override
                        public void onSuccess(Message message) {

                        }

                        @Override
                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }, throwable -> finish = true);
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
