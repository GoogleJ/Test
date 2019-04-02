package com.zxjk.duoduo.ui.msgpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

import androidx.annotation.Nullable;

/**
 * @author Administrator
 * @// TODO: 2019\4\2 0002  跳转到发送红包的界面
 */
public class PrivacyRedPacketActivity extends BaseActivity {
    public static void start(Activity activity,String id){
        Intent intent=new Intent(activity,PrivacyRedPacketActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_envelopes);
    }
}
