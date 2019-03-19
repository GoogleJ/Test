package com.zxjk.duoduo;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.blankj.utilcode.util.Utils;

import android.app.ActivityManager;
import android.content.Context;

import androidx.multidex.MultiDex;
//import io.rong.imkit.RongIM;
//import io.rong.push.PushType;
//import io.rong.push.notification.PushMessageReceiver;
//import io.rong.push.notification.PushNotificationMessage;

/**
 * 这里是Application
 * @author Administrator
 */
public class Application extends android.app.Application {

    public static OSS oss;

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);

        new Thread(this::initOSS).start();

        MultiDex.install(this);
//        RongIM.init(this);
    }

    //初始化阿里云OSS上传服务
    private void initOSS() {
        String AK = "LTAI8wpEjXW0r2y9";
        String SK = "4Dv0UhSB16KLPKC8GR6DuH9GTEGil7";
        OSSPlainTextAKSKCredentialProvider ossPlainTextAKSKCredentialProvider =
                new OSSPlainTextAKSKCredentialProvider(AK, SK);
        String endpoint = "oss-cn-hongkong.aliyuncs.com";
        oss = new OSSClient(this, endpoint, ossPlainTextAKSKCredentialProvider);

    }

    /**
     * 这个是对融云的方法进行的包装
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

//    public class MyRongReceiver extends PushMessageReceiver {
//
//
//        @Override
//        public boolean onNotificationMessageArrived(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
//            //false是系统的，true是需要自定义
//            return false;
//        }
//
//        @Override
//        public boolean onNotificationMessageClicked(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
//            return false;
//        }
//    }
}
