package com.zxjk.duoduo.ui.msgpage.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.PhoneInfo;

/**
 * @author Administrator
 * @// TODO: 2019\4\6 0006 关于手机联系人的UI实现
 */
public class PhoneContactAdapter extends BaseQuickAdapter<PhoneInfo, BaseViewHolder> {
    public PhoneContactAdapter() {
        super(R.layout.item_phone_contact);
    }

    @Override
    protected void convert(BaseViewHolder helper, PhoneInfo item) {
        helper.setText(R.id.user_name,item.getName())
        .addOnClickListener(R.id.apply_to_add);

    }
}
