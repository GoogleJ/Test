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

    String type="0";
    Context context;
    public BaseContactAdapter( ) {
        super(R.layout.item_contact);
        context=mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendListResponse item) {
        helper.setText(R.id.m_item_contact_user_name_text,item.getNick());
        ImageView heardImage=helper.getView(R.id.m_item_contact_heard_icon);
        GlideUtil.loadImg(heardImage,item.getHeadPortrait());
        TextView typeText=helper.getView(R.id.m_item_contact_type_text_1);
        if (type.equals(item.getIsDelete())){
            typeText.setText("申请添加");
            typeText.setTextColor(Color.BLACK);
        }else{
            typeText.setBackgroundColor(Color.WHITE);
            typeText.setTextColor(Color.GRAY);
            typeText.setText("已添加");
        }



    }


}
