package com.zxjk.duoduo.ui.msgpage.adapter;

import android.content.Intent;
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
import com.zxjk.duoduo.ui.msgpage.GameRecordDetailActivity;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin.GameRecordPopupWindow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class GameRecordAdapter extends RecyclerView.Adapter<GameRecordAdapter.ViewHolder> {

    private GameRecordPopupWindow gameRecordPopupWindow;
    private List<GetIntegralDetailsResponse> data = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public String groupId = "";

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
        if (gameRecordPopupWindow == null) {
            gameRecordPopupWindow = new GameRecordPopupWindow(holder.itemView.getContext());
        }
        holder.bindData(data.get(position));
        holder.itemView.setOnClickListener(v -> {
            GetIntegralDetailsResponse getIntegralDetailsResponse = data.get(holder.getAdapterPosition());
            if (getIntegralDetailsResponse.getTitle().equals("流局") || getIntegralDetailsResponse.getSettlementCardType().equals("流局")) {
                gameRecordPopupWindow.show(3,data.get(holder.getAdapterPosition()));
            }
            if (getIntegralDetailsResponse.equals("上分")) {
                gameRecordPopupWindow.show(1,data.get(holder.getAdapterPosition()));
            }
            if (getIntegralDetailsResponse.equals("下分")) {
                gameRecordPopupWindow.show(2,data.get(holder.getAdapterPosition()));
            }

            String redPackageId = getIntegralDetailsResponse.getRedPackageId();
            Intent intent = new Intent(holder.itemView.getContext(), GameRecordDetailActivity.class);
            intent.putExtra("redPackageId", redPackageId);
            intent.putExtra("groupId", groupId);
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

        void bindData(GetIntegralDetailsResponse bean) {
            if ("上分".equals(bean.getTitle())) {
                iv.setImageResource(R.drawable.ic_game_record_list10);
            }
            if ("下分".equals(bean.getTitle())) {
                iv.setImageResource(R.drawable.ic_game_record_list7);
            }
            if ("牛牛".equals(bean.getTitle())) {
                iv.setImageResource(R.drawable.ic_game_record_list3);
            }
            if ("百家乐".equals(bean.getTitle())) {
                iv.setImageResource(R.drawable.ic_game_record_list1);
            }
            if ("大小单双合".equals(bean.getTitle())) {
                iv.setImageResource(R.drawable.ic_game_record_list6);
            }
            if ("流局".equals(bean.getTitle()) || "流局".equals(bean.getSettlementCardType())) {
                iv.setImageResource(R.drawable.ic_game_record_list4);
            }

            if (TextUtils.isEmpty(bean.getSettlementCardType())) {
                tvTitle.setText(bean.getTitle());
            } else {
                if (bean.getTitle().equals("流局")) {
                    tvTitle.setText(bean.getSettlementCardType() + "-" + bean.getTitle());
                } else {
                    tvTitle.setText(bean.getTitle() + "-" + bean.getSettlementCardType());
                }
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
