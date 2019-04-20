package com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.msgpage.GameDownScoreActivity;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

public class GameDownScorePlugin implements IPluginModule {

    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.ic_plugin_downscore);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.downscore);
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        fragment.startActivity(new Intent(fragment.getContext(), GameDownScoreActivity.class));
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
