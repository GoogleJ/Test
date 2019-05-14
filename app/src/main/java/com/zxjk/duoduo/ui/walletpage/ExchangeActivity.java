package com.zxjk.duoduo.ui.walletpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GetNumbeOfTransactionResponse;
import com.zxjk.duoduo.network.response.PayInfoResponse;
import com.zxjk.duoduo.network.response.ReleaseSaleResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.dialog.SelectPopupWindow;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.MD5Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * author L
 * create at 2019/5/7
 * description: 多多交易所
 */
@SuppressLint("CheckResult")
public class ExchangeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, SelectPopupWindow.OnPopWindowClickListener {

    private SelectPopupWindow selectPopupWindow;

    private TextView tvExchangePrice;
    private TextView tvExchangeTotal;
    private RadioGroup rgExchangeTop;
    private TextView tvExchangeLine1;
    private TextView tvExchangeLine3;
    private TextView tvExchangeLine4;
    private TextView tvExchangePayType;
    private TextView tv_confirm;
    private EditText etExchangeChooseCount;
    private EditText etMinMoney;
    private EditText etMaxMoney;

    private RelativeLayout rl_weChat;
    private RelativeLayout rl_aliPay;
    private RelativeLayout rl_bank;
    private LinearLayout llChooseMinMax;
    private CheckBox checkbox_weChat;
    private CheckBox checkbox_aliPay;
    private CheckBox checkbox_bank;
    private TextView tv_weChatInfo;
    private TextView tv_aliPayInfo;
    private TextView tv_bankInfo;
    private TextView tvTipsExchange;

    private List<String> paytypes = new ArrayList<>(3);
    private String buyType = "";
    private static final String PAYTYPE_WECHAT = "1";
    private static final String PAYTYPE_ALI = "2";
    private static final String PAYTYPE_BANK = "3";

    private boolean buyOrSale = true;

    private String totalPrice;

    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        initViews();

        ServiceFactory.getInstance().getBaseService(Api.class).getNumbeOfTransaction()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .flatMap((Function<GetNumbeOfTransactionResponse, ObservableSource<List<PayInfoResponse>>>) s -> {
                    runOnUiThread(() -> tvExchangePrice.setText(s.getHkPrice() + " CNY=1HK"));

                    return ServiceFactory.getInstance().getBaseService(Api.class)
                            .getPayInfo().compose(RxSchedulers.normalTrans());
                })
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(listBaseResponse -> {
                    for (PayInfoResponse res : listBaseResponse) {
                        if (res.getPayType().equals(PAYTYPE_WECHAT)) {
                            tv_weChatInfo.setText(res.getWechatNick());
                        } else if (res.getPayType().equals(PAYTYPE_ALI)) {
                            tv_aliPayInfo.setText(res.getZhifubaoNumber());
                        } else {
                            tv_bankInfo.setText(res.getOpenBank());
                        }
                    }
                }, this::handleApiError);
    }

    private void initViews() {
        selectPopupWindow = new SelectPopupWindow(this, this);
        llChooseMinMax = findViewById(R.id.llChooseMinMax);
        rl_weChat = findViewById(R.id.rl_weChat);
        rl_aliPay = findViewById(R.id.rl_aliPay);
        rl_bank = findViewById(R.id.rl_bank);
        checkbox_weChat = findViewById(R.id.checkbox_weChat);
        checkbox_aliPay = findViewById(R.id.checkbox_aliPay);
        checkbox_bank = findViewById(R.id.checkbox_bank);
        tv_weChatInfo = findViewById(R.id.tv_weChatInfo);
        tv_aliPayInfo = findViewById(R.id.tv_aliPayInfo);
        tv_bankInfo = findViewById(R.id.tv_bankInfo);
        tvTipsExchange = findViewById(R.id.tvTipsExchange);
        rl_weChat.setOnClickListener(this);
        rl_aliPay.setOnClickListener(this);
        rl_bank.setOnClickListener(this);

        tvExchangePrice = findViewById(R.id.tvExchangePrice);
        tvExchangeTotal = findViewById(R.id.tvExchangeTotal);
        rgExchangeTop = findViewById(R.id.rgExchangeTop);
        tvExchangeLine1 = findViewById(R.id.tvExchangeLine1);
        tvExchangeLine3 = findViewById(R.id.tvExchangeLine3);
        tvExchangeLine4 = findViewById(R.id.tvExchangeLine4);
        tvExchangePayType = findViewById(R.id.tvExchangePayType);
        tv_confirm = findViewById(R.id.tv_confirm);
        etExchangeChooseCount = findViewById(R.id.etExchangeChooseCount);
        etMinMoney = findViewById(R.id.etMinMoney);
        etMaxMoney = findViewById(R.id.etMaxMoney);
        etMaxMoney.setEnabled(false);
        etExchangeChooseCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    totalPrice = decimalFormat.format(CommonUtils.mul(Double.parseDouble(tvExchangePrice.getText().toString().split(" ")[0]), Double.parseDouble(s.toString())));
                } else {
                    totalPrice = "0";
                }
                tvExchangeTotal.setText(totalPrice);
                etMaxMoney.setText(s.toString());

            }
        });
        etMinMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    return;
                }
                if (!TextUtils.isEmpty(etExchangeChooseCount.getText().toString().trim()) &&
                        Integer.parseInt(s.toString()) > Integer.parseInt(etExchangeChooseCount.getText().toString().trim())) {
                    ToastUtils.showShort(R.string.input_outrate);
                }
            }
        });
//        etMaxMoney.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.toString().length() == 0) {
//                    return;
//                }
//                if (!TextUtils.isEmpty(etExchangeChooseCount.getText().toString().trim()) &&
//                        Integer.parseInt(s.toString()) > Integer.parseInt(etExchangeChooseCount.getText().toString().trim())) {
//                    ToastUtils.showShort(R.string.input_outrate);
//                }
//            }
//        });

        rgExchangeTop.setOnCheckedChangeListener(this);
        rgExchangeTop.check(R.id.rb1);
    }

    public void submit(View view) {
        if (TextUtils.isEmpty(etExchangeChooseCount.getText().toString())) {
            ToastUtils.showShort(getString(R.string.input_max_number));
            return;
        }

        if (Integer.parseInt(etExchangeChooseCount.getText().toString()) < 50) {
            ToastUtils.showShort(getString(R.string.number_max));
            return;
        }

        if (TextUtils.isEmpty(buyType) && buyOrSale) {
            ToastUtils.showShort(R.string.select_buytype_tips);
            return;
        }
        if (paytypes.size() == 0 && !buyOrSale) {
            ToastUtils.showShort(R.string.select_saletype_tips);
            return;
        }
        Api api = ServiceFactory.getInstance().getBaseService(Api.class);

        if (buyOrSale) {
            api.isConfine().compose(bindToLifecycle())
                    .compose(RxSchedulers.normalTrans())
                    .flatMap((Function<String, ObservableSource<ReleaseSaleResponse>>) s -> api.releaseSale(etExchangeChooseCount.getText().toString(), totalPrice,
                            "1", buyType).compose(RxSchedulers.normalTrans()))
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .subscribe(s -> {
                        //购买
                        Intent intent = new Intent(this, ConfirmBuyActivity.class);
                        if (buyType.equals(PAYTYPE_WECHAT)) {
                            s.setReceiptNumber(s.getWechatNick());
                        }
                        intent.putExtra("data", s);
                        s.setCreateTime(String.valueOf(System.currentTimeMillis()));
                        intent.putExtra("rate", tvExchangePrice.getText().toString().split(" ")[0]);
                        intent.putExtra("buytype", buyType);
                        startActivity(intent);
                    }, this::handleApiError);
        } else {
            KeyboardUtils.hideSoftInput(this);
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int winHeight = getWindow().getDecorView().getHeight();
            selectPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
        }
    }

    private String getPayTypes() {
        StringBuilder builder = new StringBuilder();
        for (String str : paytypes) {
            builder.append(str + ",");
        }
        return builder.substring(0, builder.length() - 1);
    }

    public void jump2List(View view) {
        Intent intent = new Intent(this, ExchangeListActivity.class);
        intent.putExtra("rate", tvExchangePrice.getText().toString());
        startActivity(intent);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        buyType = "";
        paytypes.clear();
        checkbox_weChat.setChecked(false);
        checkbox_aliPay.setChecked(false);
        checkbox_bank.setChecked(false);
        switch (checkedId) {
            case R.id.rb2:
                buyOrSale = false;
                tvExchangeLine1.setText(R.string.exchange1_sale1);
                tvExchangeLine3.setText(R.string.exchange1_sale2);
                tvExchangeLine4.setText(R.string.exchange1_sale3);
                tvExchangePayType.setText(R.string.exchange1_sale4);
                tv_confirm.setText(R.string.exchange1_sale5);
                llChooseMinMax.setVisibility(View.VISIBLE);
                tvTipsExchange.setVisibility(View.VISIBLE);

                tv_weChatInfo.setVisibility(View.VISIBLE);
                tv_aliPayInfo.setVisibility(View.VISIBLE);
                tv_bankInfo.setVisibility(View.VISIBLE);

                break;
            case R.id.rb1:
                buyOrSale = true;
                tvExchangeLine1.setText(R.string.exchange1_buy1);
                tvExchangeLine3.setText(R.string.exchange1_buy2);
                tvExchangeLine4.setText(R.string.exchange1_buy3);
                tvExchangePayType.setText(R.string.exchange1_buy4);
                tv_confirm.setText(R.string.exchange1_buy5);
                llChooseMinMax.setVisibility(View.GONE);
                tvTipsExchange.setVisibility(View.GONE);

                tv_weChatInfo.setVisibility(View.GONE);
                tv_aliPayInfo.setVisibility(View.GONE);
                tv_bankInfo.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (buyOrSale) {
            checkbox_weChat.setChecked(false);
            checkbox_aliPay.setChecked(false);
            checkbox_bank.setChecked(false);
            buyType = v.getId() == R.id.rl_weChat ? PAYTYPE_WECHAT : (v.getId() == R.id.rl_aliPay ? PAYTYPE_ALI : PAYTYPE_BANK);
            if (v.getId() == R.id.rl_weChat) {
                checkbox_weChat.setChecked(true);
            } else if (v.getId() == R.id.rl_aliPay) {
                checkbox_aliPay.setChecked(true);
            } else {
                checkbox_bank.setChecked(true);
            }
            return;
        }
        switch (v.getId()) {
            case R.id.rl_weChat:
                if (paytypes.contains(PAYTYPE_WECHAT)) {
                    paytypes.remove(PAYTYPE_WECHAT);
                    checkbox_weChat.setChecked(false);
                } else if (!tv_weChatInfo.getText().toString().equals(getString(R.string.not_perfect))) {
                    paytypes.add(PAYTYPE_WECHAT);
                    checkbox_weChat.setChecked(true);
                }
                break;
            case R.id.rl_aliPay:
                if (paytypes.contains(PAYTYPE_ALI)) {
                    paytypes.remove(PAYTYPE_ALI);
                    checkbox_aliPay.setChecked(false);
                } else if (!tv_aliPayInfo.getText().toString().equals(getString(R.string.not_perfect))) {
                    paytypes.add(PAYTYPE_ALI);
                    checkbox_aliPay.setChecked(true);
                }
                break;
            case R.id.rl_bank:
                if (paytypes.contains(PAYTYPE_BANK)) {
                    paytypes.remove(PAYTYPE_BANK);
                    checkbox_bank.setChecked(false);
                } else if (!tv_bankInfo.getText().toString().equals(getString(R.string.not_perfect))) {
                    paytypes.add(PAYTYPE_BANK);
                    checkbox_bank.setChecked(true);
                }
                break;
            default:
        }
    }

    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (complete) {
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .releasePurchase(etExchangeChooseCount.getText().toString(), String.valueOf(totalPrice),
                            "1", MD5Utils.getMD5(psw), getPayTypes(), etMinMoney.getText().toString().trim(), etMaxMoney.getText().toString().trim())
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .subscribe(s -> {
                        Intent intent = new Intent(this, ConfirmSaleActivity.class);
                        intent.putExtra("data", s);
                        intent.putExtra("rate", tvExchangePrice.getText().toString());
                        startActivity(intent);
                    }, this::handleApiError);
        }
    }
}
