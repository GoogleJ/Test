package com.zxjk.duoduo.ui.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.utils.WeChatShareUtil;

import razerdp.basepopup.BasePopupWindow;

public class SharePopWindow extends BasePopupWindow implements View.OnClickListener {

    public SharePopWindow(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        bind();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.window_share);
    }

    private void bind() {
        findViewById(R.id.tvShareWechat).setOnClickListener(this);
        findViewById(R.id.tvShareTimeLine).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (v.getId() == R.id.tvShareWechat) {
            WeChatShareUtil.shareImg(v.getContext(), 0);
        } else if (v.getId() == R.id.tvShareTimeLine) {
            WeChatShareUtil.shareImg(v.getContext(), 1);
        }
    }
}
