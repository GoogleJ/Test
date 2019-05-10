package com.zxjk.duoduo.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.skin.ContactFragment;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.CommunityFragment;
import com.zxjk.duoduo.ui.minepage.MineFragment;
import com.zxjk.duoduo.ui.msgpage.MsgFragment;
import com.zxjk.duoduo.ui.walletpage.WalletFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import static com.ashokvarma.bottomnavigation.BottomNavigationBar.BACKGROUND_STYLE_RIPPLE;
import static com.ashokvarma.bottomnavigation.BottomNavigationBar.BACKGROUND_STYLE_STATIC;
import static com.google.android.material.tabs.TabLayout.MODE_FIXED;

/**
 * todo 1.微信分享  2.exchangelist  3.转账更新
 *
 * @author Administrator
 */
public class HomeActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    BottomNavigationBar m_bottom_bar;
    FrameLayout fragment_content;

    private FragmentTransaction fragmentTransaction;

    private Fragment mFragment;

    MsgFragment msgFragment;
    CommunityFragment communityFragment;
    WalletFragment walletFragment;
    MineFragment mineFragment;
    ContactFragment contactFragment;

    @SuppressLint({"WrongConstant", "CheckResult"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initFriendList();

        initFragment();

        fragment_content = findViewById(R.id.fragment_content);
        m_bottom_bar = findViewById(R.id.m_bottom_bar);

        BadgeItem badgeItem = new BadgeItem();
        badgeItem.setHideOnSelect(false)
                .setBackgroundColorResource(R.color.colorAccent)
                .setBorderWidth(0);
//设置Item选中颜色方法
        m_bottom_bar.setActiveColor(R.color.colorAccent)
                //设置Item未选中颜色方法
                .setInActiveColor(R.color.colorPrimary)
                //背景颜色
                .setBarBackgroundColor("#FFFFFF");

        m_bottom_bar.setMode(BottomNavigationBar.MODE_FIXED);
        // 设置mode
        if (Constant.isVerifyVerision) {
            m_bottom_bar.setMode(MODE_FIXED)
                    // 背景样式
                    .setBackgroundStyle(BACKGROUND_STYLE_RIPPLE)
                    // 背景颜色
                    .setBarBackgroundColor("#ffffff")
                    .setActiveColor("#08C1BC")
                    .setInActiveColor("#000000")
                    // 添加Item
                    .addItem(new BottomNavigationItem(R.drawable.tab_message_icon_nl, "消息").setInactiveIconResource(R.drawable.tab_message_icon_hl).setBadgeItem(badgeItem))
                    .addItem(new BottomNavigationItem(R.drawable.tab_wallet_icon_nl, "联系人").setInactiveIconResource(R.drawable.tab_wallet_icon_hl))
                    .addItem(new BottomNavigationItem(R.drawable.tab_setting_icon_nl, "我的").setInactiveIconResource(R.drawable.tab_setting_icon_hl))
                    //设置默认选中位置
                    .setFirstSelectedPosition(0)
                    // 提交初始化（完成配置）
                    .initialise();
        } else {
            m_bottom_bar.setMode(MODE_FIXED)
                    // 背景样式
                    .setBackgroundStyle(BACKGROUND_STYLE_STATIC)
                    // 背景颜色
                    .setBarBackgroundColor("#ffffff")
                    .setActiveColor("#08C1BC")
                    .setInActiveColor("#000000")
                    // 添加Item
                    .addItem(new BottomNavigationItem(R.drawable.tab_message_icon_nl, "消息").setInactiveIconResource(R.drawable.tab_message_icon_hl).setBadgeItem(badgeItem))
                    .addItem(new BottomNavigationItem(R.drawable.tab_qun_icon_nl, "社群").setInactiveIconResource(R.drawable.tab_qun_icon_hl))
                    .addItem(new BottomNavigationItem(R.drawable.tab_wallet_icon_nl, "钱包").setInactiveIconResource(R.drawable.tab_wallet_icon_hl))
                    .addItem(new BottomNavigationItem(R.drawable.tab_setting_icon_nl, "我的").setInactiveIconResource(R.drawable.tab_setting_icon_hl))
                    //设置默认选中位置
                    .setFirstSelectedPosition(0)
                    // 提交初始化（完成配置）
                    .initialise();
        }

        m_bottom_bar.setTabSelectedListener(this);

        RongIM.getInstance().addUnReadMessageCountChangedObserver(messageCount -> {
            if (messageCount == 0) {
                badgeItem.hide();
            } else {
                if (messageCount > 100) {
                    badgeItem.setText("...");
                } else {
                    badgeItem.setText(String.valueOf(messageCount));
                }
                badgeItem.show(true);
            }
        }, Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP);
    }

    private boolean canFinish = false;

    @SuppressLint("CheckResult")
    @Override
    public void onBackPressed() {
        if (!canFinish) {
            ToastUtils.showShort(R.string.pressagain2finish);
            canFinish = true;
            Observable.timer(2, TimeUnit.SECONDS)
                    .compose(bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(aLong -> canFinish = false);
            return;
        }
        super.onBackPressed();
    }

    @SuppressLint("CheckResult")
    private void initFriendList() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendListById()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.ioObserver())
                .subscribe(friendInfoResponses -> Constant.friendsList = friendInfoResponses.data, t -> initFriendList());
    }

    private void initFragment() {
        msgFragment = new MsgFragment();
        communityFragment = new CommunityFragment();
        walletFragment = new WalletFragment();
        mineFragment = new MineFragment();
        contactFragment = new ContactFragment();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_content, msgFragment)
                .commit();
        mFragment = msgFragment;
    }

    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0:
                switchFragment(msgFragment);
                break;
            case 1:
                if (Constant.isVerifyVerision) {
                    switchFragment(contactFragment);
                    break;
                }
                switchFragment(communityFragment);
                break;
            case 2:
                if (Constant.isVerifyVerision) {
                    switchFragment(mineFragment);
                    break;
                }
                switchFragment(walletFragment);
                break;
            case 3:
                switchFragment(mineFragment);
                break;
            default:
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private void switchFragment(Fragment fragment) {
        //判断当前显示的Fragment是不是切换的Fragment
        if (mFragment != fragment) {
            //判断切换的Fragment是否已经添加过
            if (!fragment.isAdded()) {
                //如果没有，则先把当前的Fragment隐藏，把切换的Fragment添加上
                getSupportFragmentManager().beginTransaction().hide(mFragment)
                        .add(R.id.fragment_content, fragment).commit();
            } else {
                //如果已经添加过，则先把当前的Fragment隐藏，把切换的Fragment显示出来
                getSupportFragmentManager().beginTransaction().hide(mFragment).show(fragment).commit();
            }
            mFragment = fragment;
        }
    }
}