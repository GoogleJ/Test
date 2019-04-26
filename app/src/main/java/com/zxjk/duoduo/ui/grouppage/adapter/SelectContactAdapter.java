package com.zxjk.duoduo.ui.grouppage.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.utils.GlideUtil;

/**
 * @author Administrator
 */
public class SelectContactAdapter extends BaseQuickAdapter<FriendInfoResponse, BaseViewHolder> {
    public SelectContactAdapter() {
        super(R.layout.item_add_group);
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendInfoResponse item) {
        helper.setText(R.id.user_name, item.getNick())
                .addOnClickListener(R.id.add_del_group_layout);
        ImageView heardImage = helper.getView(R.id.remove_headers);
        GlideUtil.loadCornerImg(heardImage, item.getHeadPortrait(), 2);

    }
}
