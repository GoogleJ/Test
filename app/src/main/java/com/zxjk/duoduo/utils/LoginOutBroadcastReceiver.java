package com.zxjk.duoduo.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.zxjk.duoduo.ui.LoginActivity;
/**
 * @author Administrator
 * @// TODO: 2019\3\25 0025 用广播退出所有活动
 */
public class LoginOutBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityCollector.finishAll();  // 销毁所有活动
        Intent intent1 = new Intent(context, LoginActivity.class);
        context.startActivity(intent1);
    }

}
