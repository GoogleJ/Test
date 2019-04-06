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
 * @// TODO: 2019\3\19 0019 关于通讯录索引的按钮
 */
public class BaseContactAdapter extends BaseQuickAdapter<FriendInfoResponse, BaseViewHolder> {

    String type = "0";
    Context context;

    public BaseContactAdapter() {
        super(R.layout.item_consatnt_friend);
        context = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendInfoResponse item) {
        helper.setText(R.id.m_user_name, item.getNick())
                .setText(R.id.m_singture_text, item.getSignature())
                .addOnClickListener(R.id.m_constacts_friend)
                .addOnLongClickListener(R.id.m_constacts_friend);
        ImageView heardImage = helper.getView(R.id.m_constants_header_icon);
        GlideUtil.loadCornerImg(heardImage, item.getHeadPortrait(),2);


    }


}
