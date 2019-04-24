package com.zxjk.duoduo.ui.msgpage.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetIntegralDetailsResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class GameRecordAdapter extends RecyclerView.Adapter<GameRecordAdapter.ViewHolder> {

    private List<GetIntegralDetailsResponse> data = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void setData(List<GetIntegralDetailsResponse> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_record, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvTime;
        private TextView tvHk;
        private TextView tvLeft;
        private ImageView iv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvHk = itemView.findViewById(R.id.tvHk);
            tvLeft = itemView.findViewById(R.id.tvLeft);
            iv = itemView.findViewById(R.id.iv);
        }

        void bindData(GetIntegralDetailsResponse bean) {
            if (bean.getTitle().equals("上分")) {
                iv.setImageResource(R.drawable.ic_game_record_list10);
            }
            if (bean.getTitle().equals("下分")) {
                iv.setImageResource(R.drawable.ic_game_record_list7);
            }
            if (bean.getTitle().equals("牛牛")) {
                iv.setImageResource(R.drawable.ic_game_record_list3);
            }
            if (bean.getTitle().equals("百家乐")) {
                iv.setImageResource(R.drawable.ic_game_record_list1);
            }
            if (bean.getTitle().equals("大小单")) {
                iv.setImageResource(R.drawable.ic_game_record_list6);
            }


            if (TextUtils.isEmpty(bean.getSettlementCardType())) {
                tvTitle.setText(bean.getTitle());
            } else {
                tvTitle.setText(bean.getTitle() + "-" + bean.getSettlementCardType());
            }
            tvTime.setText(simpleDateFormat.format(Long.valueOf(bean.getTime())));
            if (bean.getType().equals("0")) {
                //转进
                tvHk.setText("+" + bean.getIntegral() + "HK");
                tvHk.setTextColor(ContextCompat.getColor(tvHk.getContext(), R.color.red_eth_in));
            } else {
                //转出
                tvHk.setText("-" + bean.getIntegral() + "HK");
                tvHk.setTextColor(ContextCompat.getColor(tvHk.getContext(), R.color.textcolor1));
            }
            tvLeft.setText("剩余:" + bean.getRemainingIntegral() + "HK");
        }
    }
}
