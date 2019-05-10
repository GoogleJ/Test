package com.zxjk.duoduo.ui.msgpage.widget;

import android.content.Context;
import android.view.View;
import com.zxjk.duoduo.R;
import razerdp.basepopup.BasePopupWindow;

public class ChooseFanYongPopWindow extends BasePopupWindow {

    public ChooseFanYongPopWindow(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_choose_fanyong);
    }
}
