package com.zxjk.duoduo.ui.msgpage.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.msgpage.PrivacyRedPacketActivity;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.plugin.IPluginModule;

import static com.liang.permission.PermissionHelper.TAG;

public class RedPacketPlugin implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context,R.drawable.icon_red_packet_btn);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.red_packet);
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        PrivacyRedPacketActivity.start(fragment.getActivity(),rongExtension.getTargetId());
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }

}
