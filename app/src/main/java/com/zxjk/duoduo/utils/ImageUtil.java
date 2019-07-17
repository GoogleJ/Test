package com.zxjk.duoduo.utils;

import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import com.othershe.combinebitmap.CombineBitmap;
import com.othershe.combinebitmap.layout.WechatLayoutManager;
import com.zxjk.duoduo.R;
import java.util.Arrays;

public class ImageUtil {

    private static void loadGroupPortrait(ImageView target, String[] urls, int size, int gap) {
        CombineBitmap.init(target.getContext())
                .setLayoutManager(new WechatLayoutManager())
                .setGapColor(ContextCompat.getColor(target.getContext(), R.color.grey))
                .setSize(CommonUtils.dip2px(target.getContext(), size))
                .setGap(CommonUtils.dip2px(target.getContext(), gap))
                .setUrls(urls)
                .setImageView(target)
                .build();
    }

    public static void loadGroupPortrait(ImageView target, String urls) {
        loadGroupPortrait(target, urls, 48, 2);
    }

    public static void loadGroupPortrait(ImageView target, String urls, int size, int gap) {
        String[] split = urls.replace("ISGAMEGROUP", "").split(",");
        if (split.length > 9) {
            split = Arrays.copyOfRange(split, 0, 9);
        }
        loadGroupPortrait(target, split, size, gap);
    }
}
