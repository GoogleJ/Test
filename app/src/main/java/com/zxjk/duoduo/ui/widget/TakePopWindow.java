package com.zxjk.duoduo.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxjk.duoduo.R;

public class TakePopWindow extends PopupWindow implements View.OnClickListener {

    private TextView tvCamera, tvPhoto, tvCancel;
    private View mView;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public TakePopWindow(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        mView = inflater.inflate(R.layout.view_pop_bottom, null);
        tvCamera = mView.findViewById(R.id.tvCamera);
        tvPhoto = mView.findViewById(R.id.tvPhoto);
        tvCancel = mView.findViewById(R.id.tvCancel);
        setPopupWindow();
        tvCamera.setOnClickListener(this);
        tvPhoto.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @SuppressLint({"InlinedApi", "ClickableViewAccessibility"})
    private void setPopupWindow() {
        this.setContentView(mView);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.share_pop_style);
        this.setBackgroundDrawable(new ColorDrawable(0xb0000000));//背景透明
        // 如果触摸位置在窗口外面则销毁
        mView.setOnTouchListener((v, event) -> {
            int height = mView.findViewById(R.id.id_pop_layout).getTop();
            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < height) {
                    dismiss();
                }
            }
            return true;
        });
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.setOnItemClick(v);
        }
    }

    public interface OnItemClickListener {
        void setOnItemClick(View v);
    }
}
