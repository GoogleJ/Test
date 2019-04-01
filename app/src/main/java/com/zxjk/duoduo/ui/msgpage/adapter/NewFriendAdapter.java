package com.zxjk.duoduo.ui.msgpage.adapter;

import android.graphics.Color;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.utils.GlideUtil;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;


/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 新的朋友的列表适配器
 */
public class NewFriendAdapter extends BaseQuickAdapter<FriendInfoResponse, BaseViewHolder> {
    public NewFriendAdapter() {
        super(R.layout.item_new_friend);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void convert(BaseViewHolder helper, FriendInfoResponse item) {
        helper.setText(R.id.m_item_new_friend_user_name_text, item.getNick())
                .setText(R.id.m_item_new_friend_message_label, item.getSignature())
                .addOnClickListener(R.id.m_item_new_friend_type_btn)
                .addOnClickListener(R.id.m_add_btn_layout)
        .addOnLongClickListener(R.id.m_add_btn_layout);
        ImageView headerImage=helper.getView(R.id.m_item_new_friend_icon);
        GlideUtil.loadCornerImg(headerImage,item.getHeadPortrait(),2);

        TextView typeBtn=helper.getView(R.id.m_item_new_friend_type_btn);
        ConstraintLayout btnLayout=helper.getView(R.id.m_add_btn_layout) ;
        if ("0".equals(item.getStatus())){
            typeBtn.setText(mContext.getString(R.string.add_btn));
            typeBtn.setBackgroundColor(mContext.getColor(R.color.white));
            typeBtn.setTextColor(mContext.getColor(R.color.text_select_color));
        }else if ("2".equals(item.getStatus())){
            typeBtn.setBackgroundColor(Color.WHITE);
            typeBtn.setText(mContext.getString(R.string.m_item_contact_type_text));
            typeBtn.setTextColor(Color.GRAY);
            typeBtn.setEnabled(false);
            btnLayout.setEnabled(false);

        }
    }
}
