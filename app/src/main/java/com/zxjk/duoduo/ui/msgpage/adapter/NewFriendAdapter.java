package com.zxjk.duoduo.ui.msgpage.adapter;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.TestBean;


/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 新的朋友的列表适配器
 */
public class NewFriendAdapter extends BaseQuickAdapter<TestBean, BaseViewHolder> {
    public NewFriendAdapter() {
        super(R.layout.item_new_friend);
    }


    @SuppressLint({"ResourceAsColor", "NewApi"})
    @Override
    protected void convert(BaseViewHolder helper, TestBean item) {
        helper.setText(R.id.m_item_new_friend_user_name_text, item.getUserName())
                .setText(R.id.m_item_new_friend_message_label, item.getMessageLabel());
        TextView type = helper.getView(R.id.m_item_new_friend_type_btn);
        if (item.getType().equals("添加")) {
            type.setText(item.getType());
            helper.addOnClickListener(R.id.m_item_new_friend_type_btn);

            type.setTextColor(mContext.getColor(R.color.m_verification_send_btn));


            type.setBackgroundColor(mContext.getColor(R.color.m_verification_send_btn_background));
        } else {
            type.setText(item.getType());

            type.setTextColor(mContext.getColor(R.color.personal_information));
        }


    }
}
