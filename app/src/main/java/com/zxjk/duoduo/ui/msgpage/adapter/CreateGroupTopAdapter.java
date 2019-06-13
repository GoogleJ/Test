package com.zxjk.duoduo.ui.msgpage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.response.FriendInfoResponse;
import com.zxjk.duoduo.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CreateGroupTopAdapter extends RecyclerView.Adapter<CreateGroupTopAdapter.ViewHolder> {

    private List<FriendInfoResponse> data = new ArrayList<>();

    public void setData(List<FriendInfoResponse> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GlideUtil.loadCornerImg((holder.item_header), data.get(position).getHeadPortrait(), 5);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_header;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_header = itemView.findViewById(R.id.item_header);
        }
    }
}
