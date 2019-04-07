package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GetGroupRedPackageInfoResponse;
import com.zxjk.duoduo.network.response.PersonalRedPackageInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.RedPackageAdapter;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.RedPacketMessage;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.functions.Consumer;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * @author Administrator
 * @// TODO: 2019\4\6 0006 红包未领取界面
 */
public class PeopleUnaccalimedActivity extends BaseActivity {

    private TextView title;
    private ImageView head;
    private TextView name;
    private TextView tips;
    private RecyclerView recycler;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_unaccalimed);

        title = findViewById(R.id.title);
        head = findViewById(R.id.head);
        name = findViewById(R.id.name);
        tips = findViewById(R.id.tips);
        recycler = findViewById(R.id.recycler);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        RedPackageAdapter adapter = new RedPackageAdapter();
        adapter.setData(new ArrayList<>());
        recycler.setAdapter(adapter);

        String type = getIntent().getStringExtra("type");
        Message message = getIntent().getParcelableExtra("msg");
        RedPacketMessage redPacketMessage = (RedPacketMessage) message.getContent();
        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId());

        title.setText(redPacketMessage.getRemark());
        GlideUtil.loadCornerImg(head, userInfo.getPortraitUri().toString(), 3);
        name.setText(userInfo.getName());

        if (type.equals("private")) {
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .personalRedPackageInfo(redPacketMessage.getRedId())
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .subscribe(response -> {
                        String money = response.getRedPachageInfo().getMoney();
                        if (response.getRedPachageInfo().getStatus().equals("0")) {
                            //未领取
                            tips.setText("红包金额" + money + "HK,等待对方领取");
                        } else {
                            //已领取
                            tips.setText("红包金额" + money + "HK,已被领取");
                        }
                        GetGroupRedPackageInfoResponse.CustomerInfoBean bean = new GetGroupRedPackageInfoResponse.CustomerInfoBean();
                        if (response.getRedPachageInfo().getStatus().equals("1")) {
                            bean.setHeadPortrait(response.getReceiveInfo().getHeadPortrait());
                            bean.setNick(response.getReceiveInfo().getUsernick());
                            bean.setMoney(Double.parseDouble(response.getRedPachageInfo().getMoney()));
                            bean.setCreateTime(response.getReceiveInfo().getTime());
                            ArrayList<GetGroupRedPackageInfoResponse.CustomerInfoBean> objects = new ArrayList<>(1);
                            objects.add(bean);
                            adapter.setData(objects);
                        }
                    }, this::handleApiError);
        } else {
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getGroupRedPackageInfo(redPacketMessage.getRedId())
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .subscribe(response -> {
                        String money = response.getRedPackageInfo().getMoney();
                        int number = response.getRedPackageInfo().getNumber();
                        tips.setText(number + "个红包，共" + money + "HK");
                        adapter.setData(response.getCustomerInfo());
                    }, this::handleApiError);
        }
    }

    // 红包记录
    public void showRecord(View view) {
        startActivity(new Intent(this, RedPackageListActivity.class));
    }

    public void back(View view) {
        finish();
    }
}
