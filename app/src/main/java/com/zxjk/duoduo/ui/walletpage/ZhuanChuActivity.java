package com.zxjk.duoduo.ui.walletpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.SelectContactActivity;
import com.zxjk.duoduo.utils.CommonUtils;

import java.text.DecimalFormat;

import androidx.annotation.Nullable;
import io.reactivex.functions.Consumer;


@SuppressLint("CheckResult")
public class ZhuanChuActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    private String type = "1";// 0:eth转账，1：HKB转账

    private SeekBar seekZhuanchu;
    private TextView tvKuanggongPrice;
    private TextView tvGwei;
    private TextView tvZhuanChuCoinType;
    private EditText etWalletAddress;
    private EditText etCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuan_chu);

        seekZhuanchu = findViewById(R.id.seekZhuanchu);
        etCount = findViewById(R.id.etCount);
        etWalletAddress = findViewById(R.id.etWalletAddress);
        tvZhuanChuCoinType = findViewById(R.id.tvZhuanChuCoinType);
        tvKuanggongPrice = findViewById(R.id.tvKuanggongPrice);
        tvGwei = findViewById(R.id.tvGwei);

        seekZhuanchu.setMax(1000);
        seekZhuanchu.setOnSeekBarChangeListener(this);
    }

    //选择联系人
    public void selectContack(View view) {
        Intent intent = new Intent(this, SelectContactActivity.class);
        intent.putExtra("fromZhuanChu", true);
        startActivityForResult(intent, 1);
    }

    //选择币种
    public void chooseCoinType(View view) {
        Intent intent = new Intent(this, ChooseCoinActivity.class);
        intent.putExtra("coin", tvZhuanChuCoinType.getText().toString());
        startActivityForResult(intent, 1);
    }

    public void submit(View view) {
        String walletAddress = etWalletAddress.getText().toString().trim();
        if (TextUtils.isEmpty(walletAddress)) {
            ToastUtils.showShort(R.string.inputwalletaddress);
            return;
        }

        String count = etCount.getText().toString().trim();
        if (TextUtils.isEmpty(count)) {
            ToastUtils.showShort(R.string.inputZhuanChuCount);
            return;
        }
        ServiceFactory.getInstance().getBaseService(Api.class)
                .signTransaction(Constant.currentUser.getPayPwd()
                        , type, Constant.currentUser.getWalletAddress(),
                        walletAddress, tvKuanggongPrice.getText().toString(), count, "", Constant.currentUser.getDuoduoId())
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(s -> {

                }, this::handleApiError);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tvKuanggongPrice.setText("≈" + new DecimalFormat("#.0000").format((progress / 1000f * 0.006)) + " ether");
        tvGwei.setText(new DecimalFormat("#.00").format((progress / 1000f * 100)) + " gwei");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 2) {
            String coin = data.getStringExtra("coin");
            tvZhuanChuCoinType.setText(coin);
            Drawable drawable;
            if (coin.equals("ETH")) {
                type = "0";
                drawable = getDrawable(R.drawable.ic_blockwallet_eth);
                etCount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            } else {
                type = "1";
                etCount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
                drawable = getDrawable(R.drawable.ic_exchange_coins);
            }

            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicWidth());

            tvZhuanChuCoinType.setCompoundDrawables(null, null, drawable, null);
        }

        if (requestCode == 1 && resultCode == 3) {
            String walletAddress = data.getStringExtra("walletAddress");
            etWalletAddress.setText(walletAddress);
        }
    }
}