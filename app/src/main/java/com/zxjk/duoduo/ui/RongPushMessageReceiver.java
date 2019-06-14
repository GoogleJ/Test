package com.zxjk.duoduo.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import com.blankj.utilcode.util.AppUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.utils.badge.BadgeNumberManager;
import com.zxjk.duoduo.utils.badge.BadgeNumberManagerXiaoMi;
import com.zxjk.duoduo.utils.badge.MobileBrand;

import io.rong.push.PushType;
import io.rong.push.RongPushClient;
import io.rong.push.common.PushCacheHelper;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

import static io.rong.push.notification.RongNotificationInterface.removeNotification;

public class RongPushMessageReceiver extends PushMessageReceiver {
    private long lastNotificationTimestamp;
    private static int PUSH_SERVICE_NOTIFICATION_ID = 3000;
    private static final int VOIP_NOTIFICATION_ID = 2000;
    private static final int NOTIFICATION_ID = 1000;

    @Override
    public boolean onNotificationMessageArrived(Context context, PushType pushType, PushNotificationMessage message) {
        if (Build.MANUFACTURER.equalsIgnoreCase(MobileBrand.XIAOMI) && pushType != PushType.XIAOMI) {
            RongPushClient.ConversationType conversationType = message.getConversationType();
            String objName = message.getObjectName();
            String content = "您有了一条新消息";
            boolean isMulti = false;
            int requestCode = 200;
            boolean isSilent = false;
            if (!TextUtils.isEmpty(objName) && conversationType != null) {
                long now = System.currentTimeMillis();
                if (now - lastNotificationTimestamp < 3000L) {
                    isSilent = true;
                } else {
                    lastNotificationTimestamp = now;
                }

                String title;
                int notificationId;
                if (!conversationType.equals(RongPushClient.ConversationType.SYSTEM) && !conversationType.equals(RongPushClient.ConversationType.PUSH_SERVICE)) {
                    title = (String) context.getPackageManager().getApplicationLabel(context.getApplicationInfo());
                    if ((objName.equals("RC:VCInvite") || objName.equals("RC:VCModifyMem") || objName.equals("RC:VCHangup"))) {
                        if (objName.equals("RC:VCHangup")) {
                            removeNotification(context, VOIP_NOTIFICATION_ID);
                            return true;
                        }

                        notificationId = VOIP_NOTIFICATION_ID;
                        requestCode = 400;
                        content = message.getPushContent();
                    } else {
                        notificationId = NOTIFICATION_ID;
                    }
                } else {
                    //融云推送，基本没用
                    title = message.getPushTitle();
                    if (TextUtils.isEmpty(title)) {
                        title = (String) context.getPackageManager().getApplicationLabel(context.getApplicationInfo());
                    }

                    content = message.getPushContent();
                    notificationId = PUSH_SERVICE_NOTIFICATION_ID;
                    requestCode = 300;
                    ++PUSH_SERVICE_NOTIFICATION_ID;
                }

                PendingIntent intent;
                intent = createPendingIntent(context, message, requestCode, isMulti);

                Notification notification = createNotification(context, title, intent, content, isSilent);
                if (!AppUtils.isAppForeground()) {
                    BadgeNumberManagerXiaoMi.setBadgeNumber(notification, ++Constant.messageCount);
                }
                NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                if (notification != null && nm != null) {
                    nm.notify(notificationId, notification);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
        Constant.messageCount = 0;
        BadgeNumberManager.from(context).setBadgeNumber(0);
        if (TextUtils.isEmpty(Constant.userId)) {
            Intent intent = new Intent(context, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    private PendingIntent createPendingIntent(Context context, PushNotificationMessage message, int requestCode, boolean isMulti) {
        Intent intent = new Intent();
        intent.setAction("io.rong.push.intent.MESSAGE_CLICKED");
        intent.putExtra("message", message);
        intent.putExtra("isMulti", isMulti);
        intent.setPackage(context.getPackageName());
        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Notification createNotification(Context context, String title, PendingIntent pendingIntent, String content, boolean isSilent) {
        String tickerText = "您有了一条新消息";

        Notification notification;

        int smallIcon = context.getResources().getIdentifier("notification_small_icon", "drawable", context.getPackageName());
        if (smallIcon <= 0) {
            smallIcon = context.getApplicationInfo().icon;
        }

        Drawable loadIcon = context.getApplicationInfo().loadIcon(context.getPackageManager());
        Bitmap appIcon = null;

        try {
            if (Build.VERSION.SDK_INT >= 26 && loadIcon instanceof AdaptiveIconDrawable) {
                appIcon = Bitmap.createBitmap(loadIcon.getIntrinsicWidth(), loadIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(appIcon);
                loadIcon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                loadIcon.draw(canvas);
            } else {
                appIcon = ((BitmapDrawable) loadIcon).getBitmap();
            }
        } catch (Exception var19) {
            var19.printStackTrace();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.LOCAL_CHANNEL_ID);
        builder.setLargeIcon(appIcon);
        if (!isSilent) {
            builder.setVibrate(new long[]{100L});
        }

        builder.setSmallIcon(smallIcon);
        builder.setTicker(tickerText);
        if (PushCacheHelper.getInstance().getPushContentShowStatus(context)) {
            builder.setContentTitle(title);
            builder.setContentText(content);
        } else {
            PackageManager pm = context.getPackageManager();
            String name;
            try {
                name = pm.getApplicationLabel(pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)).toString();
            } catch (PackageManager.NameNotFoundException var17) {
                name = "";
            }
            builder.setContentTitle(name);
            builder.setContentText(tickerText);
        }
        builder.setContentIntent(pendingIntent);
        builder.setLights(Color.RED, 3000, 3000);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        notification = builder.build();
        notification.flags = 1;

        return notification;
    }

}
