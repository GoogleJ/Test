package com.zxjk.duoduo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.GlideUtil;

public class ImgActivity extends BaseActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);

        iv = findViewById(R.id.iv);

        GlideUtil.loadNormalImg(iv, getIntent().getStringExtra("url"));
    }

    public void back(View view) {
        finish();
    }
}
