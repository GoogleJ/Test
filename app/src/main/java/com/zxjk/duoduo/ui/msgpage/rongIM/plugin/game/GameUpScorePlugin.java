package com.zxjk.duoduo.ui.msgpage.rongIM.plugin.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.msgpage.GameUpScoreActivity;
import com.zxjk.duoduo.ui.msgpage.GroupMasterPointsActivity;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

public class GameUpScorePlugin implements IPluginModule {

    String gameType;
    boolean isGroup;

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

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
        if (isGroup) {
            Intent intent = new Intent(fragment.getContext(), GroupMasterPointsActivity.class);
            intent.putExtra("groupId", rongExtension.getTargetId());
            intent.putExtra("gameType", gameType);
            fragment.startActivity(intent);
        } else {
            Intent intent = new Intent(fragment.getContext(), GameUpScoreActivity.class);
            intent.putExtra("groupId", rongExtension.getTargetId());
            fragment.startActivity(intent);
        }


    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
