package com.zxjk.duoduo.ui.msgpage.rongIMAdapter;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.BuildConfig;
import com.zxjk.duoduo.DuoDuoFileProvider;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.TakePicUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;
import sun.misc.BASE64Encoder;

/**
 * @author Administrator
 * @// TODO: 2019\4\3 0003 关于拍摄的自定义plugin
 */
public class TakePhotoPlugin implements IPluginModule {
    private Context context;
    public static final int REQUEST_ALBUM = 1;
    String userId;
    Conversation.ConversationType conversationType;
    private File file;
    private Uri imageUri;

    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.icon_photography);
    }

    @Override
    public String obtainTitle(Context context) {
        this.context = context;
        return context.getString(R.string.shotting_image_title);
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        userId = rongExtension.getTargetId();
        conversationType = rongExtension.getConversationType();
        FragmentActivity activity = fragment.getActivity();
        PermissionUtils.permission(PermissionConstants.CAMERA,
                PermissionConstants.STORAGE)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DuoDuo/images/" + System.currentTimeMillis() + ".png");
                        file.getParentFile().mkdirs();
                        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        imageUri = getUriForFile(activity, file);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intentCamera.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        rongExtension.startActivityForPluginResult(intentCamera, REQUEST_ALBUM, TakePhotoPlugin.this);
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.showShort(R.string.please_open_permission);
                    }
                }).request();
    }

    private static Uri getUriForFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return DuoDuoFileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".FileProvider", file);
        }
        return Uri.fromFile(file);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ALBUM:
                    imageUri = Uri.fromFile(file);
                    ImageMessage obtain = ImageMessage.obtain(imageUri, imageUri, false);
                    Message message = Message.obtain(userId, conversationType, obtain);
                    RongIM.getInstance().sendImageMessage(message, null, null, new RongIMClient.SendImageMessageCallback() {
                        @Override
                        public void onAttached(Message message) {

                        }

                        @Override
                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                        }

                        @Override
                        public void onSuccess(Message message) {

                        }

                        @Override
                        public void onProgress(Message message, int i) {

                        }
                    });
            }
        }
    }
}
