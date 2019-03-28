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
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;

import io.reactivex.functions.Consumer;

@SuppressLint("CheckResult")
public class BlockWalletActivity extends BaseActivity {

    private ImageView ivBlockWalletHeadImg;
    private TextView tvBlockWalletAddress;
    private TextView tvBlockWalletNick;
    private TextView tvBlockWalletBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_wallet);

        ivBlockWalletHeadImg = findViewById(R.id.ivBlockWalletHeadImg);
        tvBlockWalletAddress = findViewById(R.id.tvBlockWalletAddress);
        tvBlockWalletNick = findViewById(R.id.tvBlockWalletNick);
        tvBlockWalletBalance = findViewById(R.id.tvBlockWalletBalance);

        GlideUtil.loadCornerImg(ivBlockWalletHeadImg, Constant.currentUser.getHeadPortrait(), 3);
        tvBlockWalletAddress.setText(Constant.currentUser.getWalletAddress());
        tvBlockWalletNick.setText(Constant.currentUser.getNick());
//        tvBlockWalletBalance.setText(Constant.currentUser.get);

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getWallet(Constant.currentUser.getDuoduoId())
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {

                }, this::handleApiError);
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

    }
}
