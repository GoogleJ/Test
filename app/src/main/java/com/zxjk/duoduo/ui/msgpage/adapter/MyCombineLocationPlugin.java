package com.zxjk.duoduo.ui.msgpage.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.zxjk.duoduo.R;

import androidx.core.content.ContextCompat;
import io.rong.imkit.plugin.CombineLocationPlugin;

public class MyCombineLocationPlugin extends CombineLocationPlugin {
    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.icon_position);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.location_address_title);
    }
}
