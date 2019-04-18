package com.zxjk.duoduo.ui.msgpage.adapter;


import android.annotation.SuppressLint;
import android.util.TypedValue;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GroupChatResponse;

/**
 *
 * @author Administrator
 */

public class GroupChatAdapter extends BaseQuickAdapter<GroupChatResponse, BaseViewHolder> {


    public GroupChatAdapter() {
        super(R.layout.item_group_chat);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void convert(BaseViewHolder helper, GroupChatResponse item) {
        //群名称
        helper.setText(R.id.group_name, item.getGroupNikeName())
                .setText(R.id.group_message, item.getGroupSign())
//                .setText(R.id.group_message_time,item.getUpdateTime())
                .addOnClickListener(R.id.m_group_chat);
//群头像
        ImageView heardImage = helper.getView(R.id.group_chat_iamge);
        String[] split = item.getHeadPortrait().split(",");
        Glide.with(mContext).load("https://zhongxingjike.oss-cn-hongkong.aliyuncs.com/upload/201553155744564")
                .into(heardImage);
//
//        if (split.length != 1) {
//            ArrayList<String> objects = new ArrayList<>();
//            for (String s : split) {
//                LogUtils.e(s);
//                if (s.endsWith(".jpg")) {
//                    objects.add(s);
//                }
//            }
//
//            String[] objects1 = (String[]) objects.toArray();
//
//            CombineBitmap.init(mContext)
//                    .setLayoutManager(new WechatLayoutManager())
//                    .setSize(dp2px(72))
//                    .setUrls(objects)
//                    .setImageView(heardImage)
//                    .build();

//            ArrayList<Bitmap> bitmaps = new ArrayList<>();
//            Observable.just("1")
//                    .doOnSubscribe(disposable -> {
//                        for (int i = 0; i < split.length; i++) {
//                            bitmaps.add(Glide.with(mContext).asBitmap()
//                                    .load(split[i])
//                                    .into(dp2px(72), dp2px(72))
//                                    .get());
//                        }
//                    })
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(s -> {
//
//                    }, t -> {
//
//                    });
//        } else {
//            GlideUtil.loadCornerImg(heardImage, item.getHeadPortrait(), 2);
//        }
    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, mContext.getResources().getDisplayMetrics());
    }
}
