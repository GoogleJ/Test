package com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetGroupGameParameterResponse;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private List<GetGroupGameParameterResponse.ParentListBean.ChildListBean> data;
    private int checkedPosition;

    public void setData(List<GetGroupGameParameterResponse.ParentListBean.ChildListBean> data) {
        checkedPosition = -1;
        this.data = data;
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
        notifyDataSetChanged();
    }

    public int getCheckedPosition() {
        return checkedPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_peilv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(data.get(position));
        if (position == checkedPosition) {
            data.get(position).setChecked(true);
            holder.itemView.setBackgroundResource(R.drawable.bac_game_checked);
            return;
        }
        if (data.get(position).isChecked() && position != checkedPosition) {
            data.get(position).setChecked(false);
            holder.itemView.setBackgroundResource(R.drawable.bac_game_normal);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView beilv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            beilv = itemView.findViewById(R.id.beilv);
            itemView.setOnClickListener(v -> setCheckedPosition(getAdapterPosition()));
        }

        void bindData(GetGroupGameParameterResponse.ParentListBean.ChildListBean bean) {
            name.setText(bean.getPlayName());
            beilv.setText(bean.getMultiple() + "倍");
        }
    }
}
