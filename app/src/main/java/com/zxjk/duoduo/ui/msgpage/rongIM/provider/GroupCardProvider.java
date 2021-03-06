package com.zxjk.duoduo.ui.msgpage.rongIM.provider;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.othershe.combinebitmap.CombineBitmap;
import com.othershe.combinebitmap.layout.WechatLayoutManager;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.grouppage.AgreeGroupChatActivity;
import com.zxjk.duoduo.ui.msgpage.rongIM.message.GroupCardMessage;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.Arrays;
import java.util.List;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

@ProviderTag(messageContent = GroupCardMessage.class)
public class GroupCardProvider extends IContainerItemProvider.MessageProvider<GroupCardMessage> {

    public GroupCardProvider() {

    }

    @Override
    public void bindView(View view, int i, GroupCardMessage groupCardMessage, UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {
            //消息方向，自己发送的
            holder.sendLayout.setBackgroundResource(R.drawable.icon_business_card_user);
        } else {
            holder.sendLayout.setBackgroundResource(R.drawable.icon_business_card_friend);
        }

        List<String> headUrls = Arrays.asList(groupCardMessage.getIcon().split(","));

        holder.content.setText(groupCardMessage.getName() + "邀请你加入群聊" + groupCardMessage.getGroupName()
                + ",点击查看");
        if (headUrls.size() == 0) {
            return;
        }

        String[] split = groupCardMessage.getIcon().split(",");
        if (split.length > 9) {
            List<String> strings = Arrays.asList(split);
            List<String> strings1 = strings.subList(0, 9);
            split = new String[strings1.size()];
            for (int j = 0; j < strings1.size(); j++) {
                split[j] = strings1.get(j);
            }
        }

        CombineBitmap.init(view.getContext())
                .setLayoutManager(new WechatLayoutManager()) // 必选， 设置图片的组合形式，支持WechatLayoutManager、DingLayoutManager
                .setGapColor(view.getContext().getResources().getColor(R.color.grey)) // 单个图片间距的颜色，默认白色
                .setSize(CommonUtils.dip2px(view.getContext(), 56)) // 必选，组合后Bitmap的尺寸，单位dp
                .setGap(CommonUtils.dip2px(view.getContext(), 2)) // 单个Bitmap之间的距离，单位dp，默认0dp
                .setUrls(split) // 要加载的图片url数组
                .setImageView(holder.nineImg) // 直接设置要显示图片的ImageView
                .build();
    }

    @Override
    public Spannable getContentSummary(GroupCardMessage groupCardMessage) {
        return new SpannableString("[群名片]");
    }

    @Override
    public void onItemClick(View view, int i, GroupCardMessage groupCardMessage, UIMessage uiMessage) {
        if (!uiMessage.getSenderUserId().equals(Constant.userId)) {
            Intent intent = new Intent(view.getContext(), AgreeGroupChatActivity.class);
            intent.putExtra("inviterId", groupCardMessage.getInviterId());
            intent.putExtra("groupId", groupCardMessage.getGroupId());
            intent.putExtra("groupName", groupCardMessage.getGroupName());
            intent.putExtra("headUrls", groupCardMessage.getIcon());
            intent.putExtra("id", uiMessage.getMessageId());
            if (!TextUtils.isEmpty(uiMessage.getExtra())) {
                intent.putExtra("overtime", true);
            }
            view.getContext().startActivity(intent);
        }
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group_card, viewGroup, false);
        GroupCardProvider.ViewHolder holder = new GroupCardProvider.ViewHolder();
        holder.sendLayout = view.findViewById(R.id.sendlayout);
        holder.content = view.findViewById(R.id.content);
        holder.nineImg = view.findViewById(R.id.nineImg);
        view.setTag(holder);
        return view;
    }

    class ViewHolder {
        private LinearLayout sendLayout;
        private TextView content;
        private ImageView nineImg;
    }
}
