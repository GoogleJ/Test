package com.zxjk.duoduo.ui.walletpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GetOverOrderResponse;
import com.zxjk.duoduo.network.response.GetReleasePurchaseResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.walletpage.adapter.ExchangeListAdapter;
import com.zxjk.duoduo.utils.CommonUtils;
import java.util.ArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Administrator
 * @// TODO: 2019\4\6 0006 交易所记录
 */
@SuppressLint("CheckResult")

public class ExchangeListActivity extends BaseActivity {

    private RecyclerView recyclerExchangeMyOrder;
    private RadioGroup rgExchangeTop;
    // 1.进行中 2.已完成
    private int flag;
    // 如果有刷新的需求，给refresh设值
    private boolean refresh;

    private ArrayList<GetOverOrderResponse> overOrders;
    private ArrayList<GetReleasePurchaseResponse> releaseList;
    ExchangeListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_list);

        recyclerExchangeMyOrder = findViewById(R.id.recyclerExchangeMyOrder);
        rgExchangeTop = findViewById(R.id.rgExchangeTop);

        rgExchangeTop.setOnCheckedChangeListener((group, id) -> {
            switch (id) {
                case R.id.rb1:
                    flag = 1;
                    reloadData();
                    recyclerExchangeMyOrder.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
                    mAdapter=new ExchangeListAdapter("1");
                    mAdapter.setNewData(overOrders);
                    recyclerExchangeMyOrder.setAdapter(mAdapter);
                    mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                            Intent intent=new Intent(ExchangeListActivity.this,ExchangeListTypeActivity.class);
                            intent.putExtra("exchangeType",mAdapter.getData().get(position));
                            startActivity(intent);
                        }
                    });
                    break;
                case R.id.rb2:
                    flag = 2;
                    reloadData();
                    recyclerExchangeMyOrder.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
                    mAdapter=new ExchangeListAdapter("0");
                    mAdapter.setNewData(overOrders);
                    recyclerExchangeMyOrder.setAdapter(mAdapter);
                    mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                            Intent intent=new Intent(ExchangeListActivity.this,ExchangeListTypeActivity.class);
                            intent.putExtra("exchangeType",mAdapter.getData().get(position));
                            startActivity(intent);
                        }
                    });
                    break;
                default:
            }
        });

        rgExchangeTop.check(R.id.rb1);


    }

    private void reloadData() {
        if (flag == 1) {
            if (overOrders == null || refresh) {
                overOrders = new ArrayList<>();
                ServiceFactory.getInstance().getBaseService(Api.class)
                        .getOverOrder()
                        .compose(bindToLifecycle())
                        .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                        .compose(RxSchedulers.normalTrans())
                        .subscribe(overOrders -> this.overOrders = (ArrayList<GetOverOrderResponse>) overOrders, this::handleApiError);
            }
        } else if (flag == 2) {
            if (releaseList == null || refresh) {
                ServiceFactory.getInstance().getBaseService(Api.class)
                        .getReleasePurchase()
                        .compose(bindToLifecycle())
                        .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                        .compose(RxSchedulers.normalTrans())
                        .subscribe(releaseList -> this.releaseList = (ArrayList<GetReleasePurchaseResponse>) releaseList, this::handleApiError);
            }
        }
    }

    public void back(View view) {
        finish();
    }
}
