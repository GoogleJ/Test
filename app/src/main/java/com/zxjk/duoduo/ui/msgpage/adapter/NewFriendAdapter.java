package com.zxjk.duoduo.ui.msgpage.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.utils.GlideUtil;


/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 新的朋友的列表适配器
 */
public class NewFriendAdapter extends BaseQuickAdapter<FriendInfoResponse, BaseViewHolder> {
    public NewFriendAdapter() {
        super(R.layout.item_new_friend);
    }


    @Override
    protected void convert(BaseViewHolder helper, FriendInfoResponse item) {
        helper.setText(R.id.m_item_new_friend_user_name_text, item.getNick())
                .setText(R.id.m_item_new_friend_message_label, item.getSignature())
                .addOnClickListener(R.id.m_item_new_friend_type_btn)
                .addOnClickListener(R.id.m_add_btn_layout)
        .addOnLongClickListener(R.id.m_add_btn_layout);
        ImageView headerImage=helper.getView(R.id.m_item_new_friend_icon);
        GlideUtil.loadCornerImg(headerImage,item.getHeadPortrait(),2);
    }
}
