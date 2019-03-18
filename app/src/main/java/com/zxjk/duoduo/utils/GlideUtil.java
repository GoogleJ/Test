package com.zxjk.duoduo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.zxjk.duoduo.R;

public class GlideUtil {
    private static final int LOAD_CIRCLE = 1;
    private static final int LOAD_CORNER = 2;
    private static final int LOAD_SOURCE = 3;

    public static void loadImg(ImageView iv, String url) {
        loadImage(iv, url, 0, R.drawable.ic_launcher, 0, null);
    }

    public static void loadNormalImg(ImageView iv, String url, int resId) {
        loadImage(iv, url, 0, resId, 0, null);
    }

    public static void loadNormalImg(ImageView iv, String url, int resId, RequestListener<Bitmap> listener) {
        loadImage(iv, url, 0, resId, 0, listener);
    }

    public static void loadCircleImg(ImageView iv, String url, int resId) {
        loadImage(iv, url, LOAD_CIRCLE, resId, 0, null);
    }

    public static void loadCircleImg(ImageView iv, String url, int resId, RequestListener<Bitmap> listener) {
        loadImage(iv, url, LOAD_CIRCLE, resId, 0, listener);
    }

    public static void loadCornerImg(ImageView iv, String url, int resId, int radius) {
        loadImage(iv, url, LOAD_CORNER, resId, radius, null);
    }

    public static void loadCornerImg(ImageView iv, String url, int resId, int radius, RequestListener<Bitmap> listener) {
        loadImage(iv, url, LOAD_CORNER, resId, radius, listener);
    }

    //加载图片实现方法 Glide 可设置加载回调返回bitmap
    private static void loadImage(ImageView iv, String url, int mode, int resId, int radius, RequestListener<Bitmap> listener) {
        RequestManager manager = Glide.with(iv.getContext());
        if (listener != null) {
            manager.asBitmap().listener(listener)
                    .load(url).apply(getRequestOptions(iv.getContext(), mode, resId, radius)).into(iv);
            return;
        }
        manager.load(url).apply(getRequestOptions(iv.getContext(), mode, resId, radius)).into(iv);
    }

    private static RequestOptions getRequestOptions(Context context, int mode, int resId, int radius) {
        switch (mode) {
            case LOAD_CIRCLE:
                return RequestOptions.bitmapTransform(new CircleCrop()).placeholder(resId);
            case LOAD_CORNER:
                return new RequestOptions()
                        .placeholder(resId)
                        .error(resId)
                        .transform(new RoundedCorners(CommonUtils.dip2px(context, radius)));
            case LOAD_SOURCE:
                return new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .placeholder(resId).error(resId);
            default:
                return new RequestOptions().placeholder(resId);
        }
    }
}
