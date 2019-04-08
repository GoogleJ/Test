package com.zxjk.duoduo.ui.msgpage.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.utils.GlideUtil;

/**
 * @author Administrator
 * @// TODO: 2019\4\8 0008 搜索适配器
 */
public class GlobalSearchAdapter extends BaseQuickAdapter<FriendInfoResponse, BaseViewHolder> {

    Context context;

    public GlobalSearchAdapter() {
        super(R.layout.item_search);
        this.context = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendInfoResponse item) {
        helper.setText(R.id.m_item_search_text, item.getNick())
                .setText(R.id.m_item_search_dudu_id, item.getDuoduoId())
                .addOnClickListener(R.id.m_item_search_layout);
        ImageView heardImage = helper.getView(R.id.m_item_search_icon);
        GlideUtil.loadImg(heardImage, item.getHeadPortrait());


    }
}
