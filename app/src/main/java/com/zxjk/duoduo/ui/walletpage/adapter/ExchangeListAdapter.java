package com.zxjk.duoduo.ui.walletpage.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetOverOrderResponse;

import java.text.SimpleDateFormat;


/**
 * @author Administrator
 */
public class ExchangeListAdapter extends BaseQuickAdapter<GetOverOrderResponse, BaseViewHolder> {

    TextView exchange_list_date;
    TextView exchange_list_time;
    TextView exchange_list_type;
    ImageView exchange_list_icon;
    TextView exchange_list_number;
    TextView exchange_list_number_label;
    TextView exchange_list_currency;
    TextView exchange_list_currency_label;
    TextView exchange_list_money;
    TextView exchange_list_money_label;

    String type;

    public ExchangeListAdapter(String type) {
        super(R.layout.item_exchangelist);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, GetOverOrderResponse item) {

        helper.addOnClickListener(R.id.btn_layout);
        initView(helper);
        initData(item);


    }

    private void initData(GetOverOrderResponse item) {
        exchange_list_date.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.parseLong(item.getCreateTime())));
        exchange_list_currency.setText("HK");
        if ("1".equals(type)) {
            if (Constant.userId.equals(item.getBuyId())) {
                exchange_list_icon.setImageResource(R.drawable.icon_buy);
                if ("0".equals(item.getStatus()) || "1".equals(item.getStatus()) || "2".equals(item.getStatus())) {

                    exchange_list_type.setText(mContext.getString(R.string.exchange_list_successful));
                    exchange_list_number.setText(item.getNumber());
                    exchange_list_money.setText(item.getMoney());
                    return;
                } else if ("3".equals(item.getStatus())) {
                    if ("0".equals(item.getIsBuyPay())) {
                        //等待对方审核
                        exchange_list_type.setText(mContext.getString(R.string.exchange_list_wait_friend));
                        exchange_list_number.setText(item.getNumber());
                        exchange_list_money.setText(item.getMoney());
                    } else {
                        //未付款
                        exchange_list_type.setText(mContext.getString(R.string.exchange_list_not_pay));
                        exchange_list_number.setText(item.getNumber());
                        exchange_list_money.setText(item.getMoney());
                    }
                }
            } else if (Constant.userId.equals(item.getSellId())) {
                exchange_list_icon.setImageResource(R.drawable.icon_sell);
                if ("0".equals(item.getStatus()) || "1".equals(item.getStatus()) || "2".equals(item.getStatus())) {

                    //对方支付中
                    exchange_list_type.setText(mContext.getString(R.string.exchange_list_successful));
                    exchange_list_number.setText(item.getNumber());
                    exchange_list_money.setText(item.getMoney());
                    return;

                } else if ("3".equals(item.getStatus())) {
                    if ("0".equals(item.getIsBuyPay())) {
                        //等待对方审核
                        exchange_list_type.setText(mContext.getString(R.string.exchange_list_wait_review));
                        exchange_list_number.setText(item.getNumber());
                        exchange_list_money.setText(item.getMoney());
                    } else {
                        //未付款
                        exchange_list_type.setText(mContext.getString(R.string.exchange_list_wait));
                        exchange_list_number.setText(item.getNumber());
                        exchange_list_money.setText(item.getMoney());

                    }
                }
            }

        } else if ("0".equals(type)) {
            if (Constant.userId.equals(item.getBuyId())) {
                exchange_list_icon.setImageResource(R.drawable.icon_buy);
                if ("0".equals(item.getStatus()) || "1".equals(item.getStatus()) || "2".equals(item.getStatus())) {

                    exchange_list_type.setText(mContext.getString(R.string.exchange_list_successful));
                    exchange_list_number.setText(item.getNumber());
                    exchange_list_money.setText(item.getMoney());
                    return;
                }
            } else {
                exchange_list_icon.setImageResource(R.drawable.icon_sell);
                if ("0".equals(item.getStatus()) || "1".equals(item.getStatus()) || "2".equals(item.getStatus())) {
                    //对方支付中
                    exchange_list_type.setText(mContext.getString(R.string.exchange_list_successful));
                    exchange_list_number.setText(item.getNumber());
                    exchange_list_money.setText(item.getMoney());
                    return;
                }
            }
        }
    }

    private void initView(BaseViewHolder helper) {
        exchange_list_date = helper.getView(R.id.exchange_list_date);
        exchange_list_time = helper.getView(R.id.exchange_list_time);
        exchange_list_type = helper.getView(R.id.exchange_list_type);
        exchange_list_icon = helper.getView(R.id.exchange_list_icon);
        exchange_list_number = helper.getView(R.id.exchange_list_number);
        exchange_list_number_label = helper.getView(R.id.exchange_list_number_label);
        exchange_list_currency = helper.getView(R.id.exchange_list_currency);
        exchange_list_currency_label = helper.getView(R.id.exchange_list_currency_label);
        exchange_list_money = helper.getView(R.id.exchange_list_money);
        exchange_list_money_label = helper.getView(R.id.exchange_list_money_label);

    }
}
