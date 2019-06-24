package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.response.GetVicinityResponse;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;

public class NearByActivity extends BaseActivity {

    private RecyclerView recycler;
    private BaseQuickAdapter<GetVicinityResponse, BaseViewHolder> adapter;
    private List<GetVicinityResponse> nearList;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by);

        AMapLocation l = getIntent().getParcelableExtra("location");

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getVicinity(String.valueOf(l.getLongitude()), String.valueOf(l.getLatitude()))
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(this::initView, this::handleApiError);
    }

    private void initView(List<GetVicinityResponse> nearList) {
        this.nearList = nearList;
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BaseQuickAdapter<GetVicinityResponse, BaseViewHolder>(R.layout.item_nearby, nearList) {
            @Override
            protected void convert(BaseViewHolder helper, GetVicinityResponse item) {
                helper.setText(R.id.tvName, item.getNick())
                        .setText(R.id.tvDistence, item.getDistance());
                GlideUtil.loadCornerImg(helper.getView(R.id.ivHead), item.getHeadPortrait(), 3);

                helper.setImageResource(R.id.ivSex, item.getSex().equals("0") ? R.drawable.icon_gender_man : R.drawable.icon_gender_woman);
            }
        };

        adapter.setOnItemClickListener((adapter1, view, position) ->
                CommonUtils.resolveFriendList(this, ((GetVicinityResponse) adapter1.getData().get(position)).getId()));

        recycler.setAdapter(adapter);

        ((TextView) findViewById(R.id.tv_title)).setText(R.string.collection_nearby);
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());

        RelativeLayout rl_end = findViewById(R.id.rl_end);
        rl_end.setVisibility(View.VISIBLE);
        rl_end.setOnClickListener(v -> detail());
    }

    private void detail() {
        QuickPopupBuilder.with(NearByActivity.this)
                .contentView(R.layout.popup_nearby)
                .config(new QuickPopupConfig()
                        .gravity(Gravity.BOTTOM)
                        .withClick(R.id.tv1, v -> handlePopwindow(1), true)
                        .withClick(R.id.tv2, v -> handlePopwindow(2), true)
                        .withClick(R.id.tv3, v -> handlePopwindow(3), true)
                        .withClick(R.id.tv4, v -> handlePopwindow(4), true)
                )
                .show();
    }

    //1.女生 2.男生 3.全部 4.清除
    private void handlePopwindow(int action) {
        if (adapter == null) {
            return;
        }

        switch (action) {
            case 1:
            case 2:
                adapter.setNewData(handleSex(action));
                break;
            case 3:
                adapter.setNewData(nearList);
                break;
            case 4:
                ServiceFactory.getInstance().getBaseService(Api.class)
                        .getVicinity("", "")
                        .compose(bindToLifecycle())
                        .compose(RxSchedulers.normalTrans())
                        .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(NearByActivity.this)))
                        .subscribe(s -> finish(), this::handleApiError);
                break;
            default:
        }
    }

    private List<GetVicinityResponse> handleSex(int action) {
        List<GetVicinityResponse> result = new ArrayList<>(nearList.size());
        for (GetVicinityResponse r : nearList) {
            if (action == 1) {
                if (!r.getSex().equals("0")) {
                    result.add(r);
                }
            } else {
                if (r.getSex().equals("0")) {
                    result.add(r);
                }
            }
        }
        return result;
    }

}
