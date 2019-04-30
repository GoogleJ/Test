package com.zxjk.duoduo.ui.minepage.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.DetailListResposne;
import com.zxjk.duoduo.ui.minepage.DetailInfoActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.ViewHolder> {

    private List<DetailListResposne> data = new ArrayList<>();

    public void setData(List<DetailListResposne> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detaillist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(data.get(position));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailInfoActivity.class);
            intent.putExtra("data", data.get(position));
            intent.putExtra("type", holder.tvItemDetaillistName.getText());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivItemDetaillist;
        private TextView tvItemDetaillistName;
        private TextView tvItemDetaillistTime;
        private TextView tvItemDetaillistMoney;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivItemDetaillist = itemView.findViewById(R.id.ivItemDetaillist);
            tvItemDetaillistName = itemView.findViewById(R.id.tvItemDetaillistName);
            tvItemDetaillistTime = itemView.findViewById(R.id.tvItemDetaillistTime);
            tvItemDetaillistMoney = itemView.findViewById(R.id.tvItemDetaillistMoney);
        }

        private void bindData(DetailListResposne bean) {
            tvItemDetaillistTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.parseLong(bean.getCreateTime())));
            tvItemDetaillistMoney.setText(bean.getHk());
            if (bean.getSource() == 0 || bean.getSource() == 2) {
                ivItemDetaillist.setImageResource(R.drawable.ic_detail_item_type1);
                tvItemDetaillistName.setText(R.string.red_packet);
            } else if (bean.getSource() == 1) {
                ivItemDetaillist.setImageResource(R.drawable.ic_detail_item_type2);
                tvItemDetaillistName.setText(R.string.transfer);
            } else if (bean.getSource() == 8 || bean.getSource() == 9 || bean.getSource() == -9) {
                ivItemDetaillist.setImageResource(R.drawable.ic_detail_item_type3);
                tvItemDetaillistName.setText(R.string.huazhuan);
            } else if (bean.getSource() == 4) {
                ivItemDetaillist.setImageResource(R.drawable.ic_detail_item_type4);
                tvItemDetaillistName.setText(R.string.chushou);
            } else if (bean.getSource() == 5) {
                ivItemDetaillist.setImageResource(R.drawable.ic_detail_item_type6);
                tvItemDetaillistName.setText(R.string.dingdanchexiao);
            } else {
                ivItemDetaillist.setImageResource(R.drawable.ic_detail_item_type5);
                tvItemDetaillistName.setText(R.string.others);
            }
        }
    }
}
