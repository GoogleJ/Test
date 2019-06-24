package com.zxjk.duoduo.ui.minepage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.ui.msgpage.MyQrCodeActivity;
import com.zxjk.duoduo.ui.walletpage.BlockWalletActivity;
import com.zxjk.duoduo.ui.walletpage.ExchangeActivity;
import com.zxjk.duoduo.ui.walletpage.RecipetQRActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;

/**
 * author L
 * create at 2019/5/7
 * description: 我的
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private ImageView ivMineHeadImg;
    private ImageView ivMineAuthSign;
    private TextView tv_nickName;
    private TextView tvMineSign;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_mine, container, false);
        TextView tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.my));
        RelativeLayout rl_balance = view.findViewById(R.id.rl_balance);
        RelativeLayout rl_blockWallet = view.findViewById(R.id.rl_blockWallet);
        RelativeLayout rl_collectionCode = view.findViewById(R.id.rl_collectionCode);
        RelativeLayout rl_setting = view.findViewById(R.id.rl_setting);
        RelativeLayout rl_exchange = view.findViewById(R.id.rl_exchange);

        CardView cv_Info = view.findViewById(R.id.cv_Info);
        TextView tv_DuoDuoNumber = view.findViewById(R.id.tv_DuoDuoNumber);
        ivMineHeadImg = view.findViewById(R.id.ivMineHeadImg);
        ivMineAuthSign = view.findViewById(R.id.ivMineAuthSign);
        tv_nickName = view.findViewById(R.id.tv_nickName);
        tvMineSign = view.findViewById(R.id.tvMineSign);
        ImageView iv_QRCode = view.findViewById(R.id.iv_QRCode);
        tv_DuoDuoNumber.setText(getString(R.string.duoduo_acount) + Constant.currentUser.getDuoduoId());

        rl_balance.setOnClickListener(this);
        rl_blockWallet.setOnClickListener(this);
        rl_collectionCode.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        cv_Info.setOnClickListener(this);
        iv_QRCode.setOnClickListener(this);
        rl_exchange.setOnClickListener(this);

        if (Constant.isVerifyVerision) {
            rl_blockWallet.setVisibility(View.GONE);
        } else {
            rl_blockWallet.setVisibility(View.VISIBLE);
        }

        //附近的人
        getPermisson(view.findViewById(R.id.rl_nearby), granted -> {
            if (!granted) {
                return;
            }
            AMapLocationClient mLocationClient = new AMapLocationClient(Utils.getApp());
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.setLocationListener(location -> {
                CommonUtils.destoryDialog();
                if (location.getErrorCode() == 0) {
                    Intent intent = new Intent(getContext(), NearByActivity.class);
                    intent.putExtra("location", location);
                    startActivity(intent);
                } else {
                    ToastUtils.showShort(R.string.getlocation_fail);
                }
            });
            CommonUtils.initDialog(getActivity(), getString(R.string.getlocation)).show();
            mLocationClient.startLocation();

        }, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tv_nickName.setText(Constant.currentUser.getNick());
        if (TextUtils.isEmpty(Constant.currentUser.getSignature())) {
            tvMineSign.setText(R.string.none);
        } else {
            tvMineSign.setText(Constant.currentUser.getSignature());
        }
        GlideUtil.loadCornerImg(ivMineHeadImg, Constant.currentUser.getHeadPortrait(), 5);
        String isAuthentication = Constant.currentUser.getIsAuthentication();
        if (isAuthentication.equals("0")) {
            ivMineAuthSign.setVisibility(View.VISIBLE);
        } else {
            ivMineAuthSign.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //二维码
            case R.id.iv_QRCode:
                startActivity(new Intent(getContext(), MyQrCodeActivity.class));
                break;
            //个人信息
            case R.id.cv_Info:
                startActivity(new Intent(getContext(), UserInfoActivity.class));
                break;
            //余额
            case R.id.rl_balance:
                startActivity(new Intent(getContext(), BalanceLeftActivity.class));
                break;
            //多多交易所
            case R.id.rl_exchange:
                startActivity(new Intent(getActivity(), ExchangeActivity.class));
                break;
            //数字钱包
            case R.id.rl_blockWallet:
                startActivity(new Intent(getActivity(), BlockWalletActivity.class));
                break;
            //收款码
            case R.id.rl_collectionCode:
                startActivity(new Intent(getActivity(), RecipetQRActivity.class));
                break;
            //设置
            case R.id.rl_setting:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            default:
        }
    }
}
