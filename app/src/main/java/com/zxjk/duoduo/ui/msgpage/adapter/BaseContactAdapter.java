package com.zxjk.duoduo.ui.msgpage.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.utils.GlideUtil;

import java.util.List;

/**
 * @author Administrator
 */
public class BaseContactAdapter extends BaseQuickAdapter<FriendInfoResponse, BaseViewHolder> {

    Context context;

    public BaseContactAdapter() {
        super(R.layout.item_consatnt_friend);
        context = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendInfoResponse item) {

        boolean visiable;
        List<FriendInfoResponse> data = getData();
        if (helper.getAdapterPosition() != 0) {
            visiable = !data.get(helper.getAdapterPosition() - 1).getSortLetters().equals(item.getSortLetters());
        } else {
            visiable = true;
        }
        helper.setText(R.id.m_user_name, item.getNick())
                .setVisible(R.id.tvFirstLetter, visiable)
                .setText(R.id.m_singture_text, helper.itemView.getContext().getString(R.string.bank_real_name) + ":" + item.getRealname())
                .setText(R.id.tvFirstLetter, item.getSortLetters())
                .addOnClickListener(R.id.m_constacts_friend)
                .addOnLongClickListener(R.id.m_constacts_friend);

        ImageView heardImage = helper.getView(R.id.m_constants_header_icon);
        GlideUtil.loadCornerImg(heardImage, item.getHeadPortrait(), 2);

    }
}
