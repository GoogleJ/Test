package com.zxjk.duoduo.ui.msgpage;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

import androidx.annotation.Nullable;

/**
 * @author Administrator
 * @// TODO: 2019\4\3 0003 领取红包后跳转的页面
 */
public class PeopleRedEnvelopesActivity extends BaseActivity {
    ImageView titleLeftImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_red_envelopes);
        titleLeftImage=findViewById(R.id.titleLeftImage);
        titleLeftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
