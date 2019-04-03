package com.zxjk.duoduo.ui.msgpage.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.zxjk.duoduo.R;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

public class TakePhotoPlugin implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.icon_photography);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.shotting_image_title);
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {

    }



    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
