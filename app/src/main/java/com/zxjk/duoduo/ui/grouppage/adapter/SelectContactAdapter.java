package com.zxjk.duoduo.ui.grouppage.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.FriendListResponse;
import com.zxjk.duoduo.utils.GlideUtil;

/**
 * @author Administrator
 */
public class SelectContactAdapter extends BaseQuickAdapter<FriendListResponse, BaseViewHolder> {
    public SelectContactAdapter() {
        super(R.layout.item_add_group);
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendListResponse item) {
        helper.setText(R.id.user_name,item.getNick())
        .addOnClickListener(R.id.add_del_group_layout);
        ImageView heardImage=helper.getView(R.id.remove_headers);
        GlideUtil.loadImg(heardImage,item.getHeadPortrait());

    }
}
