package com.zxjk.duoduo.ui.msgpage.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.FriendListResponse;
import com.zxjk.duoduo.network.response.GroupChatResponse;
import com.zxjk.duoduo.utils.GlideUtil;

/**
 * TODO:群聊信息的适配器
 */

public class GroupChatAdapter extends BaseQuickAdapter<GroupChatResponse, BaseViewHolder> {


    public GroupChatAdapter() {
        super(R.layout.item_group_chat);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupChatResponse item) {
        helper.setText(R.id.group_name,item.getGroupNikeName()) //群名称
                .setText(R.id.group_message,item.getGroupSign())
                .setText(R.id.group_message_time,item.getUpdateTime())
        .addOnClickListener(R.id.m_group_chat);

        ImageView heardImage=helper.getView(R.id.group_chat_iamge);
        GlideUtil.loadImg(heardImage,item.getHeadPortrait());//群头像



    }
}
