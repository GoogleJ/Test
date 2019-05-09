package com.zxjk.duoduo.ui.msgpage.rongIMAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.msgpage.SelectContactForCardActivity;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * @author Administrator
 */
public class BusinessCardPlugin implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.icon_personal_business_card);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.personal_business_card_title);
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        switch (rongExtension.getConversationType()) {
            case GROUP:
                Intent intentGroup = new Intent(fragment.getContext(), SelectContactForCardActivity.class);
                intentGroup.putExtra("intentType", 0);
                fragment.startActivity(intentGroup);
                break;
            case PRIVATE:
                Intent intent = new Intent(fragment.getContext(), SelectContactForCardActivity.class);
                UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(rongExtension.getTargetId());
                if (null == userInfo) {
                    intent.putExtra("userType", 0);
                    intent.putExtra("intentType", 1);
                    intent.putExtra("userId", rongExtension.getTargetId());
                } else {
                    intent.putExtra("userType", 0);
                    intent.putExtra("intentType", 1);
                    intent.putExtra("user", userInfo);
                }
                fragment.startActivity(intent);
                break;
            default:
                break;
        }
    }

}
