package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
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

import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GroupResponse;
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

/**
 * author L
 * create at 2019/5/8
 * description: 积分详情
 */
public class GameRecordActivity extends BaseActivity {

    private MagicIndicator indicator;
    private ViewPager pager;
    private int[] mTitleDataList1 = new int[]{R.string.jifen, R.string.dailifanyong};
    private int[] mTitleDataList2 = new int[]{R.string.jifen, R.string.fanyongfafang};
    private boolean isOwner = false;
    private String groupId;
    GroupResponse groupResponse = new GroupResponse();


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_record);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.scoredetail));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());

        groupId = getIntent().getStringExtra("groupId");

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getGroupByGroupId(groupId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(response -> {
                    groupResponse = response;
                    String groupOwnerId = response.getGroupInfo().getGroupOwnerId();
                    if (groupOwnerId.equals(Constant.userId)) {
                        isOwner = true;
                    }

                    indicator = findViewById(R.id.indicator);
                    pager = findViewById(R.id.pager);

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
                if (position == 0) {
                    GameRecordFragment fragment1 = new GameRecordFragment();
                    fragment1.groupId = groupId;
                    fragment1.groupResponse = groupResponse;
                    return fragment1;
                } else {
                    if (isOwner) {
                        GameRecordOwnerFragment fragment3 = new GameRecordOwnerFragment();
                        fragment3.groupId = groupId;
                        return fragment3;
                    }
                    GameRecordDaiLiFragment fragment2 = new GameRecordDaiLiFragment();
                    fragment2.groupId = groupId;
                    return fragment2;
                }
            }

            @Override
            public int getCount() {
                return 2;
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
                return 2;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.themecolor));
                colorTransitionPagerTitleView.setText(isOwner ? mTitleDataList2[index] : mTitleDataList1[index]);
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

}
