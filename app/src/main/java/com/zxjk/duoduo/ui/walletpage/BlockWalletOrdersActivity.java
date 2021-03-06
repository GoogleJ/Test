package com.zxjk.duoduo.ui.walletpage;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

public class BlockWalletOrdersActivity extends BaseActivity {

    private MagicIndicator magic_indicator;
    private ViewPager pagerBlockOrders;
    private int[] mTitleDataList = new int[]{R.string.all, R.string.huazhuan, R.string.zhuanchu, R.string.zhuanru, R.string.failed};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_wallet_orders);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.orders));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());

        magic_indicator = findViewById(R.id.magic_indicator);
        pagerBlockOrders = findViewById(R.id.pagerBlockOrders);

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitleDataList == null ? 0 : mTitleDataList.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.themecolor));
                colorTransitionPagerTitleView.setText(mTitleDataList[index]);
                colorTransitionPagerTitleView.setOnClickListener(view -> pagerBlockOrders.setCurrentItem(index));
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        magic_indicator.setNavigator(commonNavigator);

        pagerBlockOrders.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                OrdersFragment fragment = new OrdersFragment();
                if (position == 0) {
                    fragment.type = "1";
                } else if (position == 1) {
                    fragment.type = "2";
                } else if (position == 2) {
                    fragment.type = "3";
                } else if (position == 3) {
                    fragment.type = "4";
                } else {
                    fragment.type = "5";
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 5;
            }
        });

        ViewPagerHelper.bind(magic_indicator, pagerBlockOrders);
    }

}
