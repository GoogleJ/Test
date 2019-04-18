package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GetGroupRedPackageInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.RedPackageAdapter;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PeopleUnaccalimedActivity extends BaseActivity {

    private TextView title;
    private ImageView head;
    private TextView name;
    private TextView tips;
    private TextView tvRedFromList;
    private RecyclerView recycler;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_unaccalimed);

        title = findViewById(R.id.title);
        tvRedFromList = findViewById(R.id.tvRedFromList);
        head = findViewById(R.id.head);
        name = findViewById(R.id.name);
        tips = findViewById(R.id.tips);
        recycler = findViewById(R.id.recycler);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        RedPackageAdapter adapter = new RedPackageAdapter();
        adapter.setData(new ArrayList<>());
        recycler.setAdapter(adapter);

        String id = getIntent().getStringExtra("id");
        boolean fromList = getIntent().getBooleanExtra("fromList", false);
        if (fromList) {
            tvRedFromList.setVisibility(View.GONE);
        }

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getRedPackageStatus(id)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(getRedPackageStatusResponse -> {
                    title.setText(getRedPackageStatusResponse.getMessage());
                    GlideUtil.loadCornerImg(head, getRedPackageStatusResponse.getHeadPortrait(), 3);
                    name.setText(getRedPackageStatusResponse.getUsernick());
                    if (getRedPackageStatusResponse.getRedPackageType() == 1) {
                        //群组红包
                        ServiceFactory.getInstance().getBaseService(Api.class)
                                .getGroupRedPackageInfo(id)
                                .compose(bindToLifecycle())
                                .compose(RxSchedulers.normalTrans())
                                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                                .subscribe(response -> {
                                    String money = response.getRedPackageInfo().getMoney();
                                    int number = response.getRedPackageInfo().getNumber();
                                    tips.setText(number + "个红包，共" + money + "HK");
                                    adapter.setData(response.getCustomerInfo());
                                }, this::handleApiError);
                    } else {
                        //个人红包
                        ServiceFactory.getInstance().getBaseService(Api.class)
                                .personalRedPackageInfo(id, Integer.parseInt(Constant.userId))
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
                    }
                }, this::handleApiError);
    }

    // 红包记录
    public void showRecord(View view) {
        startActivity(new Intent(this, RedPackageListActivity.class));
    }

    public void back(View view) {
        finish();
    }
}
