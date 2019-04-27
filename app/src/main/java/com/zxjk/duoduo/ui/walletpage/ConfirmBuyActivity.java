package com.zxjk.duoduo.ui.walletpage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ReleasePurchase;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.ReleaseSaleResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.TakePopWindow;
import com.zxjk.duoduo.ui.widget.dialog.ConfirmDialog;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.OssUtils;
import com.zxjk.duoduo.utils.TakePicUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

@SuppressLint("CheckResult")
public class ConfirmBuyActivity extends BaseActivity implements TakePopWindow.OnItemClickListener {

    private String pictureUrl = ""; //支付凭证

    private static final int REQUEST_TAKE = 1;
    private static final int REQUEST_ALBUM = 2;

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

    private TakePopWindow selectPicPopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_buy);

        getPermisson(findViewById(R.id.llUploadSign), granted -> {
            KeyboardUtils.hideSoftInput(ConfirmBuyActivity.this);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            selectPicPopWindow.showAtLocation(ConfirmBuyActivity.this.findViewById(android.R.id.content),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        selectPicPopWindow = new TakePopWindow(this);
        selectPicPopWindow.setOnItemClickListener(this);
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

        Observable.interval(0, 1, TimeUnit.SECONDS)
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
        tvConfirmBuyOrderTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.parseLong(data.getCreateTime())));
        tvConfirmBuyMoney.setText(data.getMoney());
    }

    //我已完成支付
    public void confirm(View view) {
        if (TextUtils.isEmpty(pictureUrl)) {
            ToastUtils.showShort(getString(R.string.please_upload_a_payment_voucher));
            return;
        }
        if (dialogConfirm == null) {
            dialogConfirm = new ConfirmDialog(this, getString(R.string.payment_confirmation), getString(R.string.please_confirm_that_you_have_paid_the_seller), callback -> {
                ServiceFactory.getInstance().getBaseService(Api.class)
                        .updateBuyPayState(data.getBothOrderId(), pictureUrl)
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

    public void copyNick(View view) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(tvConfirmBuyReceiver.getText().toString());
        ToastUtils.showShort(getString(R.string.duplicated_to_clipboard));
    }

    public void copyAccount(View view) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(tvConfirmBuyReceiverAccount.getText().toString());
        ToastUtils.showShort(getString(R.string.duplicated_to_clipboard));
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void setOnItemClick(View v) {
        selectPicPopWindow.dismiss();
        switch (v.getId()) {
            case R.id.tvCamera:
                TakePicUtil.takePicture(this, REQUEST_TAKE);
                break;
            case R.id.tvPhoto:
                TakePicUtil.albumPhoto(this, REQUEST_ALBUM);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String filePath = "";

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE:
                    filePath = TakePicUtil.file.getAbsolutePath();
                    break;
                case REQUEST_ALBUM:
                    filePath = TakePicUtil.getPath(this, data.getData());
                    break;
                default:
            }
        }

        if (!TextUtils.isEmpty(filePath)) {
            zipFile(Collections.singletonList(filePath), files -> {
                File file = files.get(0);
                OssUtils.uploadFile(file.getAbsolutePath(), url -> {
                    flagConfirmBuy.setText(R.string.upload_done);
                    ToastUtils.showShort(R.string.upload_done);
                    pictureUrl = url;
                });
            });
        }
    }
}
