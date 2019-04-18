package com.zxjk.duoduo.ui.msgpage.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetRedPackageRecordResponse;
import com.zxjk.duoduo.ui.msgpage.PeopleUnaccalimedActivity;
import com.zxjk.duoduo.utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

public class RedPackageRecoderAdapter extends RecyclerView.Adapter<RedPackageRecoderAdapter.ViewHolder> {

    private Context context;
    private List<GetRedPackageRecordResponse.RedpackageListBean> data;

    public RedPackageRecoderAdapter(List<GetRedPackageRecordResponse.RedpackageListBean> data) {
        this.data = data;
    }

    public void setData(List<GetRedPackageRecordResponse.RedpackageListBean> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_red_packet_record, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetRedPackageRecordResponse.RedpackageListBean redpackageListBean = data.get(position);
        holder.bindData(redpackageListBean);
        holder.itemView.setOnClickListener(v -> {
            Intent intent1 = new Intent(context, PeopleUnaccalimedActivity.class);
            intent1.putExtra("id", String.valueOf(data.get(holder.getAdapterPosition()).getRedPackageId()));
            intent1.putExtra("fromList", true);
            startActivity(intent1);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRedPackageNick;
        TextView tvRedPackageTime;
        TextView tvRedPackageMoney;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRedPackageNick = itemView.findViewById(R.id.tvRedPackageNick);
            tvRedPackageTime = itemView.findViewById(R.id.tvRedPackageTime);
            tvRedPackageMoney = itemView.findViewById(R.id.tvRedPackageMoney);
        }

        void bindData(GetRedPackageRecordResponse.RedpackageListBean bean) {
            tvRedPackageNick.setText(bean.getNick());
            tvRedPackageTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.parseLong(bean.getCreateTime())));
            tvRedPackageMoney.setText(String.valueOf(bean.getMoney()) + "HK");
            if (bean.getTYPE().equals("1")) {
                tvRedPackageNick.setCompoundDrawablesRelative(null, null, context.getDrawable(R.drawable.ic_redpackage_pingshouqi), null);
                tvRedPackageNick.setCompoundDrawablePadding(CommonUtils.dip2px(context, 4));
            } else {
                tvRedPackageNick.setCompoundDrawablesRelative(null, null, null, null);
                tvRedPackageNick.setCompoundDrawablePadding(CommonUtils.dip2px(context, 0));
            }
        }
    }
}
