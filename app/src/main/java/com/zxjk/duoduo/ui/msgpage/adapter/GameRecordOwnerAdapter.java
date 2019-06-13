package com.zxjk.duoduo.ui.msgpage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.response.GetRebatePayRecordResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class GameRecordOwnerAdapter extends RecyclerView.Adapter<GameRecordOwnerAdapter.ViewHolder> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private List<GetRebatePayRecordResponse.ListBean> data = new ArrayList<>();
    public String groupId = "";

    public void setData(List<GetRebatePayRecordResponse.ListBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GameRecordOwnerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gamerecord_owner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GameRecordOwnerAdapter.ViewHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNick;
        TextView tvTime;
        TextView tvMoney;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNick = itemView.findViewById(R.id.tvNick);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvMoney = itemView.findViewById(R.id.tvMoney);
        }

        private void bindData(GetRebatePayRecordResponse.ListBean bean) {
            tvNick.setText(bean.getCustomerNick());
            tvTime.setText(dateFormat.format(Long.parseLong(bean.getCreateTime())));
            tvMoney.setText("-" + bean.getHk() + "HK");
        }
    }
}
