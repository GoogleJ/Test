package com.zxjk.duoduo.ui;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.blankj.utilcode.util.VibrateUtils;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.zxjk.duoduo.BuildConfig;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.DuoDuoFileProvider;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.response.GetAppVersionResponse;
import com.zxjk.duoduo.network.rx.RxException;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.skin.ContactFragment;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.minepage.MineFragment;
import com.zxjk.duoduo.ui.msgpage.MsgFragment;
import com.zxjk.duoduo.utils.MMKVUtils;
import com.zxjk.duoduo.utils.badge.BadgeNumberManager;
import com.zxjk.duoduo.utils.badge.MobileBrand;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.message.CommandMessage;
import io.rong.pushperm.ResultCallback;
import io.rong.pushperm.RongPushPremissionsCheckHelper;

import static com.ashokvarma.bottomnavigation.BottomNavigationBar.BACKGROUND_STYLE_RIPPLE;
import static com.ashokvarma.bottomnavigation.BottomNavigationBar.BACKGROUND_STYLE_STATIC;
import static com.google.android.material.tabs.TabLayout.MODE_FIXED;

/**
 * @author Administrator
 */
public class HomeActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    BottomNavigationBar m_bottom_bar;
    FrameLayout fragment_content;

    private FragmentTransaction fragmentTransaction;

    private Fragment mFragment;

    public BadgeItem badgeItem2;

    private MsgFragment msgFragment;
    private MineFragment mineFragment;
    private ContactFragment contactFragment;

    private int messageCount;

    @Override
    protected void onDestroy() {
        RongIM.setOnReceiveMessageListener(null);
        super.onDestroy();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onResume() {
        cleanBadge();
        Observable.timer(1, TimeUnit.SECONDS)
                .subscribe(aLong -> RongIM.setOnReceiveMessageListener((message, i) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationManager mNotificationManager = (NotificationManager) this
                                .getSystemService(Context.NOTIFICATION_SERVICE);
                        List<NotificationChannel> notificationChannels = mNotificationManager.getNotificationChannels();
                        List<NotificationChannelGroup> notificationChannelGroups = mNotificationManager.getNotificationChannelGroups();
                    }
                    //update badge
                    if (!AppUtils.isAppForeground()) {
                        if (!Build.MANUFACTURER.equalsIgnoreCase(MobileBrand.XIAOMI)) {
                            BadgeNumberManager.from(this).setBadgeNumber(++messageCount);
                        } else {
                            //MIUI
//                            NotificationManager mNotificationManager = (NotificationManager) this
//                                    .getSystemService(Context.NOTIFICATION_SERVICE);
//                            StatusBarNotification[] activeNotifications = mNotificationManager.getActiveNotifications();
//                            if (activeNotifications.length != 0) {
//                                BadgeNumberManagerXiaoMi.setBadgeNumber(activeNotifications[0].getNotification(), ++messageCount);
//                                mNotificationManager.notify(activeNotifications[0].getId(), activeNotifications[0].getNotification());
//                            }
                        }
                    }

                    //handle command(delete add newfriend)
                    if (message.getContent() instanceof CommandMessage) {
                        CommandMessage commandMessage = (CommandMessage) message.getContent();
                        if (commandMessage.getName().equals("deleteFriend")) {
                            RongIM.getInstance().clearMessages(Conversation.ConversationType.PRIVATE, message.getSenderUserId(), null);
                            RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, message.getSenderUserId(), null);
                        } else if (commandMessage.getName().equals("addFriend")) {
                            runOnUiThread(() -> {
                                long newFriendCount = MMKVUtils.getInstance().decodeLong("newFriendCount");
                                newFriendCount += 1;
                                MMKVUtils.getInstance().enCode("newFriendCount", newFriendCount);
                                if (newFriendCount == 0) {
                                    badgeItem2.hide();
                                } else {
                                    badgeItem2.show(true);
                                    if (newFriendCount >= 100) {
                                        badgeItem2.setText("99+");
                                    } else {
                                        badgeItem2.setText(String.valueOf(newFriendCount));
                                    }
                                }
                                if (contactFragment.getDotNewFriend() != null) {
                                    contactFragment.getDotNewFriend().setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                    return false;
                }));
        super.onResume();
    }

    @SuppressLint({"WrongConstant", "CheckResult"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getVersion();

        initFriendList();

        initFragment();

        initView();

        getNewFriendCount();

        checkPermission();
    }

    private void cleanBadge() {
        messageCount = 0;
        if (!Build.MANUFACTURER.equalsIgnoreCase(MobileBrand.XIAOMI)) {
            BadgeNumberManager.from(this).setBadgeNumber(0);
        }
    }

    private void checkPermission() {
        RongPushPremissionsCheckHelper.checkPermissionsAndShowDialog(this, new ResultCallback() {
            @Override
            public void onAreadlyOpened(String value) {

            }

            @Override
            public boolean onBeforeShowDialog(String value) {
                return false;
            }

            @Override
            public void onGoToSetting(String value) {

            }

            @Override
            public void onFailed(String value, FailedType type) {

            }
        });
    }

    private void initView() {
        fragment_content = findViewById(R.id.fragment_content);
        m_bottom_bar = findViewById(R.id.m_bottom_bar);

        BadgeItem badgeItem = new BadgeItem();
        badgeItem.setHideOnSelect(false)
                .setBackgroundColorResource(R.color.red_eth_in)
                .setBorderWidth(0);

        badgeItem2 = new BadgeItem();
        badgeItem2.setHideOnSelect(false)
                .setBackgroundColorResource(R.color.red_eth_in)
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
                    .addItem(new BottomNavigationItem(R.drawable.tab_qun_icon_nl, "通讯录").setInactiveIconResource(R.drawable.tab_qun_icon_hl).setBadgeItem(badgeItem2))
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
                if (messageCount >= 100) {
                    badgeItem.setText("99+");
                } else {
                    badgeItem.setText(String.valueOf(messageCount));
                }
                badgeItem.show(true);
            }
        }, Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP);
    }

    private void getNewFriendCount() {
        long newFriendCount = MMKVUtils.getInstance().decodeLong("newFriendCount");
        if (newFriendCount == 0) {
            badgeItem2.hide();
        } else {
            badgeItem2.show(true);
            if (newFriendCount >= 100) {
                badgeItem2.setText("99+");
            } else {
                badgeItem2.setText(String.valueOf(newFriendCount));
            }
        }
    }

    private long max1;

    //获取版本
    @SuppressLint("CheckResult")
    private void getVersion() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getAppVersion()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .subscribe(response -> {
                    if (response.code != Constant.CODE_SUCCESS) {
                        return;
                    }
                    GetAppVersionResponse data = response.data;
                    String appVersionName = AppUtils.getAppVersionName();

                    File file = new File(Utils.getApp().getCacheDir(), data.getVersion() + ".apk");
                    if (file.exists()) {
                        file.delete();
                    }

                    if (!appVersionName.equals(data.getVersion())) {
                        NiceDialog.init().setLayoutId(R.layout.dialog_update).setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                                holder.setText(R.id.tv, data.getUpdateContent());
                                ((TextView) holder.getView(R.id.tv)).setMovementMethod(new ScrollingMovementMethod());
                                holder.getView(R.id.ivClose).setVisibility(data.getIsEnforcement().equals("0") ? View.VISIBLE : View.GONE);
                                TextView tvUpdate = holder.getView(R.id.tvUpdate);
                                tvUpdate.setOnClickListener(v -> {
                                    //后台下载APK并更新
                                    ServiceFactory.downloadFile(data.getVersion(), data.getUpdateAddress(), new ServiceFactory.DownloadListener() {
                                        @Override
                                        public void onStart(long max) {
                                            runOnUiThread(() -> tvUpdate.setClickable(false));
                                            max1 = max;
                                            ToastUtils.showShort(R.string.update_start);
                                        }

                                        @Override
                                        public void onProgress(long progress) {
                                            runOnUiThread(() -> tvUpdate.setText((int) ((float) progress / max1 * 100) + "%"));
                                        }

                                        @Override
                                        public void onSuccess() {
                                            runOnUiThread(() -> {
                                                tvUpdate.setClickable(true);
                                                tvUpdate.setText(R.string.dianjianzhuang);
                                                tvUpdate.setOnClickListener(v1 -> {
                                                    File file = new File(Utils.getApp().getCacheDir(), data.getVersion() + ".apk");// 设置路径
                                                    Intent intent = installIntent(file.getPath());
                                                    if (intent != null) {
                                                        startActivity(intent);
                                                    }
                                                });
                                                tvUpdate.performClick();
                                            });
                                        }

                                        @Override
                                        public void onFailure() {
                                            runOnUiThread(() -> {
                                                File file = new File(Utils.getApp().getCacheDir(), data.getVersion() + ".apk");
                                                if (file.exists()) {
                                                    file.delete();
                                                }
                                                tvUpdate.setClickable(true);
                                                tvUpdate.setText(R.string.dianjichongshi);
                                            });
                                            ToastUtils.showShort(R.string.update_failure);
                                        }
                                    });
                                });
                                holder.setOnClickListener(R.id.ivClose, v -> dialog.dismiss());
                            }
                        }).setDimAmount(0.5f).setOutCancel(false).show(getSupportFragmentManager());
                    }
                }, t -> {
                });
    }

    private Intent installIntent(String path) {
        try {
            File file = new File(path);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(DuoDuoFileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".FileProvider", file),
                        "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            return intent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

    @SuppressLint("CheckResult")
    private void initFriendList() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendListById()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.ioObserver())
                .subscribe(response -> {
                    Constant.friendsList = response.data;
                    for (FriendInfoResponse f : response.data) {
                        RongUserInfoManager.getInstance().setUserInfo(new UserInfo(f.getId(), TextUtils.isEmpty(f.getRemark()) ? f.getNick() : f.getRemark(), Uri.parse(f.getHeadPortrait())));
                    }
                }, t -> {
                    //重复登录不再递归，避免过多请求
                    if (t.getCause() instanceof RxException.DuplicateLoginExcepiton ||
                            t instanceof RxException.DuplicateLoginExcepiton) {
                        return;
                    }
                    Observable.timer(10, TimeUnit.SECONDS)
                            .subscribe(aLong -> initFriendList());
                });
    }

    private void initFragment() {
        msgFragment = new MsgFragment();
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
                switchFragment(contactFragment);
                break;
            case 2:
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
        VibrateUtils.vibrate(50);
        if (mFragment != fragment) {
            if (!fragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().hide(mFragment)
                        .add(R.id.fragment_content, fragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().hide(mFragment).show(fragment).commit();
            }
            mFragment = fragment;
        }
    }
}