package com.zxjk.duoduo.ui.grouppage.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.response.AllGroupMembersResponse;
import com.zxjk.duoduo.utils.GlideUtil;


public class AllGroupMemebersAdapter1 extends BaseQuickAdapter<AllGroupMembersResponse, BaseViewHolder> {
    public AllGroupMemebersAdapter1() {
        super(R.layout.item_group_chat_information);
    }

    @Override
    protected void convert(BaseViewHolder helper, AllGroupMembersResponse item) {
        helper.setText(R.id.nick_name, item.getNick());
        ImageView heardImage = helper.getView(R.id.header_image);
        TextView nick_owner = helper.getView(R.id.nick_owner);
        GlideUtil.loadCornerImg(heardImage, item.getHeadPortrait(), 5);

        if (helper.getAdapterPosition() == 0) {
            nick_owner.setVisibility(View.VISIBLE);
        } else {
            nick_owner.setVisibility(View.GONE);
        }
    }
}
