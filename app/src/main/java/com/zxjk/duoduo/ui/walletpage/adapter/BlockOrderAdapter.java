package com.zxjk.duoduo.ui.walletpage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetReleasePurchaseResponse;
import com.zxjk.duoduo.network.response.GetTransferAllResponse;
import com.zxjk.duoduo.ui.walletpage.model.BlockWalletOrderData;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.crypto.interfaces.PBEKey;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BlockOrderAdapter extends RecyclerView.Adapter<BlockOrderAdapter.ViewHoder> {
    private Context context;
    private List<GetTransferAllResponse.ListBean> data;

    private OnClickListener onClickListener;

    public interface OnClickListener {
        void onClick(GetTransferAllResponse.ListBean data);
    }

    public void setData(List<GetTransferAllResponse.ListBean> data) {
        this.data = data;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        return new ViewHoder(LayoutInflater.from(context).inflate(R.layout.item_blockorder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        holder.bindData(data.get(position));
        holder.llItemClick.setOnClickListener(v -> {
            if (null != onClickListener) {
                onClickListener.onClick(data.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHoder extends RecyclerView.ViewHolder {

        private ImageView ivItemBlockOrderImgLeft;
        private ImageView ivItemArrow;
        private TextView tvWalletAddress;
        private TextView tvItemFirst;
        private TextView tvItemSecond;
        private TextView tvItemTime;
        private TextView tvItemState;
        private LinearLayout llItemClick;

        ViewHoder(@NonNull View itemView) {
            super(itemView);
            ivItemBlockOrderImgLeft = itemView.findViewById(R.id.ivItemBlockOrderImgLeft);
            ivItemArrow = itemView.findViewById(R.id.ivItemArrow);
            tvWalletAddress = itemView.findViewById(R.id.tvWalletAddress);
            tvItemFirst = itemView.findViewById(R.id.tvItemFirst);
            tvItemSecond = itemView.findViewById(R.id.tvItemSecond);
            tvItemTime = itemView.findViewById(R.id.tvItemTime);
            tvItemState = itemView.findViewById(R.id.tvItemState);
            llItemClick = itemView.findViewById(R.id.llItemClick);
        }

        void bindData(GetTransferAllResponse.ListBean bean) {
//            tvItemTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bean.getCreateTime()));
            tvItemState.setText((bean.getTxreceiptStatus().equals("0")) ? R.string.failed : (bean.getTxreceiptStatus().equals("1") ? R.string.success : R.string.procssing));
            if (bean.getSerialType().equals("0") || bean.getSerialType().equals("1")) {
                tvWalletAddress.setVisibility(View.VISIBLE);
                tvItemFirst.setVisibility(View.GONE);
                tvItemSecond.setVisibility(View.GONE);
                ivItemArrow.setVisibility(View.GONE);
                if (bean.getInOrOut().equals("0")) {
                    tvWalletAddress.setText(bean.getFromAddress());
                    ivItemBlockOrderImgLeft.setImageResource(R.drawable.ic_ethorders_icon_in);
                } else {
                    tvWalletAddress.setText(bean.getToAddress());
                    ivItemBlockOrderImgLeft.setImageResource(R.drawable.ic_ethorders_icon_out);
                }
            } else {
                ivItemBlockOrderImgLeft.setImageResource(R.drawable.ic_huazhuan_order);
                tvWalletAddress.setVisibility(View.GONE);
                tvItemFirst.setVisibility(View.VISIBLE);
                tvItemSecond.setVisibility(View.VISIBLE);
                ivItemArrow.setVisibility(View.VISIBLE);
                if (bean.getSerialType().equals("2")) {
                    tvItemFirst.setText("HK");
                    tvItemSecond.setText("HKB");
                } else {
                    tvItemFirst.setText("HKB");
                    tvItemSecond.setText("HK");
                }
            }
        }
    }
}
