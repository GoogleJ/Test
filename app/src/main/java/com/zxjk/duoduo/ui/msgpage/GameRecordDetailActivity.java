package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GetBetInfoDetailsResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

@SuppressLint("CheckResult")
public class GameRecordDetailActivity extends BaseActivity {

    private GetBetInfoDetailsResponse response;
    private MagicIndicator indicator;
    private ViewPager pager;

    private int[] mTitleDataList = new int[]{R.string.game_niuniu, R.string.game_baijiale, R.string.game_daxiao1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_record_detail);

        indicator = findViewById(R.id.indicator);
        pager = findViewById(R.id.pager);

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getBetInfolDetails(getIntent().getStringExtra("redPackageId"), getIntent().getStringExtra("groupId"))
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(response -> {
                    this.response = response;

                    initIndicator();
                    initPager();

                    ViewPagerHelper.bind(indicator, pager);
                }, this::handleApiError);
    }

    private void initPager() {
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                GameRecordDetailFragment gameRecordDetailFragment = new GameRecordDetailFragment();
                gameRecordDetailFragment.ownerBean = response.getGroupOwner();
                if (position == 0) {
                    gameRecordDetailFragment.data = response.getBetInfo().getNiuniu();
                    gameRecordDetailFragment.type = 1;
                } else if (position == 1) {
                    gameRecordDetailFragment.data = response.getBetInfo().getBaijiale();
                    gameRecordDetailFragment.type = 2;
                } else {
                    gameRecordDetailFragment.data = response.getBetInfo().getDaxiao();
                    gameRecordDetailFragment.type = 3;
                }
                return gameRecordDetailFragment;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

            }
        });
    }

    private void initIndicator() {
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
                colorTransitionPagerTitleView.setOnClickListener(view -> pager.setCurrentItem(index));
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        indicator.setNavigator(commonNavigator);
    }

    public void back(View view) {
        finish();
    }
}
