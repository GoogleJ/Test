package com.zxjk.duoduo.ui.msgpage.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.PhoneInfo;

/**
 * @author Administrator
 */
public class PhoneContactAdapter extends BaseQuickAdapter<PhoneInfo, BaseViewHolder> {
    private Context context;

    public PhoneContactAdapter(Context context) {
        super(R.layout.item_phone_contact);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PhoneInfo item) {
        View view = helper.getView(R.id.apply_to_add);
        if (!item.isAdd()) {
            helper.setText(R.id.apply_to_add, "邀请")
                    .setTextColor(R.id.apply_to_add, context.getColor(R.color.themecolor));
            view.setBackgroundResource(R.drawable.shape_bac_pressed);
        } else {
            helper.setText(R.id.apply_to_add, "已添加")
                    .setTextColor(R.id.apply_to_add, context.getColor(R.color.textcolor2));
            view.setBackground(null);
        }

        helper.setText(R.id.user_name, item.getName())
                .addOnClickListener(R.id.apply_to_add);


    }
}
