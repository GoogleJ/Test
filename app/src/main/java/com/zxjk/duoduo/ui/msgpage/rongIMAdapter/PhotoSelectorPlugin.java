package com.zxjk.duoduo.ui.msgpage.rongIMAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.zxjk.duoduo.R;

import androidx.core.content.ContextCompat;
import io.rong.imkit.plugin.ImagePlugin;

/**
 * @author Administrator
 */
public class PhotoSelectorPlugin extends ImagePlugin {
    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.icon_album);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.photo_image_title);
    }
}
