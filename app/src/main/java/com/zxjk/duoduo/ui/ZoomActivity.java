package com.zxjk.duoduo.ui;

import android.os.Bundle;
import android.view.View;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.PinchImageView;
import com.zxjk.duoduo.utils.GlideUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * *********************
 * Administrator
 * *********************
 * 2019/5/31
 * *********************
 * 头像放大
 * *********************
 */
public class ZoomActivity extends BaseActivity {

    @BindView(R.id.pic)
    PinchImageView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        String imageUrl = getIntent().getStringExtra("image");
        GlideUtil.loadNormalImg(pic, imageUrl);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
            }
        });


    }
}
