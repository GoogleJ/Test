package com.zxjk.duoduo.ui.msgpage.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.SearchResponse;
import com.zxjk.duoduo.utils.GlideUtil;

public class GlobalSearchAdapter extends BaseQuickAdapter<SearchResponse, BaseViewHolder> {

    Context context;

    public GlobalSearchAdapter() {
        super(R.layout.item_search);
        this.context = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchResponse item) {
        helper.setText(R.id.m_item_search_text, item.getRealname())
                .setText(R.id.m_item_search_dudu_id, item.getDuoduoId())
                .addOnClickListener(R.id.m_item_search_layout);
        ImageView heardImage = helper.getView(R.id.m_item_search_icon);
        GlideUtil.loadImg(heardImage, item.getHeadPortrait());


    }
}
