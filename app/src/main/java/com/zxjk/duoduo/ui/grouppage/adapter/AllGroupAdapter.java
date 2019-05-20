package com.zxjk.duoduo.ui.grouppage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.othershe.combinebitmap.CombineBitmap;
import com.othershe.combinebitmap.layout.WechatLayoutManager;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetAllPlayGroupResponse;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Arrays;
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


        String[] split = data.get(position).getHeadPortrait().split(",");
        if (split.length > 9) {
            List<String> strings = Arrays.asList(split);
            List<String> strings1 = strings.subList(0, 9);
            split = new String[strings1.size()];
            for (int i = 0; i < strings1.size(); i++) {
                split[i] = strings1.get(i);
            }
        }

        CombineBitmap.init(holder.itemView.getContext())
                .setLayoutManager(new WechatLayoutManager()) // 必选， 设置图片的组合形式，支持WechatLayoutManager、DingLayoutManager
                .setGapColor(holder.itemView.getContext().getResources().getColor(R.color.grey)) // 单个图片间距的颜色，默认白色
                .setSize(CommonUtils.dip2px(holder.itemView.getContext(), 56)) // 必选，组合后Bitmap的尺寸，单位dp
                .setGap(CommonUtils.dip2px(holder.itemView.getContext(), 2)) // 单个Bitmap之间的距离，单位dp，默认0dp
                .setUrls(split) // 要加载的图片url数组
                .setImageView(holder.nineImg) // 直接设置要显示图片的ImageView
                .build();
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
