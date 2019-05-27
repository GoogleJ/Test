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
 */
public class SearchAdapter extends BaseQuickAdapter<FriendInfoResponse, BaseViewHolder> {

    Context context;

    public SearchAdapter(Context context) {
        super(R.layout.item_consatnt_friend);
        context = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendInfoResponse item) {
        helper.setText(R.id.m_user_name, item.getNick())
                .setText(R.id.m_singture_text, helper.itemView.getContext().getString(R.string.bank_real_name) + ":" + item.getRealname());


        ImageView heardImage = helper.getView(R.id.m_constants_header_icon);
        GlideUtil.loadCornerImg(heardImage, item.getHeadPortrait(), 5);
    }
}
