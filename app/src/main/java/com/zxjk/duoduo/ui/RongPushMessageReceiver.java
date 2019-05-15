package com.zxjk.duoduo.ui;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.zxjk.duoduo.Constant;

import io.rong.push.PushType;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

public class RongPushMessageReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
        if (TextUtils.isEmpty(Constant.userId)) {
            context.startActivity(new Intent(context,WelcomeActivity.class));
            return true;
        }
        return false;
    }
}
