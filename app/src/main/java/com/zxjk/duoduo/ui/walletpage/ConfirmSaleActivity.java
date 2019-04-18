package com.zxjk.duoduo.ui.walletpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.ReleasePurchase;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.dialog.ConfirmDialog;

@SuppressLint("CheckResult")
public class ConfirmSaleActivity extends BaseActivity {

    private ReleasePurchase data;

    private TextView tvConfirmSaleOrderId;
    private TextView tvConfirmSaleCoinType;
    private TextView tvConfirmSalePriceReference;
    private TextView tvConfirmSaleCount;
    private TextView tvConfirmSaleTotalPrice;
    private TextView tvConfirmSalePayType;

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
        tvConfirmSalePayType = findViewById(R.id.tvConfirmSalePayType);

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
                        sb.append("微信" + ",");
                        break;
                    case "2":
                        sb.append("支付宝" + ",");
                        break;
                    case "3":
                        sb.append("银行卡" + ",");
                        break;
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        } else {
            switch (data.getPayType()) {
                case "1":
                    sb.append("微信" + ",");
                    break;
                case "2":
                    sb.append("支付宝" + ",");
                    break;
                case "3":
                    sb.append("银行卡" + ",");
                    break;
            }
        }
        tvConfirmSalePayType.setText(sb.toString());
    }

    // 取消订单
//    public void cancelOrder(View view) {
//        if (dialog == null) {
//            dialog = new ConfirmDialog(this, "取消订单", "您确定要取消正在挂卖的订单么？", callback -> ServiceFactory.getInstance().getBaseService(Api.class)
//                    .closeSellOrder(data.getSellOrderId())
//                    .compose(bindToLifecycle())
//                    .compose(RxSchedulers.normalTrans())
//                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
//                    .subscribe(response -> {
//                        finish();
//                        Intent intent = new Intent(this, CancelOrderActivity.class);
//                        intent.putExtra("data", data);
//                        intent.putExtra("rate", rate);
//                        startActivity(intent);
//                    }, this::handleApiError));
//            dialog.setPoText(R.string.cancel_now);
//            dialog.setNegText(R.string.dontcancel_temp);
//        }
//        dialog.show();
//    }

    // 我的订单
    public void showOrders(View view) {
        finish();
        startActivity(new Intent(this, ExchangeListActivity.class));
    }
}
