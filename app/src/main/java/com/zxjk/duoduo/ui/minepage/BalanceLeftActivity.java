package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.AuthenticationActivity;
import com.zxjk.duoduo.utils.CommonUtils;

/**
 * author L
 * create at 2019/5/7
 * description: 余额
 */
@SuppressLint("CheckResult")
public class BalanceLeftActivity extends BaseActivity {

    private TextView tv_authentication; //是否认证tv
    private TextView tv_balance; //余额
    private TextView tv_currency; //币种
    private String otherIdCardType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_left);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.balance));
        tv_balance = findViewById(R.id.tv_balance);
        tv_currency = findViewById(R.id.tv_currency);
        tv_authentication = findViewById(R.id.tv_authentication);

        //返回
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        //账单明细
        findViewById(R.id.rl_billingDetails).setOnClickListener(v ->
                startActivity(new Intent(BalanceLeftActivity.this, DetailListActivity.class)));
        //支付设置
        findViewById(R.id.rl_PaymentSettings).setOnClickListener(v ->
                startActivity(new Intent(BalanceLeftActivity.this, PaySettingActivity.class)));
        //实名认证
        findViewById(R.id.rl_realNameAuthentication).setOnClickListener(v -> {
            if (Constant.currentUser.getIsAuthentication().equals("2")) {
                ToastUtils.showShort(R.string.verifying_pleasewait);
            } else if (Constant.currentUser.getIsAuthentication().equals("0")) {
                ToastUtils.showShort(R.string.authen_true);
            } else {
                NiceDialog.init().setLayoutId(R.layout.layout_general_dialog11).setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        ImageView iv_idCard = holder.getView(R.id.iv_idCard);
                        ImageView iv_passport = holder.getView(R.id.iv_passport);
                        ImageView iv_other = holder.getView(R.id.iv_other);
                        holder.setOnClickListener(R.id.ll_idCard, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                iv_idCard.setImageResource(R.drawable.ic_radio_select);
                                iv_passport.setImageResource(R.drawable.ic_radio_unselect);
                                iv_other.setImageResource(R.drawable.ic_radio_unselect);
                                otherIdCardType = "1";

                            }
                        });
                        holder.setOnClickListener(R.id.ll_passport, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                iv_idCard.setImageResource(R.drawable.ic_radio_unselect);
                                iv_passport.setImageResource(R.drawable.ic_radio_select);
                                iv_other.setImageResource(R.drawable.ic_radio_unselect);
                                otherIdCardType = "2";
                            }
                        });
                        holder.setOnClickListener(R.id.ll_other, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                iv_idCard.setImageResource(R.drawable.ic_radio_unselect);
                                iv_passport.setImageResource(R.drawable.ic_radio_unselect);
                                iv_other.setImageResource(R.drawable.ic_radio_select);
                                otherIdCardType = "3";
                            }
                        });
                        holder.setOnClickListener(R.id.tv_confirm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!TextUtils.isEmpty(otherIdCardType)) {
                                    dialog.dismiss();
                                    if (otherIdCardType.equals("1")) {
                                        Intent intent = new Intent(BalanceLeftActivity.this, AuthenticationActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(BalanceLeftActivity.this, VerifiedActivity.class);
                                        intent.putExtra("otherIdCardType", otherIdCardType);
                                        startActivity(intent);
                                    }
                                } else {
                                    ToastUtils.showShort("请选择证件类型");
                                }
                            }
                        });

                    }
                }).setDimAmount(0.5f).setOutCancel(true).show(getSupportFragmentManager());
            }
        });

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getBalanceHk()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(b -> tv_balance.setText(b.getBalanceHk()), this::handleApiError);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_authentication.setText(CommonUtils.getAuthenticate(Constant.currentUser.getIsAuthentication()));
    }


}
