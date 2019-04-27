package com.zxjk.duoduo.ui.walletpage.adapter;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetOverOrderResponse;
import com.zxjk.duoduo.utils.CommonUtils;

import java.text.SimpleDateFormat;


/**
 * @author Administrator
 */
public class ExchangeListAdapter extends BaseQuickAdapter<GetOverOrderResponse, BaseViewHolder> {

    public ExchangeListAdapter() {
        super(R.layout.item_exchangelist);
    }

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

    @Override
    protected void convert(BaseViewHolder helper, GetOverOrderResponse item) {
        TextView exchange_list_date = helper.getView(R.id.exchange_list_date);
        TextView exchange_list_time = helper.getView(R.id.exchange_list_time);
        TextView exchange_list_type = helper.getView(R.id.exchange_list_type);
        ImageView exchange_list_icon = helper.getView(R.id.exchange_list_icon);
        TextView exchange_list_number = helper.getView(R.id.exchange_list_number);
        TextView exchange_list_currency = helper.getView(R.id.exchange_list_currency);
        TextView exchange_list_money = helper.getView(R.id.exchange_list_money);

        exchange_list_date.setText(CommonUtils.timeStamp2Date(item.getCreateTime()));
        exchange_list_currency.setText("HK");
        exchange_list_money.setText(item.getMoney() + " CNY");
        exchange_list_number.setText(item.getNumber());
        exchange_list_type.setTextColor(Color.parseColor("#000000"));

        if (item.getBuyId().equals(Constant.userId)) {
            //买方
            exchange_list_icon.setImageResource(R.drawable.icon_buy);

            if (item.getIsBuyPay().equals("1")) {
                exchange_list_type.setTextColor(Color.parseColor("#EA2222"));
                exchange_list_type.setText(R.string.exchange_list_not_pay);
            } else if (item.getIsBuyPay().equals("0")) {
                exchange_list_type.setTextColor(Color.parseColor("#EA2222"));
                exchange_list_type.setText(R.string.wait_for_others_to_judge);
            }
        } else {
            //卖方
            exchange_list_icon.setImageResource(R.drawable.icon_sell);

            if (item.getBuyNick().equals("")) {
                exchange_list_type.setText(R.string.guadan);
            }
            if (item.getIsBuyPay().equals("1")) {
                exchange_list_type.setText(R.string.wait_for_others_to_pay);
            } else if (item.getIsDelete().equals("0")) {
                exchange_list_type.setText(R.string.sale_waitforjudge);
            }
        }

        if (item.getStatus().equals("3")) {
            //交易中
            long l = 15 * 1000 * 60 - System.currentTimeMillis() + Long.parseLong(item.getCreateTime());
            if (l > 0) {
                String timeLeft = simpleDateFormat.format(l);
                exchange_list_time.setText(timeLeft);
            } else {
                exchange_list_time.setText("");
            }
        } else {
            exchange_list_time.setText("");
        }

        if (item.getStatus().equals("2")) {
            exchange_list_type.setText(R.string.tradeFailed);
        }

        if (item.getStatus().equals("0")) {
            exchange_list_type.setText(R.string.tradeDone1);
        }

        if (item.getStatus().equals("1")) {
            exchange_list_type.setText(R.string.tradeCancel);
        }
    }

}
