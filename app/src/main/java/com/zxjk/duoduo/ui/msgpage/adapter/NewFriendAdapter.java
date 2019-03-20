package com.zxjk.duoduo.ui.msgpage.adapter;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.FriendListResponse;


/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 新的朋友的列表适配器
 */
public class NewFriendAdapter extends BaseQuickAdapter<FriendListResponse, BaseViewHolder> {
    public NewFriendAdapter() {
        super(R.layout.item_new_friend);
    }

    String isDel = "0";

    @SuppressLint({"ResourceAsColor", "NewApi"})
    @Override
    protected void convert(BaseViewHolder helper, FriendListResponse item) {
        helper.setText(R.id.m_item_new_friend_user_name_text, item.getRealname())
                .setText(R.id.m_item_new_friend_message_label, item.getSignature())
                .addOnClickListener(R.id.m_item_new_friend_type_btn)
        ;


    }
}
