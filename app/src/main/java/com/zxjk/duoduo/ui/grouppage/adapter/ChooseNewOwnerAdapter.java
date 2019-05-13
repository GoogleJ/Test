package com.zxjk.duoduo.ui.grouppage.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.AllGroupMembersResponse;
import com.zxjk.duoduo.utils.GlideUtil;


/**
 * @author Administrator
 */
public class ChooseNewOwnerAdapter extends BaseQuickAdapter<AllGroupMembersResponse, BaseViewHolder> {
    public ChooseNewOwnerAdapter() {
        super(R.layout.item_choose_new_owner);
    }

    @Override
    protected void convert(BaseViewHolder helper, AllGroupMembersResponse item) {
        helper.setText(R.id.user_name, item.getNick())
                .addOnClickListener(R.id.item_choose);
        ImageView headerImage = helper.getView(R.id.headers);
        GlideUtil.loadCornerImg(headerImage, item.getHeadPortrait(), 5);


    }
}
