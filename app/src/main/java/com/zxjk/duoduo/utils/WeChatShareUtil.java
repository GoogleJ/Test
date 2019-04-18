package com.zxjk.duoduo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.zxjk.duoduo.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WeChatShareUtil {

    public static IWXAPI wxShare;

    private static final int THUMB_SIZE = 150;

    //分享类型（会话、朋友圈） WXSceneSession WXSceneTimeline
    private static int mTargetScene = SendMessageToWX.Req.WXSceneSession;

    public static void shareImg(Context context, int targetScene) {

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.shareimg_wechat);
        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.title = "多多优社";
        bmp.recycle();
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.shareimg_wechat_icon)
//                , THUMB_SIZE, THUMB_SIZE, true);
//        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = targetScene;
        wxShare.sendReq(req);
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public byte[] getFromAssets(Context context, String fileName) throws IOException {
        InputStream is = context.getAssets().open(fileName);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int num = is.read(buffer);
            while (num != -1) {
                baos.write(buffer, 0, num);
                num = is.read(buffer);
            }
            baos.flush();
            return baos.toByteArray();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
