package com.zxjk.duoduo.ui.msgpage.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.SearchBean;
import com.zxjk.duoduo.network.response.SearchCustomerInfoResponse;
import com.zxjk.duoduo.network.response.SearchResponse;
import com.zxjk.duoduo.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 搜索的适配器
 */
public class SearchAdapter extends BaseQuickAdapter<SearchCustomerInfoResponse, BaseViewHolder> {

    Context context;

    public SearchAdapter() {
        super(R.layout.item_search);
        this.context = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchCustomerInfoResponse item) {
        helper.setText(R.id.m_item_search_text, item.getRealname())
                .setText(R.id.m_item_search_dudu_id, item.getDuoduoId())
                .addOnClickListener(R.id.m_item_search_layout);
        ImageView heardImage = helper.getView(R.id.m_item_search_icon);
        GlideUtil.loadImg(heardImage, item.getHeadPortrait());


    }
}
