package com.zxjk.duoduo.ui.msgpage.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.othershe.combinebitmap.CombineBitmap;
import com.othershe.combinebitmap.layout.WechatLayoutManager;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;

import java.util.Arrays;
import java.util.List;

import io.rong.imlib.model.Conversation;

public class ShareGroupQRAdapter extends BaseQuickAdapter<Conversation, BaseViewHolder> {

    private Context context;

    public ShareGroupQRAdapter(Context context) {
        super(R.layout.item_share_group_qr);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Conversation item) {
        helper.addOnClickListener(R.id.ll);

        ImageView iv = helper.getView(R.id.iv);
        TextView tv = helper.getView(R.id.tv);

        tv.setText(item.getConversationTitle());

        if (item.getConversationType().equals(Conversation.ConversationType.GROUP)) {
            //群聊
            String[] split = item.getPortraitUrl().split(",");
            if (split.length > 9) {
                List<String> strings = Arrays.asList(split);
                List<String> strings1 = strings.subList(0, 9);
                split = new String[strings1.size()];
                for (int i = 0; i < strings1.size(); i++) {
                    split[i] = strings1.get(i);
                }
            }
            CombineBitmap.init(context)
                    .setLayoutManager(new WechatLayoutManager()) // 必选， 设置图片的组合形式，支持WechatLayoutManager、DingLayoutManager
                    .setGapColor(ContextCompat.getColor(context, R.color.grey)) // 单个图片间距的颜色，默认白色
                    .setSize(CommonUtils.dip2px(context, 48)) // 必选，组合后Bitmap的尺寸，单位dp
                    .setGap(CommonUtils.dip2px(context, 2)) // 单个Bitmap之间的距离，单位dp，默认0dp
                    .setUrls(split) // 要加载的图片url数组
                    .setImageView(iv) // 直接设置要显示图片的ImageView
                    .build();
        } else {
            //单聊
            GlideUtil.loadCornerImg(iv, item.getPortraitUrl(), 3);
        }
    }
}
