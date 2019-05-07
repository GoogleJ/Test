package com.zxjk.duoduo.ui.walletpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
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
    private ImageView iv_wechat, iv_alipay, iv_bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_judge);

        tvWaitForJudgeCountDown = findViewById(R.id.tvWaitForJudgeCountDown);

        tvWaitForJudgeOrderId = findViewById(R.id.tvWaitForJudgeOrderId);
        tvWaitForJudgeMoney = findViewById(R.id.tvWaitForJudgeMoney);
        tvWaitForJudgePrice = findViewById(R.id.tvWaitForJudgePrice);
        tvWaitForJudgeCount = findViewById(R.id.tvWaitForJudgeCount);
        tvWaitForJudgeTime = findViewById(R.id.tvWaitForJudgeTime);
        tvWaitForJudgeReceiver = findViewById(R.id.tvWaitForJudgeReceiver);
        tvWaitForJudgeReceiverAccount = findViewById(R.id.tvWaitForJudgeReceiverAccount);

        iv_wechat = findViewById(R.id.iv_wechat);
        iv_alipay = findViewById(R.id.iv_alipay);
        iv_bank = findViewById(R.id.iv_bank);


        String buytype = getIntent().getStringExtra("buytype");
        switch (buytype) {
            case "1":
                iv_wechat.setVisibility(View.VISIBLE);
                break;
            case "2":
                iv_alipay.setVisibility(View.VISIBLE);
                break;
            case "3":
                iv_bank.setVisibility(View.VISIBLE);
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

        long l1 = (System.currentTimeMillis() - Long.parseLong(data.getPayTime())) / 1000;
        long total = (900 - l1) <= 0 ? 0 : (900 - l1);
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(total)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(l -> {
                    long minute = (total - l) / 60;
                    long second = (total - l) % 60;
                    tvWaitForJudgeCountDown.setText(minute + ":" + (second == 60 ? "00" : ((second < 10 ? ("0" + second) : second))));
                    if (total == 0 || l == total - 1) {
                        ToastUtils.showShort(R.string.timeup);
                        finish();
                    }
                }, t -> {
                });
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
