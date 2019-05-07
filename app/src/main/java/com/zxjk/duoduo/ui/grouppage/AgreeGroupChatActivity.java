package com.zxjk.duoduo.ui.grouppage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.othershe.combinebitmap.CombineBitmap;
import com.othershe.combinebitmap.layout.WechatLayoutManager;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.HomeActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.Arrays;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * @author Administrator
 */
public class AgreeGroupChatActivity extends BaseActivity {
    ImageView groupHeader;
    TextView tvGroupName;
    TextView pleaseJoinGroup;
    TextView joinGroupBtn;

    private String groupName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree_group_chat);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.group_invitation));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());

        groupHeader = findViewById(R.id.group_headers);
        tvGroupName = findViewById(R.id.group_chat_name);
        pleaseJoinGroup = findViewById(R.id.invite_to_group_chat);
        joinGroupBtn = findViewById(R.id.join_a_group_chat);
        String inviterId = getIntent().getStringExtra("inviterId");
        String groupId = getIntent().getStringExtra("groupId");
        groupName = getIntent().getStringExtra("groupName");
        String headUrls = getIntent().getStringExtra("headUrls");
        boolean overtime = getIntent().getBooleanExtra("overtime", false);
        if (overtime) {
            joinGroupBtn.setClickable(false);
            joinGroupBtn.setEnabled(false);
            joinGroupBtn.setText(R.string.hasjoined);
        }

        String[] split = headUrls.split(",");
        if (split.length > 9) {
            List<String> strings = Arrays.asList(split);
            List<String> strings1 = strings.subList(0, 9);
            split = new String[strings1.size()];
            for (int i = 0; i < strings1.size(); i++) {
                split[i] = strings1.get(i);
            }
        }

        CombineBitmap.init(this)
                .setLayoutManager(new WechatLayoutManager()) // 必选， 设置图片的组合形式，支持WechatLayoutManager、DingLayoutManager
                .setGapColor(this.getResources().getColor(R.color.grey)) // 单个图片间距的颜色，默认白色
                .setSize(CommonUtils.dip2px(this, 80)) // 必选，组合后Bitmap的尺寸，单位dp
                .setGap(CommonUtils.dip2px(this, 2)) // 单个Bitmap之间的距离，单位dp，默认0dp
                .setUrls(split) // 要加载的图片url数组
                .setImageView(groupHeader) // 直接设置要显示图片的ImageView
                .build();
        tvGroupName.setText(groupName + "(" + headUrls.split(",").length + "人)");

        joinGroupBtn.setOnClickListener(v -> enterGroup(groupId, inviterId, Constant.userId));
    }

    private void enterGroup(String groupId, String inviterId, String customerIds) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .enterGroup(groupId, inviterId, customerIds)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    int id = getIntent().getIntExtra("id", -1);
                    RongIM.getInstance().setMessageExtra(id, "1", new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            Intent intent = new Intent(AgreeGroupChatActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            RongIM.getInstance().startGroupChat(AgreeGroupChatActivity.this, groupId, groupName);
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }, this::handleApiError);
    }
}
