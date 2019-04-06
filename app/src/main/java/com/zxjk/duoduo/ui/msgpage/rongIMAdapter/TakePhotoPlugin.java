package com.zxjk.duoduo.ui.msgpage.rongIMAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.utils.TakePicUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

import static com.zxjk.duoduo.ui.EditPersonalInformationFragment.REQUEST_ALBUM;


/**
 * @author Administrator
 * @// TODO: 2019\4\3 0003 关于拍摄的自定义plugin 
 */
public class TakePhotoPlugin implements IPluginModule {
    public static final int REQUEST_ALBUM = 1;
    String userId;
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
        TakePicUtil.takePicture( fragment.getActivity(), REQUEST_ALBUM);
        userId=rongExtension.getTargetId();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        String filePath = "";
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ALBUM:
                    filePath = TakePicUtil.file.getAbsolutePath();
                    break;
                default:
                    break;
            }
            TakePhotoMessage message=new TakePhotoMessage();
            message.setFilePath(filePath);
            Message message1=Message.obtain(userId, Conversation.ConversationType.PRIVATE,message);
            RongIM.getInstance().sendImageMessage(message1, null, null, new RongIMClient.SendImageMessageCallback() {
                @Override
                public void onAttached(Message message) {

                }

                @Override
                public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                }

                @Override
                public void onSuccess(Message message) {
                    ToastUtils.showShort("发送成功");
                }

                @Override
                public void onProgress(Message message, int i) {

                }
            });

        }
    }
}
