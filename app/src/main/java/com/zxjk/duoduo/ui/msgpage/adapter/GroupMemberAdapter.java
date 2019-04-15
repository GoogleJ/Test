package com.zxjk.duoduo.ui.msgpage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GroupResponse;
import com.zxjk.duoduo.utils.GlideUtil;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.ViewHolder> {
    private List<GroupResponse.CustomersBean> data = new ArrayList<>();

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setData(List<GroupResponse.CustomersBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onclick(GroupResponse.CustomersBean item, boolean check, int position);
    }

    @NonNull
    @Override
    public GroupMemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_group, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupMemberAdapter.ViewHolder holder, int position) {
        GroupResponse.CustomersBean bean = data.get(position);

        holder.bindData(bean);
        holder.itemView.setOnClickListener(v -> {
            bean.setChecked(!bean.isChecked());
            notifyItemChanged(position);
            if (null != onClickListener) onClickListener.onclick(bean, bean.isChecked(), position);
        });

        if (position != 0) {
            if (data.get(position - 1).getFirstLetter().equals(data.get(position).getFirstLetter())) {
                holder.tvLetter.setVisibility(View.GONE);
            } else {
                holder.tvLetter.setVisibility(View.VISIBLE);
            }
        } else {
            holder.tvLetter.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLetter;
        private ImageView remove_headers;
        private TextView user_name;
        private CheckBox selected_delete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            remove_headers = itemView.findViewById(R.id.remove_headers);
            user_name = itemView.findViewById(R.id.user_name);
            selected_delete = itemView.findViewById(R.id.selected_delete);
            tvLetter = itemView.findViewById(R.id.tvLetter);
        }

        private void bindData(GroupResponse.CustomersBean bean) {
            GlideUtil.loadCornerImg(remove_headers, bean.getHeadPortrait(), 2);
            user_name.setText(bean.getNick());
            selected_delete.setChecked(bean.isChecked());
            tvLetter.setText(bean.getFirstLetter());
        }
    }
}
