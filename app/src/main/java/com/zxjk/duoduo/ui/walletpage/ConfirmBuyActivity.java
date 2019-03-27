package com.zxjk.duoduo.ui.walletpage;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ReleasePurchase;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.ReleaseSaleResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.weight.dialog.ConfirmDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
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
                .subscribe(l -> {
                    long minute = (900 - l) / 60;
                    long second = 60 - l % 60;
                    tvConfirmBuyCountDown.setText(minute + ":" + (second == 60 ? "00" : ((second < 10 ? ("0" + second) : second))));
                }, this::handleApiError);

        data = (ReleaseSaleResponse) getIntent().getSerializableExtra("data");
        String buytype = getIntent().getStringExtra("buytype");

        Drawable drawable = getDrawable(R.drawable.ic_exchange_wechat);
        switch (buytype) {
            case "1":
                tvConfirmBuyPayType.setText(R.string.wechatPay);
                drawable = getDrawable(R.drawable.ic_exchange_wechat);
                break;
            case "2":
                tvConfirmBuyPayType.setText(R.string.alipay);
                drawable = getDrawable(R.drawable.ic_exchange_alipay);
                break;
            case "3":
                tvConfirmBuyPayType.setText(R.string.bankcard);
                drawable = getDrawable(R.drawable.ic_exchange_bankpay);
                break;
            default:
        }
        tvConfirmBuyPayType.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

        tvConfirmBuyReceiver.setText(data.getNick());
        tvConfirmBuyReceiverAccount.setText(data.getReceiptNumber());
        tvConfirmBuyOrderId.setText(data.getBothOrderId());
        tvConfirmBuyReceiverCount.setText(data.getNumber());
        tvConfirmBuyReceiverSinglePrice.setText(getIntent().getStringExtra("rate"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tvConfirmBuyOrderTime.setText(format.format(new Date(data.getCreateTime())));
        tvConfirmBuyMoney.setText(data.getMoney());
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
                        .subscribe(s -> {
                            Intent intent = new Intent(this, WaitForJudgeActivity.class);
                            intent.putExtra("data", data);
                            intent.putExtra("buytype", getIntent().getStringExtra("buytype"));
                            intent.putExtra("rate", getIntent().getStringExtra("rate"));
                            startActivity(intent);
                            finish();
                        }, this::handleApiError);
            });
        }
        dialogConfirm.show();
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
                        .subscribe(s -> {
                            ReleasePurchase result = new ReleasePurchase();
                            result.setSellOrderId(data.getBothOrderId());
                            result.setCurrency(data.getCurrency());
                            result.setNumber(data.getNumber());
                            result.setMoney(data.getMoney());
                            result.setPayType(getIntent().getStringExtra("buytype"));
                            Intent intent = new Intent(this, CancelOrderActivity.class);
                            intent.putExtra("data", result);
                            intent.putExtra("rate", getIntent().getStringExtra("rate"));
                            startActivity(intent);
                            finish();
                        }, this::handleApiError);
            });
        }
        dialogCancel.show();
    }

    //上传支付凭证
    public void uploadSign(View view) {
        flag = true;
    }

    public void copyNick(View view) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(tvConfirmBuyReceiver.getText().toString());
        ToastUtils.showShort("已复制至剪切板");
    }

    public void copyAccount(View view) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(tvConfirmBuyReceiverAccount.getText().toString());
        ToastUtils.showShort("已复制至剪切板");
    }

    public void back(View view) {
        finish();
    }

}
