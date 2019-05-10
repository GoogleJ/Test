package com.zxjk.duoduo.ui.widget.dialog;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.KeyboardUtils;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.utils.TakePicUtil;

/**
 * author L
 * create at 2019/5/10
 * description: 底部弹窗dialog 拍照、选择相册、取消
 */
public class BottomDialog {
    public static void dialogType(AppCompatActivity activity, int REQUEST_TAKE, int REQUEST_ALBUM) {
        KeyboardUtils.hideSoftInput(activity);
        NiceDialog.init().setLayoutId(R.layout.layout_general_dialog6).setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                //拍照
                holder.setOnClickListener(R.id.tv_photograph, v -> {
                    dialog.dismiss();
                    TakePicUtil.takePicture(activity, REQUEST_TAKE);
                });
                //相册选择
                holder.setOnClickListener(R.id.tv_photo_select, v -> {
                    dialog.dismiss();
                    TakePicUtil.albumPhoto(activity, REQUEST_ALBUM);
                });
                //取消
                holder.setOnClickListener(R.id.tv_cancel, v -> dialog.dismiss());

            }
        }).setShowBottom(true)
                .setOutCancel(true)
                .setDimAmount(0.5f)
                .show(activity.getSupportFragmentManager());
    }


}
