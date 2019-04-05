package com.zxjk.duoduo.ui.msgpage.rongIMAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.msgpage.PrivacyRedPacketActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.UserInfo;

/**
 * @author Administrator
 * @// TODO: 2019\4\3 0003 关于红包的自定义plugin 
 */
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
        Intent intent = new Intent(fragment.getContext(), PrivacyRedPacketActivity.class);
        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(rongExtension.getTargetId());
        intent.putExtra("user", userInfo);
        fragment.startActivity(intent);
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }

}
