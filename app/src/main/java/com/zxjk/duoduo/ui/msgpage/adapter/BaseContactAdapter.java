package com.zxjk.duoduo.ui.msgpage.adapter;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.UFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.UserBean;
import com.zxjk.duoduo.network.response.FriendListResponse;
import com.zxjk.duoduo.utils.GlideUtil;

/**
 * @author Administrator
 * @// TODO: 2019\3\19 0019 关于通讯录索引的按钮
 */
public class BaseContactAdapter extends BaseQuickAdapter<FriendListResponse, BaseViewHolder> {

    String type = "0";
    Context context;

    public BaseContactAdapter() {
        super(R.layout.item_consatnt_friend);
        context = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendListResponse item) {
        helper.setText(R.id.m_user_name, item.getRealname())
                .setText(R.id.m_singture_text, item.getSignature())
                .addOnClickListener(R.id.m_constacts_friend)
                .addOnLongClickListener(R.id.m_constacts_friend);
        ImageView heardImage = helper.getView(R.id.m_constants_header_icon);
        GlideUtil.loadImg(heardImage, item.getHeadPortrait());


    }


}
