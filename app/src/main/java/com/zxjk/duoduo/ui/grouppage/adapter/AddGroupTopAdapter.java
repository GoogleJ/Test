package com.zxjk.duoduo.ui.grouppage.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.utils.GlideUtil;

public class AddGroupTopAdapter extends BaseQuickAdapter<FriendInfoResponse, BaseViewHolder> {
    public AddGroupTopAdapter() {
        super(R.layout.item_select_contact);
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendInfoResponse item) {
        ImageView heardImage = helper.getView(R.id.item_header);
        helper.addOnClickListener(R.id.item_header);
        GlideUtil.loadCornerImg(heardImage, item.getHeadPortrait(), 3);

    }
}
