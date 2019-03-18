package com.zxjk.duoduo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.zxjk.duoduo.ui.HomeActivity;
import com.zxjk.duoduo.ui.LoginActivity;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 融云相关监听 事件集合类
 *
 * @author AMing
 * @date 16/1/7
 * Company RongCloud
 */
public class SealAppContext {
    private static SealAppContext mRongCloudInstance;
    private final Context mContext;

    private RongIM.LocationProvider.LocationCallback mLastLocationCallback;


    public SealAppContext(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {

        if (mRongCloudInstance == null) {
            synchronized (SealAppContext.class) {

                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new SealAppContext(context);
                }
            }
        }

    }

    /**
     * 获取RongCloud 实例。
     *
     * @return RongCloud。
     */
    public static SealAppContext getInstance() {
        return mRongCloudInstance;
    }

    public Context getContext() {
        return mContext;
    }

    public RongIMClient.ConnectCallback getConnectCallback(final Context context) {
        RongIMClient.ConnectCallback connectCallback = new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                if (context instanceof LoginActivity) {
                    return;
                }
                context.startActivity(new Intent(context, LoginActivity.class));
                ((Activity) context).finish();
            }

            @Override
            public void onSuccess(String s) {
                context.startActivity(new Intent(context, HomeActivity.class));
                ((Activity) context).finish();
            }

            @Override
            public void onError(final RongIMClient.ErrorCode e) {
                Log.i("GJsonSSS", "" + e.getMessage());

            }
        };
        return connectCallback;
    }

    public void connect(String token, final Activity context) {
        RongIMClient.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                            2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {

            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                context.finish();
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });

    }

}
