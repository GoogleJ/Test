package com.zxjk.duoduo.ui.grouppage;

import android.os.Bundle;
import android.widget.ImageView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

import androidx.annotation.Nullable;

/**
 * @author Administrator
 * @// TODO: 2019\3\26 0026 群组发红包界面
 */
public class SendRedEnvelopesActivity extends BaseActivity {
    ImageView right_title_image;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_red_envelopes);
        right_title_image=findViewById(R.id.right_title_image);
    }
}
