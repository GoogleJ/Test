package com.zxjk.duoduo.ui.msgpage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetBetInfoDetailsResponse;

import java.util.List;

public class GameRecordDetailAdapter extends RecyclerView.Adapter<GameRecordDetailAdapter.ViewHolder> {

    public List<GetBetInfoDetailsResponse.BetInfoBean.Bean> data;
    public GetBetInfoDetailsResponse.GroupOwnerBean ownerBean;
    public int type;

    private int colorWhite = -1;
    private int colorTheme = -1;
    private int colorGray = -1;
    private int colorRed = -1;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (colorWhite == -1) {
            colorWhite = ContextCompat.getColor(parent.getContext(), R.color.white);
            colorTheme = ContextCompat.getColor(parent.getContext(), R.color.themecolor);
            colorGray = ContextCompat.getColor(parent.getContext(), R.color.white);
            colorRed = ContextCompat.getColor(parent.getContext(), R.color.red_eth_in);
        }
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gamerecord_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0 || position == 1) {
            holder.tv6.setVisibility(View.GONE);
        } else {
            holder.tv6.setVisibility(View.VISIBLE);
        }
        if (position == 0) {
            holder.tv1.setText("庄家");
            holder.tv2.setText("总下注");
            holder.tv3.setText("点数");
            holder.tv4.setText("结果");
            holder.tv5.setText("输赢");
            holder.tv7.setText("庄上积分[保证金]");
            holder.tv8.setText("剩余庄分");

            holder.tv1.setTextColor(colorWhite);
            holder.tv2.setTextColor(colorWhite);
            holder.tv3.setTextColor(colorWhite);
            holder.tv4.setTextColor(colorWhite);
            holder.tv5.setTextColor(colorWhite);
            holder.tv7.setTextColor(colorWhite);
            holder.tv8.setTextColor(colorWhite);

            holder.tv1.setBackgroundColor(colorTheme);
            holder.tv2.setBackgroundColor(colorTheme);
            holder.tv3.setBackgroundColor(colorTheme);
            holder.tv4.setBackgroundColor(colorTheme);
            holder.tv5.setBackgroundColor(colorTheme);
            holder.tv7.setBackgroundColor(colorTheme);
            holder.tv8.setBackgroundColor(colorTheme);
        } else if (position == 1) {
            holder.tv1.setTextColor(colorTheme);
            holder.tv2.setTextColor(colorTheme);
            holder.tv3.setTextColor(colorTheme);
            holder.tv4.setTextColor(colorTheme);
            holder.tv5.setTextColor(colorTheme);
            holder.tv7.setTextColor(colorTheme);
            holder.tv8.setTextColor(colorTheme);

            holder.tv1.setBackgroundColor(colorWhite);
            holder.tv2.setBackgroundColor(colorWhite);
            holder.tv3.setBackgroundColor(colorWhite);
            holder.tv4.setBackgroundColor(colorWhite);
            holder.tv5.setBackgroundColor(colorWhite);
            holder.tv7.setBackgroundColor(colorWhite);
            holder.tv8.setBackgroundColor(colorWhite);

            holder.tv1.setText(ownerBean.getNick());
            holder.tv2.setText(ownerBean.getBetMoney());
            holder.tv3.setText(ownerBean.getPoints());
            holder.tv4.setText(type == 1 ? ownerBean.getNiuniuCardType() : (type == 2 ? ownerBean.getBaijialeCardType() : ownerBean.getDaxiaodxCardType()));
            if (ownerBean.getStatus().equals("1")) {
                holder.tv5.setTextColor(colorTheme);
                holder.tv5.setText("-" + ownerBean.getScorePoints());
            } else {
                holder.tv5.setTextColor(colorRed);
                holder.tv5.setText("+" + ownerBean.getScorePoints());
            }

            holder.tv7.setText(ownerBean.getLastMoney());
            holder.tv8.setText(ownerBean.getThisOverMoney());
        } else if (position == 2) {
            holder.tv1.setTextColor(colorWhite);
            holder.tv2.setTextColor(colorWhite);
            holder.tv3.setTextColor(colorWhite);
            holder.tv4.setTextColor(colorWhite);
            holder.tv5.setTextColor(colorWhite);
            holder.tv6.setTextColor(colorWhite);
            holder.tv7.setTextColor(colorWhite);
            holder.tv8.setTextColor(colorWhite);

            holder.tv1.setBackgroundColor(colorTheme);
            holder.tv2.setBackgroundColor(colorTheme);
            holder.tv3.setBackgroundColor(colorTheme);
            holder.tv4.setBackgroundColor(colorTheme);
            holder.tv5.setBackgroundColor(colorTheme);
            holder.tv6.setBackgroundColor(colorTheme);
            holder.tv7.setBackgroundColor(colorTheme);
            holder.tv8.setBackgroundColor(colorTheme);

            holder.tv1.setText("玩家");
            holder.tv2.setText("下注");
            holder.tv3.setText("点数");
            holder.tv4.setText("结果");
            holder.tv5.setText("输赢");
            holder.tv6.setText("抽水");
            holder.tv7.setText("上局积分");
            holder.tv8.setText("剩余积分");
        } else {
            holder.tv1.setTextColor(colorTheme);
            holder.tv2.setTextColor(colorTheme);
            holder.tv3.setTextColor(colorTheme);
            holder.tv4.setTextColor(colorTheme);
            holder.tv5.setTextColor(colorTheme);
            holder.tv6.setTextColor(colorTheme);
            holder.tv7.setTextColor(colorTheme);
            holder.tv8.setTextColor(colorTheme);

            holder.tv1.setBackgroundColor(colorWhite);
            holder.tv2.setBackgroundColor(colorWhite);
            holder.tv3.setBackgroundColor(colorWhite);
            holder.tv4.setBackgroundColor(colorWhite);
            holder.tv5.setBackgroundColor(colorWhite);
            holder.tv6.setBackgroundColor(colorWhite);
            holder.tv7.setBackgroundColor(colorWhite);
            holder.tv8.setBackgroundColor(colorWhite);

            GetBetInfoDetailsResponse.BetInfoBean.Bean bean = data.get(position - 3);

            holder.tv1.setText(bean.getNick());
            holder.tv2.setText(bean.getBetCardType() + bean.getBetMoney());
            holder.tv3.setText(bean.getPoints());
            holder.tv4.setText(bean.getSettlementCardType());
            if (bean.getStatus().equals("1")) {
                holder.tv5.setTextColor(colorTheme);
                holder.tv5.setText("-" + bean.getScorePoints());
            } else {
                holder.tv5.setTextColor(colorRed);
                holder.tv5.setText("+" + bean.getScorePoints());
            }
            holder.tv6.setText(bean.getPumpingRate());
            holder.tv7.setText(bean.getLastMoney());
            holder.tv8.setText(bean.getThisOverMoney());
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 3;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv1;
        private TextView tv2;
        private TextView tv3;
        private TextView tv4;
        private TextView tv5;
        private TextView tv6;
        private TextView tv7;
        private TextView tv8;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            tv4 = itemView.findViewById(R.id.tv4);
            tv5 = itemView.findViewById(R.id.tv5);
            tv6 = itemView.findViewById(R.id.tv6);
            tv7 = itemView.findViewById(R.id.tv7);
            tv8 = itemView.findViewById(R.id.tv8);
        }
    }
}
