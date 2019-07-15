package com.zxjk.duoduo.ui.grouppage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.response.GetAllPlayGroupResponse;
import com.zxjk.duoduo.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

public class AllGroupAdapter extends RecyclerView.Adapter<AllGroupAdapter.ViewHolder> {

    private OnItemClickLitener mOnItemClickLitener;

    //设置回调接口
    public interface OnItemClickLitener {
        void onItemClick(int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private List<GetAllPlayGroupResponse.GroupListBean> data = new ArrayList<>();

    public void setData(List<GetAllPlayGroupResponse.GroupListBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_allplaygroup, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.tvType.setVisibility(View.VISIBLE);
        } else if (data.get(position).isHasJoined() == data.get(position - 1).isHasJoined()) {
            holder.tvType.setVisibility(View.GONE);
        } else {
            holder.tvType.setVisibility(View.VISIBLE);
        }
        holder.tvGroupName.setText(data.get(position).getGroupNikeName());
        holder.tvType.setText(data.get(position).isHasJoined() ? R.string.mygamegroup : R.string.allgamegroup);

        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(view -> {
                GetAllPlayGroupResponse.GroupListBean groupListBean = data.get(holder.getAdapterPosition());
                if (groupListBean.isHasJoined()) {
                    RongIM.getInstance().startGroupChat(holder.itemView.getContext(), groupListBean.getId(), groupListBean.getGroupNikeName());
                } else {
                    mOnItemClickLitener.onItemClick(holder.getAdapterPosition());
                }
            });
        }

        ImageUtil.loadGroupPortrait(holder.nineImg, data.get(position).getHeadPortrait(), 56, 2);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvGroupName;
        private TextView tvType;
        private ImageView nineImg;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            tvType = itemView.findViewById(R.id.tvType);
            nineImg = itemView.findViewById(R.id.nineImg);
        }
    }

}
