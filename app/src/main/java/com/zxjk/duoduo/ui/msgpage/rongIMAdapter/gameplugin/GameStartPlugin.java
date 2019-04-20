package com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.zxjk.duoduo.R;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

public class GameStartPlugin implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.ic_plugin_game_start);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.game_start);
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {

    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
