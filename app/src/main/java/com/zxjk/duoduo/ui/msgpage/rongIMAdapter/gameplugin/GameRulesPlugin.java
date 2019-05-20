package com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.EnlargeImageActivity;
import com.zxjk.duoduo.ui.msgpage.ConversationActivity;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

public class GameRulesPlugin implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.ic_plugin_gamerule);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.gamerule);
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        Intent intent5 = new Intent(fragment.getContext(), EnlargeImageActivity.class);
        intent5.putExtra("image", "GameRules");
        fragment.startActivity(intent5);
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
