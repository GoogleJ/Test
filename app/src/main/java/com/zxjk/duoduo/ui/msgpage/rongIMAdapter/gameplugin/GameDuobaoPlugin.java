package com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.msgpage.JinDuoBaoActivity;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

public class GameDuobaoPlugin implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.ic_plugin_game_duobao);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.game_duobao);
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        Intent intent = new Intent(fragment.getContext(), JinDuoBaoActivity.class);
        intent.putExtra("groupId", rongExtension.getTargetId());
        fragment.startActivity(intent);
    }
}
