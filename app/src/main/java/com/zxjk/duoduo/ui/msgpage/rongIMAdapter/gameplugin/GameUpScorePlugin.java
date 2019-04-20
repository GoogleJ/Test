package com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.zxjk.duoduo.R;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

public class GameUpScorePlugin implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.ic_plugin_upscore);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.upscore);
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {

    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
