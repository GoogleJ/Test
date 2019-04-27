package com.zxjk.duoduo.ui.walletpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.ReleaseSaleResponse;
import com.zxjk.duoduo.ui.ImgActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 等待审核
 */
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
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(600)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l -> {
                    long minute = (600 - l) / 60;
                    long second = 60 - l % 60;
                    tvWaitForJudgeCountDown.setText(minute + ":" + (second == 60 ? "00" : ((second < 10 ? ("0" + second) : second))));
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
        tvWaitForJudgeTime.setText(format.format(Long.parseLong(data.getCreateTime())));
        tvWaitForJudgeReceiver.setText(data.getNick());
        tvWaitForJudgeReceiverAccount.setText(data.getReceiptNumber());
    }

    public void showQR(View view) {
        ReleaseSaleResponse data = (ReleaseSaleResponse) getIntent().getSerializableExtra("data");
        Intent intent = new Intent(this, ImgActivity.class);
        intent.putExtra("url", data.getReceiptPicture());
        startActivity(intent);
    }

    public void back(View view) {
        finish();
    }

}
