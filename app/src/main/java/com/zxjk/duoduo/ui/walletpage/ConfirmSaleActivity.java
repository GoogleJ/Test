package com.zxjk.duoduo.ui.walletpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ReleasePurchase;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.dialog.ConfirmDialog;
import com.zxjk.duoduo.utils.CommonUtils;

@SuppressLint("CheckResult")
public class ConfirmSaleActivity extends BaseActivity {

    private ReleasePurchase data;

    private TextView tvConfirmSaleOrderId;
    private TextView tvConfirmSaleCoinType;
    private TextView tvConfirmSalePriceReference;
    private TextView tvConfirmSaleCount;
    private TextView tvConfirmSaleTotalPrice;
    private ImageView iv_wechat, iv_alipay, iv_bank;

    private ConfirmDialog dialog;
    private String rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_sale);

        data = (ReleasePurchase) getIntent().getSerializableExtra("data");
        rate = getIntent().getStringExtra("rate");

        tvConfirmSaleOrderId = findViewById(R.id.tvConfirmSaleOrderId);
        tvConfirmSaleCoinType = findViewById(R.id.tvConfirmSaleCoinType);
        tvConfirmSalePriceReference = findViewById(R.id.tvConfirmSalePriceReference);
        tvConfirmSaleCount = findViewById(R.id.tvConfirmSaleCount);
        tvConfirmSaleTotalPrice = findViewById(R.id.tvConfirmSaleTotalPrice);
        iv_wechat = findViewById(R.id.iv_wechat);
        iv_alipay = findViewById(R.id.iv_alipay);
        iv_bank = findViewById(R.id.iv_bank);

        tvConfirmSaleOrderId.setText(data.getSellOrderId());
        tvConfirmSaleCoinType.setText(data.getCurrency().equals("1") ? "HK" : "Other");
        tvConfirmSalePriceReference.setText(rate);
        tvConfirmSaleCount.setText(data.getNumber());
        tvConfirmSaleTotalPrice.setText(data.getMoney());
        StringBuilder sb = new StringBuilder();
        if (data.getPayType().contains(",")) {
            String[] split = data.getPayType().split(",");
            for (String str : split) {
                switch (str) {
                    case "1":
                        iv_wechat.setVisibility(View.VISIBLE);
                        break;
                    case "2":
                        iv_alipay.setVisibility(View.VISIBLE);

                        break;
                    case "3":
                        iv_bank.setVisibility(View.VISIBLE);
                        break;
                }
            }

        } else {
            switch (data.getPayType()) {
                case "1":
                    iv_wechat.setVisibility(View.VISIBLE);
                    break;
                case "2":
                    iv_alipay.setVisibility(View.VISIBLE);
                    break;
                case "3":
                    iv_bank.setVisibility(View.VISIBLE);
                    break;
            }
        }

    }

    //取消订单
    public void cancelOrder(View view) {
        if (dialog == null) {
            dialog = new ConfirmDialog(this, "取消订单", "您确定要取消正在挂卖的订单么？", callback -> ServiceFactory.getInstance().getBaseService(Api.class)
                    .closeSellOrder(data.getSellOrderId())
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .subscribe(response -> {
                        finish();
                        Intent intent = new Intent(this, CancelOrderActivity.class);
                        intent.putExtra("data", data);
                        intent.putExtra("rate", rate);
                        startActivity(intent);
                    }, this::handleApiError));
            dialog.setPoText(R.string.cancel_now);
            dialog.setNegText(R.string.dontcancel_temp);
        }
        dialog.show();
    }

    public void back(View view) {
        finish();
    }
}
