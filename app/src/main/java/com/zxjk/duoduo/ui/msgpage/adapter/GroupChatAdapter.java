package com.zxjk.duoduo.ui.msgpage.adapter;

import android.annotation.SuppressLint;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.othershe.combinebitmap.CombineBitmap;
import com.othershe.combinebitmap.layout.WechatLayoutManager;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GroupChatResponse;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.Arrays;
import java.util.List;

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

        helper.setText(R.id.group_name, item.getGroupNikeName())
                .setText(R.id.group_message, item.getGroupSign())
                .addOnClickListener(R.id.m_group_chat);

        ImageView heardImage = helper.getView(R.id.group_chat_iamge);

        String[] split = item.getHeadPortrait().split(",");
        if (split.length > 9) {
            List<String> strings = Arrays.asList(split);
            List<String> strings1 = strings.subList(0, 9);
            split = new String[strings1.size()];
            for (int i = 0; i < strings1.size(); i++) {
                split[i] = strings1.get(i);
            }
        }

        CombineBitmap.init(mContext)
                .setLayoutManager(new WechatLayoutManager()) // 必选， 设置图片的组合形式，支持WechatLayoutManager、DingLayoutManager
                .setGapColor(mContext.getResources().getColor(R.color.grey)) // 单个图片间距的颜色，默认白色
                .setSize(CommonUtils.dip2px(mContext, 56)) // 必选，组合后Bitmap的尺寸，单位dp
                .setGap(CommonUtils.dip2px(mContext, 2)) // 单个Bitmap之间的距离，单位dp，默认0dp
                .setUrls(split) // 要加载的图片url数组
                .setImageView(heardImage) // 直接设置要显示图片的ImageView
                .build();
    }
}
