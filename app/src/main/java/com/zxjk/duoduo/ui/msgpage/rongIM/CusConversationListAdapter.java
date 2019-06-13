package com.zxjk.duoduo.ui.msgpage.rongIM;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.othershe.combinebitmap.CombineBitmap;
import com.othershe.combinebitmap.layout.WechatLayoutManager;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.Arrays;
import java.util.List;

import io.rong.imkit.model.UIConversation;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;

public class CusConversationListAdapter extends ConversationListAdapter {

    public CusConversationListAdapter(Context context) {
        super(context);
    }

    @Override
    protected View newView(Context context, int position, ViewGroup group) {
        return super.newView(context, position, group);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void bindView(View v, int position, UIConversation data) {
        super.bindView(v, position, data);

        if (data.getConversationType() != Conversation.ConversationType.GROUP) {
            ImageView imageView = v.findViewById(R.id.rc_mask);
            ImageView game_mask = v.findViewById(R.id.game_mask);
            imageView.setVisibility(View.GONE);
            game_mask.setVisibility(View.GONE);
            ConversationListAdapter.ViewHolder holder = (ConversationListAdapter.ViewHolder) v.getTag();
            holder.leftImageView.setVisibility(View.VISIBLE);
            return;
        }

        ImageView imageView = v.findViewById(R.id.rc_mask);
        ImageView game_mask = v.findViewById(R.id.game_mask);
        imageView.setVisibility(View.VISIBLE);
        ConversationListAdapter.ViewHolder holder = (ConversationListAdapter.ViewHolder) v.getTag();
        holder.leftImageView.setVisibility(View.GONE);

        Group groupInfo = RongUserInfoManager.getInstance().getGroupInfo(data.getConversationTargetId());
        groupInfo = null;
        if (groupInfo != null && !TextUtils.isEmpty(groupInfo.getPortraitUri().toString())) {
            String s = groupInfo.getPortraitUri().toString();
            String[] split = s.split(",");
            if (split.length > 9) {
                List<String> strings = Arrays.asList(split);
                List<String> strings1 = strings.subList(0, 9);
                split = new String[strings1.size()];
                for (int i = 0; i < strings1.size(); i++) {
                    split[i] = strings1.get(i);
                }
            }
            CombineBitmap.init(v.getContext())
                    .setLayoutManager(new WechatLayoutManager()) // 必选， 设置图片的组合形式，支持WechatLayoutManager、DingLayoutManager
                    .setGapColor(ContextCompat.getColor(v.getContext(), R.color.grey)) // 单个图片间距的颜色，默认白色
                    .setSize(CommonUtils.dip2px(v.getContext(), 48)) // 必选，组合后Bitmap的尺寸，单位dp
                    .setGap(CommonUtils.dip2px(v.getContext(), 2)) // 单个Bitmap之间的距离，单位dp，默认0dp
                    .setUrls(split) // 要加载的图片url数组
                    .setImageView(imageView) // 直接设置要显示图片的ImageView
                    .build();
        } else {
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getGroupByGroupId(data.getConversationTargetId())
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver())
                    .subscribe(response -> {
                        if (TextUtils.isEmpty(response.getMaxNumber())) {
                            game_mask.setVisibility(View.GONE);
                        } else {
                            game_mask.setVisibility(View.VISIBLE);
                        }
                        String s = "";

                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < response.getCustomers().size(); i++) {
                            stringBuilder.append(response.getCustomers().get(i).getHeadPortrait() + ",");
                            if (i == response.getCustomers().size() - 1 || i == 8) {
                                s = stringBuilder.substring(0, stringBuilder.length() - 1);
                                break;
                            }
                        }

                        if (null == RongUserInfoManager.getInstance().getGroupInfo(response.getGroupInfo().getId())) {
                            Group group = new Group(response.getGroupInfo().getId(), response.getGroupInfo().getGroupNikeName(), Uri.parse(s));
                            RongUserInfoManager.getInstance().setGroupInfo(group);
                        }

                        String[] split = s.split(",");
                        if (split.length > 9) {
                            List<String> strings = Arrays.asList(split);
                            List<String> strings1 = strings.subList(0, 9);
                            split = new String[strings1.size()];
                            for (int i = 0; i < strings1.size(); i++) {
                                split[i] = strings1.get(i);
                            }
                        }
                        CombineBitmap.init(v.getContext())
                                .setLayoutManager(new WechatLayoutManager()) // 必选， 设置图片的组合形式，支持WechatLayoutManager、DingLayoutManager
                                .setGapColor(ContextCompat.getColor(v.getContext(), R.color.grey)) // 单个图片间距的颜色，默认白色
                                .setSize(CommonUtils.dip2px(v.getContext(), 48)) // 必选，组合后Bitmap的尺寸，单位dp
                                .setGap(CommonUtils.dip2px(v.getContext(), 2)) // 单个Bitmap之间的距离，单位dp，默认0dp
                                .setUrls(split) // 要加载的图片url数组
                                .setImageView(imageView) // 直接设置要显示图片的ImageView
                                .build();
                    }, t -> {
                    });
        }
    }
}
