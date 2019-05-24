package com.zxjk.duoduo.ui.msgpage.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetDuoBaoIntegralDetailsResponse;
import com.zxjk.duoduo.network.response.GetIntegralDetailsResponse;
import com.zxjk.duoduo.ui.msgpage.GroupGoldStupaInfoActivity;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin.GameRecordPopupWindow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class GameGoldAdapter extends RecyclerView.Adapter<GameGoldAdapter.ViewHolder> {

    private GameRecordPopupWindow gameRecordPopupWindow;
    private List<GetDuoBaoIntegralDetailsResponse> data = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public String groupId = "";

    public void setData(List<GetDuoBaoIntegralDetailsResponse> data) {
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
        if (gameRecordPopupWindow == null) {
            gameRecordPopupWindow = new GameRecordPopupWindow(holder.itemView.getContext());
        }
        holder.bindData(data.get(position));
        holder.itemView.setOnClickListener(v -> {
            GetDuoBaoIntegralDetailsResponse getIntegralDetailsResponse = data.get(holder.getAdapterPosition());
            GetIntegralDetailsResponse getIntegralDetailsResponse1 = new GetIntegralDetailsResponse();
            getIntegralDetailsResponse1.setTime(getIntegralDetailsResponse.getTime());
            getIntegralDetailsResponse1.setIntegral(getIntegralDetailsResponse.getIntegral());
            getIntegralDetailsResponse1.setRemainingIntegral(getIntegralDetailsResponse.getRemainingIntegral());

            gameRecordPopupWindow.show(1, getIntegralDetailsResponse1);
            if (getIntegralDetailsResponse.getTitle().equals("上分")) {
                gameRecordPopupWindow.show(1, getIntegralDetailsResponse1);
            } else if (getIntegralDetailsResponse.getTitle().equals("下分")) {
                gameRecordPopupWindow.show(2, getIntegralDetailsResponse1);
            } else {
                Intent intent = new Intent(holder.itemView.getContext(), GroupGoldStupaInfoActivity.class);
                intent.putExtra("expect", data.get(holder.getAdapterPosition()).getExpect());
                intent.putExtra("groupId", groupId);
                holder.itemView.getContext().startActivity(intent);
            }
        });
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

        void bindData(GetDuoBaoIntegralDetailsResponse bean) {
            tvTitle.setText(bean.getExpect());
            if (bean.getTitle().equals("上分")) {
                iv.setImageResource(R.drawable.ic_game_record_list10);
            }
            if (bean.getTitle().equals("下分")) {
                iv.setImageResource(R.drawable.ic_game_record_list7);
            }

            tvTime.setText(simpleDateFormat.format(Long.valueOf(bean.getTime())));
            if (bean.getType().equals("0")) {
                //转进
                if (bean.getIntegral().contains("-")) {
                    tvHk.setText(bean.getIntegral() + "HK");
                } else {
                    tvHk.setText("+" + bean.getIntegral() + "HK");
                }
                tvHk.setTextColor(ContextCompat.getColor(tvHk.getContext(), R.color.red_eth_in));
            } else if (bean.getType().equals("1")) {
                //转出
                tvHk.setText("-" + bean.getIntegral() + "HK");
                tvHk.setTextColor(ContextCompat.getColor(tvHk.getContext(), R.color.textcolor1));
            } else {
                tvHk.setText("未开奖");
                tvHk.setTextColor(ContextCompat.getColor(tvHk.getContext(), R.color.textcolor1));
            }
            tvLeft.setText("剩余:" + bean.getRemainingIntegral() + "HK");
        }
    }
}
