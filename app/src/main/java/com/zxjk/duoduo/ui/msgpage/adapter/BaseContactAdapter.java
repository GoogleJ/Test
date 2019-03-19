package com.zxjk.duoduo.ui.msgpage.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.UserBean;
/**
 * @author Administrator
 * @// TODO: 2019\3\19 0019 关于通讯录索引的按钮 
 */
public class BaseContactAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {

    Context context;
    public BaseContactAdapter( ) {
        super(R.layout.item_contact);
        context=mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserBean item) {
        helper.setText(R.id.m_item_contact_user_name_text,item.getUserName());
        TextView typeBtn=helper.getView(R.id.m_item_contact_type_text);
        TextView type1Btn=helper.getView(R.id.m_item_contact_type_text_1);
        if (item.getType()==0){
            typeBtn.setVisibility(View.VISIBLE);
        }else if (item.getType()==1){
            type1Btn.setVisibility(View.GONE);
        }
    }


}
