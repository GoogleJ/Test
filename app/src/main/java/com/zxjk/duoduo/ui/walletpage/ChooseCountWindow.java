package com.zxjk.duoduo.ui.walletpage;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zxjk.duoduo.R;
import java.util.List;
import razerdp.basepopup.BasePopupWindow;

public class ChooseCountWindow extends BasePopupWindow {

    private OnChooseCount onChooseCount;

    void setOnChooseCount(OnChooseCount onChooseCount) {
        this.onChooseCount = onChooseCount;
    }

    public interface OnChooseCount {
        void chooseCount(String count);
    }

    ChooseCountWindow(Context context) {
        super(context);
    }

    public void setData(List<String> data) {
        LinearLayout l = (LinearLayout) getContentView();
        for (int i = 0; i < data.size(); i++) {
            TextView textView = new TextView(getContext());
            textView.setTextSize(15);
            textView.setText(data.get(i));
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(10, 10, 10, 10);
            l.addView(textView);
            textView.setOnClickListener(v -> {
                if (onChooseCount != null) {
                    onChooseCount.chooseCount(textView.getText().toString());
                }
            });
        }
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.window_choosecount);
    }
}
