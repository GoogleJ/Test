package com.zxjk.duoduo.ui.msgpage.rongIM.plugin.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.msgpage.GameRecordActivity;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

public class GameRecordPlugin implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.ic_plugin_gamerecord);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.gamerecord);
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        Intent intent = new Intent(fragment.getActivity(), GameRecordActivity.class);
        intent.putExtra("groupId", rongExtension.getTargetId());
        fragment.getActivity().startActivity(intent);
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
