package com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.walletpage.ExchangeActivity;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

public class GameJiaoYiPlugin implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.ic_plugin_jiaoyisuo);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.jiaoyisuo);
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        fragment.getContext().startActivity(new Intent(fragment.getContext(), ExchangeActivity.class));
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
