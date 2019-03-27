package com.zxjk.duoduo.ui.walletpage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.ReleaseSaleResponse;
import com.zxjk.duoduo.ui.base.BaseActivity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

@SuppressLint("CheckResult")
public class WaitForJudgeActivity extends BaseActivity {

    private TextView tvWaitForJudgeCountDown;

    private TextView tvWaitForJudgeOrderId;
    private TextView tvWaitForJudgeMoney;
    private TextView tvWaitForJudgePrice;
    private TextView tvWaitForJudgeCount;
    private TextView tvWaitForJudgeTime;
    private TextView tvWaitForJudgeReceiver;
    private TextView tvWaitForJudgeReceiverAccount;
    private TextView tvWaitForJudgePayType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_judge);

        tvWaitForJudgeCountDown = findViewById(R.id.tvWaitForJudgeCountDown);
        Observable.interval(1, TimeUnit.SECONDS)
                .take(600)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l -> {
                    String second = String.valueOf(60 - l % 60).equals("60") ? "00" : String.valueOf(60 - l % 60);
                    String minute = String.valueOf(600 / (600 - l));
                    tvWaitForJudgeCountDown.setText(minute + ":" + second);
                }, t -> {
                });

        tvWaitForJudgeOrderId = findViewById(R.id.tvWaitForJudgeOrderId);
        tvWaitForJudgeMoney = findViewById(R.id.tvWaitForJudgeMoney);
        tvWaitForJudgePrice = findViewById(R.id.tvWaitForJudgePrice);
        tvWaitForJudgeCount = findViewById(R.id.tvWaitForJudgeCount);
        tvWaitForJudgeTime = findViewById(R.id.tvWaitForJudgeTime);
        tvWaitForJudgeReceiver = findViewById(R.id.tvWaitForJudgeReceiver);
        tvWaitForJudgeReceiverAccount = findViewById(R.id.tvWaitForJudgeReceiverAccount);
        tvWaitForJudgePayType = findViewById(R.id.tvWaitForJudgePayType);

        String buytype = getIntent().getStringExtra("buytype");
        switch (buytype) {
            case "1":
                tvWaitForJudgePayType.setText(R.string.wechatPay);
                break;
            case "2":
                tvWaitForJudgePayType.setText(R.string.alipay);
                break;
            case "3":
                tvWaitForJudgePayType.setText(R.string.bankcard);
                break;
            default:
        }

        ReleaseSaleResponse data = (ReleaseSaleResponse) getIntent().getSerializableExtra("data");
        tvWaitForJudgeOrderId.setText(data.getBothOrderId());
        tvWaitForJudgeMoney.setText(data.getMoney());
        tvWaitForJudgePrice.setText(getIntent().getStringExtra("rate"));
        tvWaitForJudgeCount.setText(data.getNumber());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        tvWaitForJudgeTime.setText(format.format(new Date(data.getCreateTime())));
        tvWaitForJudgeReceiver.setText(data.getNick());
        tvWaitForJudgeReceiverAccount.setText(data.getReceiptNumber());
    }

    public void back(View view) {
        finish();
    }

}
