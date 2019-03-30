package com.zxjk.duoduo.ui.grouppage.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.AllGroupMembersResponse;
import com.zxjk.duoduo.utils.GlideUtil;

import java.util.List;

import androidx.annotation.Nullable;

public class AddOrDelAdapter extends BaseQuickAdapter<AllGroupMembersResponse, BaseViewHolder> {
    public AddOrDelAdapter() {
        super(R.layout.item_select_contact);
    }

    @Override
    protected void convert(BaseViewHolder helper, AllGroupMembersResponse item) {
        ImageView heardImage=helper.getView(R.id.item_header);
        helper.addOnClickListener(R.id.item_header);
        GlideUtil.loadImg(heardImage,item.getHeadPortrait());

    }
}
