package com.zxjk.duoduo.ui.grouppage.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.AllGroupMembersResponse;
import com.zxjk.duoduo.utils.GlideUtil;

import java.util.List;

import androidx.annotation.Nullable;

public class AllGroupMemebersAdapter extends BaseQuickAdapter<AllGroupMembersResponse, BaseViewHolder> {
    public AllGroupMemebersAdapter() {
        super(R.layout.item_group_chat_information);
    }

    @Override
    protected void convert(BaseViewHolder helper, AllGroupMembersResponse item) {
        helper.setText(R.id.nick_name,item.getNick());

        ImageView heardImage=helper.getView(R.id.header_image);
        GlideUtil.loadCornerImg(heardImage,item.getHeadPortrait(),2);

    }
}
