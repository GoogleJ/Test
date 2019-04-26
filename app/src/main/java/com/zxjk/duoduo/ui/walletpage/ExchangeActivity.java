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
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

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
    private Button btnExchangeConfirm;
    private EditText etExchangeChooseCount;
    private EditText etMinMoney;
    private EditText etMaxMoney;

    private RelativeLayout rlExchangeType1;
    private RelativeLayout rlExchangeType2;
    private RelativeLayout rlExchangeType3;
    private LinearLayout llChooseMinMax;
    private CheckBox cbExchangePayType1;
    private CheckBox cbExchangePayType2;
    private CheckBox cbExchangePayType3;
    private TextView tvExchangePayInfo1;
    private TextView tvExchangePayInfo2;
    private TextView tvExchangePayInfo3;

    private List<String> paytypes = new ArrayList<>(3);
    private String buyType = "";
    private static final String PAYTYPE_WECHAT = "1";
    private static final String PAYTYPE_ALI = "2";
    private static final String PAYTYPE_BANK = "3";

    private boolean buyOrSale = true;

    private int count;
    private float totalPrice;
    private float rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        initViews();

        ServiceFactory.getInstance().getBaseService(Api.class).getNumbeOfTransaction()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .flatMap((Function<GetNumbeOfTransactionResponse, ObservableSource<List<PayInfoResponse>>>) s -> {
                    runOnUiThread(() -> {
                        rate = Float.parseFloat(s.getHkPrice());
                        tvExchangePrice.setText(rate + " CNY=1HK");
                    });
                    return ServiceFactory.getInstance().getBaseService(Api.class).getPayInfo().compose(RxSchedulers.normalTrans());
                })
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(listBaseResponse -> {
                    for (PayInfoResponse res : listBaseResponse) {
                        if (res.getPayType().equals(PAYTYPE_WECHAT)) {
                            tvExchangePayInfo1.setText(res.getWechatNick());
                        } else if (res.getPayType().equals(PAYTYPE_ALI)) {
                            tvExchangePayInfo2.setText(res.getZhifubaoNumber());
                        } else {
                            tvExchangePayInfo3.setText(res.getOpenBank());
                        }
                    }
                }, this::handleApiError);
    }

    private void initViews() {
        selectPopupWindow = new SelectPopupWindow(this, this);
        llChooseMinMax = findViewById(R.id.llChooseMinMax);
        rlExchangeType1 = findViewById(R.id.rlExchangeType1);
        rlExchangeType2 = findViewById(R.id.rlExchangeType2);
        rlExchangeType3 = findViewById(R.id.rlExchangeType3);
        cbExchangePayType1 = findViewById(R.id.cbExchangePayType1);
        cbExchangePayType2 = findViewById(R.id.cbExchangePayType2);
        cbExchangePayType3 = findViewById(R.id.cbExchangePayType3);
        tvExchangePayInfo1 = findViewById(R.id.tvExchangePayInfo1);
        tvExchangePayInfo2 = findViewById(R.id.tvExchangePayInfo2);
        tvExchangePayInfo3 = findViewById(R.id.tvExchangePayInfo3);
        rlExchangeType1.setOnClickListener(this);
        rlExchangeType2.setOnClickListener(this);
        rlExchangeType3.setOnClickListener(this);

        tvExchangePrice = findViewById(R.id.tvExchangePrice);
        tvExchangeTotal = findViewById(R.id.tvExchangeTotal);
        rgExchangeTop = findViewById(R.id.rgExchangeTop);
        tvExchangeLine1 = findViewById(R.id.tvExchangeLine1);
        tvExchangeLine3 = findViewById(R.id.tvExchangeLine3);
        tvExchangeLine4 = findViewById(R.id.tvExchangeLine4);
        tvExchangePayType = findViewById(R.id.tvExchangePayType);
        btnExchangeConfirm = findViewById(R.id.btnExchangeConfirm);
        etExchangeChooseCount = findViewById(R.id.etExchangeChooseCount);
        etMinMoney = findViewById(R.id.etMinMoney);
        etMaxMoney = findViewById(R.id.etMaxMoney);
        etExchangeChooseCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    return;
                }
                count = Integer.parseInt(s.toString());
                totalPrice = count * rate;
                tvExchangeTotal.setText(String.valueOf(totalPrice));
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
        etMaxMoney.addTextChangedListener(new TextWatcher() {
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

        rgExchangeTop.setOnCheckedChangeListener(this);
        rgExchangeTop.check(R.id.rb1);
    }

    public void submit(View view) {
        if (count == 0) {
            ToastUtils.showShort(R.string.select_count_tips);
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
                    .flatMap((Function<String, ObservableSource<ReleaseSaleResponse>>) s -> api.releaseSale(String.valueOf(count), String.valueOf(totalPrice),
                            "1", buyType).compose(RxSchedulers.normalTrans()))
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .subscribe(s -> {
                        Intent intent = new Intent(this, ConfirmBuyActivity.class);
                        intent.putExtra("data", s);
                        intent.putExtra("rate", tvExchangePrice.getText().toString());
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
        count = 0;
        cbExchangePayType1.setChecked(false);
        cbExchangePayType2.setChecked(false);
        cbExchangePayType3.setChecked(false);
        switch (checkedId) {
            case R.id.rb2:
                buyOrSale = false;
                tvExchangeLine1.setText(R.string.exchange1_sale1);
                tvExchangeLine3.setText(R.string.exchange1_sale2);
                tvExchangeLine4.setText(R.string.exchange1_sale3);
                tvExchangePayType.setText(R.string.exchange1_sale4);
                btnExchangeConfirm.setText(R.string.exchange1_sale5);
                llChooseMinMax.setVisibility(View.VISIBLE);

                if (tvExchangePayInfo1.getText().toString().length() != 0) {
                    tvExchangePayInfo1.setVisibility(View.VISIBLE);
                }
                if (tvExchangePayInfo2.getText().toString().length() != 0) {
                    tvExchangePayInfo2.setVisibility(View.VISIBLE);
                }

                if (tvExchangePayInfo3.getText().toString().length() != 0) {
                    tvExchangePayInfo3.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rb1:
                buyOrSale = true;
                tvExchangeLine1.setText(R.string.exchange1_buy1);
                tvExchangeLine3.setText(R.string.exchange1_buy2);
                tvExchangeLine4.setText(R.string.exchange1_buy3);
                tvExchangePayType.setText(R.string.exchange1_buy4);
                btnExchangeConfirm.setText(R.string.exchange1_buy5);
                llChooseMinMax.setVisibility(View.GONE);

                tvExchangePayInfo1.setVisibility(View.GONE);
                tvExchangePayInfo2.setVisibility(View.GONE);
                tvExchangePayInfo3.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (buyOrSale) {
            cbExchangePayType1.setChecked(false);
            cbExchangePayType2.setChecked(false);
            cbExchangePayType3.setChecked(false);
            buyType = v.getId() == R.id.rlExchangeType1 ? PAYTYPE_WECHAT : (v.getId() == R.id.rlExchangeType2 ? PAYTYPE_ALI : PAYTYPE_BANK);

            if (v.getId() == R.id.rlExchangeType1) {
                cbExchangePayType1.setChecked(true);
            } else if (v.getId() == R.id.rlExchangeType2) {
                cbExchangePayType2.setChecked(true);
            } else {
                cbExchangePayType3.setChecked(true);
            }
            return;
        }
        switch (v.getId()) {
            case R.id.rlExchangeType1:
                if (paytypes.contains(PAYTYPE_WECHAT)) {
                    paytypes.remove(PAYTYPE_WECHAT);
                    cbExchangePayType1.setChecked(false);
                } else if (tvExchangePayInfo1.getText().toString().length() != 0) {
                    paytypes.add(PAYTYPE_WECHAT);
                    cbExchangePayType1.setChecked(true);
                }
                break;
            case R.id.rlExchangeType2:
                if (paytypes.contains(PAYTYPE_ALI)) {
                    paytypes.remove(PAYTYPE_ALI);
                    cbExchangePayType2.setChecked(false);
                } else if (tvExchangePayInfo2.getText().toString().length() != 0) {
                    paytypes.add(PAYTYPE_ALI);
                    cbExchangePayType2.setChecked(true);
                }
                break;
            case R.id.rlExchangeType3:
                if (paytypes.contains(PAYTYPE_BANK)) {
                    paytypes.remove(PAYTYPE_BANK);
                    cbExchangePayType3.setChecked(false);
                } else if (tvExchangePayInfo3.getText().toString().length() != 0) {
                    paytypes.add(PAYTYPE_BANK);
                    cbExchangePayType3.setChecked(true);
                }
                break;
            default:
        }
    }

    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (complete) {
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .releasePurchase(String.valueOf(count), String.valueOf(totalPrice),
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
