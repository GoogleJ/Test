package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.BaseResponse;
import com.zxjk.duoduo.network.response.GetGroupRedPackageInfoResponse;
import com.zxjk.duoduo.network.response.GetRedPackageStatusResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.RedPackageAdapter;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;

import java.util.ArrayList;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * author L
 * create at 2019/5/9
 * description: 红包详情 状态
 */
public class PeopleUnaccalimedActivity extends BaseActivity {

    private TextView title;
    private TextView tv_end;
    private ImageView head;
    private TextView name;
    private TextView tips;
    private RecyclerView recycler;
    private boolean isShow;
    private TextView tv_redEnvelope;
    private String isGame;

    @SuppressLint({"CheckResult", "SetTextI18n"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_unaccalimed);

        title = findViewById(R.id.title);
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        tv_end = findViewById(R.id.tv_end);
        tv_end.setOnClickListener(v ->
                startActivity(new Intent(PeopleUnaccalimedActivity.this, RedPackageListActivity.class)));
        head = findViewById(R.id.head);
        name = findViewById(R.id.name);
        tips = findViewById(R.id.tips);
        tv_redEnvelope = findViewById(R.id.tv_redEnvelope);
        recycler = findViewById(R.id.recycler);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        RedPackageAdapter adapter = new RedPackageAdapter();
        adapter.setData(new ArrayList<>());
        recycler.setAdapter(adapter);

        String id = getIntent().getStringExtra("id");
        isShow = getIntent().getBooleanExtra("isShow", true);
        if (!isShow) {
            tv_redEnvelope.setVisibility(View.GONE);
        } else {
            tv_redEnvelope.setVisibility(View.VISIBLE);
        }
        isGame = getIntent().getStringExtra("isGame");
        if (TextUtils.isEmpty(isGame)) {
            isGame = "1";
        }
        if (isGame.equals("0")) {
            tips.setVisibility(View.GONE);
        }
        boolean fromList = getIntent().getBooleanExtra("fromList", false);
        if (fromList) {
            tv_end.setVisibility(View.GONE);
        }
        Api baseService = ServiceFactory.getInstance().getBaseService(Api.class);
        baseService.getRedPackageStatus(id, isGame)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(getRedPackageStatusResponse -> {
                    title.setText(getRedPackageStatusResponse.getMessage());
                    GlideUtil.loadCornerImg(head, getRedPackageStatusResponse.getHeadPortrait(), 5);
                    name.setText(getRedPackageStatusResponse.getUsernick() + getString(R.string.red_envelope));
                    if (getRedPackageStatusResponse.getRedPackageType() == 1) {
                        //群组红包
                        baseService
                                .getGroupRedPackageInfo(id)
                                .compose(bindToLifecycle())
                                .compose(RxSchedulers.normalTrans())
                                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                                .subscribe(response -> {
                                    for (int i = 0; i < response.getCustomerInfo().size(); i++) {
                                        if (String.valueOf(response.getCustomerInfo().get(i).getCustomerId()).equals(Constant.currentUser.getId())) {
                                            tv_redEnvelope.setText(response.getCustomerInfo().get(i).getMoney() + " HK");
                                        }
                                    }
                                    int number = response.getRedPackageInfo().getNumber();
                                    tips.setText(number + "个红包，共" + response.getRedPackageInfo().getMoney() + "HK");
                                    if (isGame.equals("0")) {

                                    } else {
                                        adapter.setData(response.getCustomerInfo());
                                    }
                                }, this::handleApiError);
                    } else {
                        //个人红包
                        baseService
                                .personalRedPackageInfo(id, Integer.parseInt(Constant.userId))
                                .compose(bindToLifecycle())
                                .compose(RxSchedulers.normalTrans())
                                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                                .subscribe(response -> {
                                    String money = response.getRedPachageInfo().getMoney();
                                    tv_redEnvelope.setText(money + " HK");
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
                                        bean.setMoney(response.getRedPachageInfo().getMoney());
                                        bean.setCreateTime(response.getReceiveInfo().getTime());
                                        ArrayList<GetGroupRedPackageInfoResponse.CustomerInfoBean> objects = new ArrayList<>(1);
                                        objects.add(bean);
                                        adapter.setData(objects);
                                    }
                                }, this::handleApiError);
                    }
                }, this::handleApiError);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        RedPackageAdapter adapter = new RedPackageAdapter();
        adapter.setData(new ArrayList<>());
        recycler.setAdapter(adapter);

        String id = intent.getStringExtra("id");
        isShow = intent.getBooleanExtra("isShow", true);
        if (!isShow) {
            tv_redEnvelope.setVisibility(View.GONE);
        } else {
            tv_redEnvelope.setVisibility(View.VISIBLE);
        }
        isGame = intent.getStringExtra("isGame");
        if (TextUtils.isEmpty(isGame)) {
            isGame = "1";
        }
        if (isGame.equals("0")) {
            tips.setVisibility(View.GONE);
        }
        boolean fromList = intent.getBooleanExtra("fromList", false);
        if (fromList) {
            tv_end.setVisibility(View.GONE);
        }
        Api baseService = ServiceFactory.getInstance().getBaseService(Api.class);
        baseService.getRedPackageStatus(id, isGame)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(getRedPackageStatusResponse -> {
                    title.setText(getRedPackageStatusResponse.getMessage());
                    GlideUtil.loadCornerImg(head, getRedPackageStatusResponse.getHeadPortrait(), 5);
                    name.setText(getRedPackageStatusResponse.getUsernick() + getString(R.string.red_envelope));
                    if (getRedPackageStatusResponse.getRedPackageType() == 1) {
                        //群组红包
                        baseService
                                .getGroupRedPackageInfo(id)
                                .compose(bindToLifecycle())
                                .compose(RxSchedulers.normalTrans())
                                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                                .subscribe(response -> {
                                    for (int i = 0; i < response.getCustomerInfo().size(); i++) {
                                        if (String.valueOf(response.getCustomerInfo().get(i).getCustomerId()).equals(Constant.currentUser.getId())) {
                                            tv_redEnvelope.setText(response.getCustomerInfo().get(i).getMoney() + " HK");
                                        }
                                    }
                                    int number = response.getRedPackageInfo().getNumber();
                                    tips.setText(number + "个红包，共" + response.getRedPackageInfo().getMoney() + "HK");
                                    if (!isGame.equals("0")) {
                                        adapter.setData(response.getCustomerInfo());
                                    }
                                }, this::handleApiError);
                    } else {
                        //个人红包
                        baseService
                                .personalRedPackageInfo(id, Integer.parseInt(Constant.userId))
                                .compose(bindToLifecycle())
                                .compose(RxSchedulers.normalTrans())
                                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                                .subscribe(response -> {
                                    String money = response.getRedPachageInfo().getMoney();
                                    tv_redEnvelope.setText(money + " HK");
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
                                        bean.setMoney(response.getRedPachageInfo().getMoney());
                                        bean.setCreateTime(response.getReceiveInfo().getTime());
                                        ArrayList<GetGroupRedPackageInfoResponse.CustomerInfoBean> objects = new ArrayList<>(1);
                                        objects.add(bean);
                                        adapter.setData(objects);
                                    }
                                }, this::handleApiError);
                    }
                }, this::handleApiError);
    }
}
