package com.zxjk.duoduo.ui.walletpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.SendHkbOrHkExchangeService;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.MD5Utils;

import java.text.DecimalFormat;

import io.reactivex.functions.Consumer;

public class HuaZhuanActivity extends BaseActivity {

    private String type = "3"; //币种类型，2：HK->HKB，3：HKB->HK

    private TextView tvHuaZhuan1;
    private TextView tvHuaZhuan2;
    private TextView tvHuaZhuan3;
    private TextView tvHuaZhuan4;
    private TextView tvHuaZhuan5;

    //type==3
    private LinearLayout llHuaZhuanType3;
    private TextView tvHuaZhuanBlock;
    private TextView tvHuaZhuanBlockWallet;
    private TextView tvHuaZhuanReceiveBalance;
    private TextView tvHuaZhuanGasPrice1;
    private TextView tvHuaZhuanGasPrice2;
    private EditText etHuaZhuanCount;
    private SeekBar seekHuaZhuan;

    //type==2
    private LinearLayout llHuaZhuanType2;
    private TextView tvHuaZhuanPoundage;

    private String gasPrice = ""; //矿工费用 type为3时需要传

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hua_zhuan);

        tvHuaZhuan1 = findViewById(R.id.tvHuaZhuan1);
        tvHuaZhuan2 = findViewById(R.id.tvHuaZhuan2);
        tvHuaZhuan3 = findViewById(R.id.tvHuaZhuan3);
        tvHuaZhuan4 = findViewById(R.id.tvHuaZhuan4);
        tvHuaZhuan5 = findViewById(R.id.tvHuaZhuan5);
        llHuaZhuanType3 = findViewById(R.id.llHuaZhuanType3);
        tvHuaZhuanBlock = findViewById(R.id.tvHuaZhuanBlock);
        tvHuaZhuanBlockWallet = findViewById(R.id.tvHuaZhuanBlockWallet);
        tvHuaZhuanReceiveBalance = findViewById(R.id.tvHuaZhuanReceiveBalance);
        tvHuaZhuanGasPrice1 = findViewById(R.id.tvHuaZhuanGasPrice1);
        tvHuaZhuanGasPrice2 = findViewById(R.id.tvHuaZhuanGasPrice2);
        etHuaZhuanCount = findViewById(R.id.etHuaZhuanCount);
        seekHuaZhuan = findViewById(R.id.seekHuaZhuan);
        seekHuaZhuan.setMax(1000);
        llHuaZhuanType2 = findViewById(R.id.llHuaZhuanType2);
        tvHuaZhuanPoundage = findViewById(R.id.tvHuaZhuanPoundage);

        tvHuaZhuanBlock.setText(Constant.walletResponse.getBalanceHkb() + "HKB");
        tvHuaZhuanBlockWallet.setText(Constant.walletResponse.getBalanceHk() + "HK");

        seekHuaZhuan.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gasPrice = new DecimalFormat("#0.00").format((progress / 1000f * 100));
                tvHuaZhuanGasPrice1.setText("≈" + new DecimalFormat("#0.0000").format((progress / 1000f * 0.006)) + " ether");
                tvHuaZhuanGasPrice2.setText(gasPrice + " gwei");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        etHuaZhuanCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvHuaZhuanReceiveBalance.setText(s.toString() + (type.equals("2") ? "HKB" : "HK"));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //币种类型，2：HK->HKB，3：HKB->HK
    public void changeType(View view) {
        if (type.equals("3")) {
            type = "2";
            gasPrice = "";
        } else {
            type = "3";
        }

        etHuaZhuanCount.setText("");

        int compoundDrawablePadding1 = tvHuaZhuan3.getCompoundDrawablePadding();
        Drawable[] compoundDrawables1 = tvHuaZhuan3.getCompoundDrawablesRelative();
        int compoundDrawablePadding2 = tvHuaZhuan4.getCompoundDrawablePadding();
        Drawable[] compoundDrawables2 = tvHuaZhuan4.getCompoundDrawablesRelative();
        tvHuaZhuan3.setCompoundDrawablesRelative(compoundDrawables2[0], compoundDrawables2[1], compoundDrawables2[2], compoundDrawables2[3]);
        tvHuaZhuan3.setCompoundDrawablePadding(compoundDrawablePadding2);
        tvHuaZhuan4.setCompoundDrawablesRelative(compoundDrawables1[0], compoundDrawables1[1], compoundDrawables1[2], compoundDrawables1[3]);
        tvHuaZhuan4.setCompoundDrawablePadding(compoundDrawablePadding1);

        String s3 = tvHuaZhuanBlock.getText().toString();
        String s4 = tvHuaZhuanBlockWallet.getText().toString();
        tvHuaZhuanBlock.setText(s4);
        tvHuaZhuanBlockWallet.setText(s3);

        if (type.equals("2")) {
            llHuaZhuanType3.setVisibility(View.GONE);
            llHuaZhuanType2.setVisibility(View.VISIBLE);
            tvHuaZhuan1.setText(R.string.balance);
            tvHuaZhuan2.setText(R.string.blockWallet);
            tvHuaZhuan4.setText(R.string.blockWallet);
            tvHuaZhuan3.setText(R.string.balance);
            tvHuaZhuan5.setText(R.string.receive);
            tvHuaZhuanReceiveBalance.setText("0HKB");
        } else {
            llHuaZhuanType2.setVisibility(View.GONE);
            llHuaZhuanType3.setVisibility(View.VISIBLE);
            tvHuaZhuan2.setText(R.string.balance);
            tvHuaZhuan4.setText(R.string.balance);
            tvHuaZhuan1.setText(R.string.blockWallet);
            tvHuaZhuan3.setText(R.string.blockWallet);
            tvHuaZhuan5.setText(R.string.receiveBalance);
            tvHuaZhuanReceiveBalance.setText("0HK");
        }
    }

    //全部划转
    public void huazhuanAll(View view) {
        if (type.equals("2")) {
            etHuaZhuanCount.setText(Constant.walletResponse.getBalanceHk());
        } else {
            etHuaZhuanCount.setText(Constant.walletResponse.getBalanceHkb());
        }
    }

    @SuppressLint("CheckResult")
    public void submit(View view) {
        if (etHuaZhuanCount.getText().toString().length() == 0) {
            ToastUtils.showShort(R.string.inputhuazhuancount);
            return;
        }

        if (type.equals("3") && seekHuaZhuan.getProgress() == 0) {
            ToastUtils.showShort(R.string.inputGasPrice);
            return;
        }

        String number;
        number = etHuaZhuanCount.getText().toString().trim();

        number = new DecimalFormat("#0.00").format(Double.valueOf(number).doubleValue());

        String price = tvHuaZhuanGasPrice1.getText().toString().replace("≈", "").replace("ether", "").trim();

        if (type.equals("3")) {
            if (!(Double.valueOf(price) <= Double.valueOf(Constant.walletResponse.getBalanceEth()) && Double.valueOf(number) <= Double.valueOf(Constant.walletResponse.getBalanceHkb()))) {
                ToastUtils.showShort(R.string.wronginput);
                return;
            }
        } else {
            if (!((Double.valueOf(number) + Double.valueOf(price)) <= Double.valueOf(Constant.walletResponse.getBalanceHkb()))) {
                ToastUtils.showShort(R.string.wronginput);
                return;
            }
        }

        String finalNumber = number;
        ServiceFactory.getInstance().getBaseService(Api.class)
                .signHkbOrHkExchange(MD5Utils.getMD5("654321"), type, Constant.walletResponse.getWalletAddress(),
                        gasPrice, number, Constant.walletResponse.getWalletKeystore(), Constant.currentUser.getDuoduoId())
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(s -> {
                    Intent intent = new Intent(this, SendHkbOrHkExchangeService.class);
                    intent.putExtra("arg1", type);
                    intent.putExtra("arg2", finalNumber);
                    intent.putExtra("arg3", s.getTransactionHash());
                    intent.putExtra("arg4", s.getRawTransaction());
                    startService(intent);
                    ToastUtils.showShort(R.string.huazhuansuccess);
                }, this::handleApiError);
    }

    public void cancel(View view) {
        finish();
    }

    public void back(View view) {
        finish();
    }
}
