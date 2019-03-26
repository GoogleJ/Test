package com.zxjk.duoduo.ui.walletpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.ReleaseSaleResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.weight.dialog.ConfirmDialog;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\26 0026 购买
 */
@SuppressLint("CheckResult")
public class ConfirmBuyActivity extends BaseActivity {
    private ReleaseSaleResponse data;

    private TextView tvConfirmBuyPayType; //支付类型
    private TextView tvConfirmBuyMoney; //需支付
    private TextView tvConfirmBuyReceiver; //收款人
    private TextView tvConfirmBuyReceiverAccount; //收款账号
    private TextView tvConfirmBuyReceiverQR; //收款二维码
    private TextView tvConfirmBuyOrderId; //订单号
    private TextView tvConfirmBuyReceiverCount; //购买数量
    private TextView tvConfirmBuyReceiverSinglePrice; //单价
    private TextView tvConfirmBuyOrderTime; //订单时间
    private TextView flagConfirmBuy; //支付凭证是否已上传？
    private TextView tvConfirmBuyCountDown; //倒计时

    private ConfirmDialog dialogConfirm;
    private ConfirmDialog dialogCancel;

    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_buy);

        tvConfirmBuyPayType = findViewById(R.id.tvConfirmBuyPayType);
        tvConfirmBuyMoney = findViewById(R.id.tvConfirmBuyMoney);
        tvConfirmBuyReceiver = findViewById(R.id.tvConfirmBuyReceiver);
        tvConfirmBuyReceiverAccount = findViewById(R.id.tvConfirmBuyReceiverAccount);
        tvConfirmBuyReceiverQR = findViewById(R.id.tvConfirmBuyReceiverQR);
        tvConfirmBuyOrderId = findViewById(R.id.tvConfirmBuyOrderId);
        tvConfirmBuyReceiverCount = findViewById(R.id.tvConfirmBuyReceiverCount);
        tvConfirmBuyReceiverSinglePrice = findViewById(R.id.tvConfirmBuyReceiverSinglePrice);
        tvConfirmBuyOrderTime = findViewById(R.id.tvConfirmBuyOrderTime);
        flagConfirmBuy = findViewById(R.id.flagConfirmBuy);
        tvConfirmBuyCountDown = findViewById(R.id.tvConfirmBuyCountDown);

        Observable.interval(1, TimeUnit.SECONDS)
                .take(900)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long l) throws Exception {
                        LogUtils.e(l);
                        long minute = (900 - l) / 60;
                        long second = 60 - l % 60;
                        tvConfirmBuyCountDown.setText(minute + ":" + (second == 60 ? "00" : ((second < 10 ? ("0" + second) : second))));
                    }
                }, this::handleApiError);

        data = (ReleaseSaleResponse) getIntent().getSerializableExtra("data");
    }

    //我已完成支付
    public void confirm(View view) {
        if (!flag) {
            ToastUtils.showShort("请上传支付凭证！");
            return;
        }
        if (dialogConfirm == null) {
            dialogConfirm = new ConfirmDialog(this, "付款确认", "请确认您已向卖家付款，恶意点击 将直接冻结账户。", callback -> {
                ServiceFactory.getInstance().getBaseService(Api.class)
                        .updateBuyPayState("id")
                        .compose(RxSchedulers.normalTrans())
                        .compose(bindToLifecycle())
                        .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
//                            Intent intent = new Intent(this, WaitForJudgeActivity.class);
//                            intent.putExtra("data",data);
//                            startActivity(intent);
                            }
                        }, this::handleApiError);
            });
        }
    }

    //取消订单
    public void cancelOrder(View view) {
        if (dialogCancel == null) {
            dialogCancel = new ConfirmDialog(this, "取消订单", "取消订单不会退款，一天内取消 3笔交易会限制买入功能。", callback -> {
                ServiceFactory.getInstance().getBaseService(Api.class)
                        .cancelled(data.getBuyOrderId(), data.getBothOrderId(), data.getSellOrderId())
                        .compose(RxSchedulers.normalTrans())
                        .compose(bindToLifecycle())
                        .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
//                            Intent intent = new Intent(this, CancelOrderActivity.class);
//                            intent.putExtra("data", data);
//                            startActivity(intent);
                            }
                        }, this::handleApiError);
            });
        }
    }

    //上传支付凭证
    public void uploadSign(View view) {
        flag = true;
    }

    public void back(View view) {
        finish();
    }

}
