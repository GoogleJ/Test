package com.zxjk.duoduo.ui.msgpage.rongIM.plugin;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.zxjk.duoduo.R;

public class SightPlugin extends io.rong.sight.SightPlugin {

    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.ic_audio);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.audio_video_sight);
    }
}
