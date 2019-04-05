package com.zxjk.duoduo.ui.msgpage;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;

import androidx.viewpager.widget.ViewPager;

public class RedPackageListActivity extends BaseActivity {

    private MagicIndicator indicator;
    private TextView tvRedListTips1;
    private TextView tvRedListTips2;
    private TextView tvRedListTips3;
    private TextView tvRedListMoney;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_package_list);

        indicator = findViewById(R.id.indicator);
        tvRedListTips1 = findViewById(R.id.tvRedListTips1);
        tvRedListTips2 = findViewById(R.id.tvRedListTips2);
        tvRedListTips3 = findViewById(R.id.tvRedListTips3);
        tvRedListMoney = findViewById(R.id.tvRedListMoney);
        pager = findViewById(R.id.pager);


    }

    public void back(View view) {
        finish();
    }

}
