package com.zxjk.duoduo.ui.walletpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.CreateWalletResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;

import java.text.DecimalFormat;

@SuppressLint("CheckResult")
public class BlockWalletActivity extends BaseActivity {

    private ImageView ivBlockWalletHeadImg;
    private TextView tvBlockWalletAddress;
    private TextView tvBlockWalletNick;
    private TextView tvBlockWalletBalance;
    private TextView tvBlockWalletETH1;
    private TextView tvBlockWalletETH2;

    private String ETHmoney1;
    private String ETHmoney2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_wallet);

        ivBlockWalletHeadImg = findViewById(R.id.ivBlockWalletHeadImg);
        tvBlockWalletAddress = findViewById(R.id.tvBlockWalletAddress);
        tvBlockWalletNick = findViewById(R.id.tvBlockWalletNick);
        tvBlockWalletBalance = findViewById(R.id.tvBlockWalletBalance);
        tvBlockWalletETH1 = findViewById(R.id.tvBlockWalletETH1);
        tvBlockWalletETH2 = findViewById(R.id.tvBlockWalletETH2);

        GlideUtil.loadCornerImg(ivBlockWalletHeadImg, Constant.currentUser.getHeadPortrait(), 3);
        tvBlockWalletAddress.setText(Constant.currentUser.getWalletAddress());
        tvBlockWalletNick.setText(Constant.currentUser.getNick());

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getWallet(Constant.currentUser.getDuoduoId())
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(response -> {
                    ETHmoney1 = response.getBalanceEth() + "ETH";
                    ETHmoney2 = "≈￥ " + new DecimalFormat("#0.00").format(response.getExchange());
                    tvBlockWalletETH1.setText(ETHmoney1);
                    tvBlockWalletETH2.setText(ETHmoney2);
                    tvBlockWalletBalance.setText(response.getBalanceHkb());
                    Constant.walletResponse = response;
                }, this::handleApiError);
    }

    public void enterETHOrders(View view) {
        Intent intent = new Intent(this, BlockETHOrders.class);
        intent.putExtra("money1", ETHmoney1);
        intent.putExtra("money2", ETHmoney2);
        startActivity(intent);
    }

    public void zhuanchu(View view) {
        startActivity(new Intent(this, ZhuanChuActivity.class));
    }

    public void huazhuan(View view) {
        startActivity(new Intent(this, HuaZhuanActivity.class));
    }

    public void back(View view) {
        finish();
    }

    public void jump2List(View view) {
        startActivity(new Intent(this, BlockWalletOrdersActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constant.walletResponse = new CreateWalletResponse();
    }
}
