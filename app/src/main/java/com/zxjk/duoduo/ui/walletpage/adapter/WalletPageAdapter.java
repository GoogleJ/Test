package com.zxjk.duoduo.ui.walletpage.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.walletpage.ExchangeActivity;
import com.zxjk.duoduo.ui.walletpage.model.WalletPageData;

import java.util.ArrayList;

public class WalletPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<WalletPageData> data;
    private Context context;

    private static final int VIEW_TYPE_TOP = 1;
    private static final int VIEW_TYPE_NEWS = 2;

    public WalletPageAdapter(ArrayList<WalletPageData> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_TOP : VIEW_TYPE_NEWS;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }

        if (viewType == VIEW_TYPE_TOP) {
            return new TopHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.walletpage_viewtype_top, parent, false));
        }
        return new NewsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.walletpage_viewtype_news, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class TopHolder extends RecyclerView.ViewHolder {
        private ImageView tvWalletPageExchange1;
        private ImageView tvWalletPageExchange2;
        private LinearLayout ll_1;
        private LinearLayout ll_2;

        TopHolder(@NonNull View itemView) {
            super(itemView);

            tvWalletPageExchange1 = itemView.findViewById(R.id.tvWalletPageExchange1);
            tvWalletPageExchange2 = itemView.findViewById(R.id.tvWalletPageExchange2);
            ll_1 = itemView.findViewById(R.id.ll_1);
            ll_2 = itemView.findViewById(R.id.ll_2);
            ll_1.setOnClickListener(v -> ToastUtils.showShort("暂未开放"));
            ll_2.setOnClickListener(v -> ToastUtils.showShort("暂未开放"));
            tvWalletPageExchange1.setOnClickListener(v -> ToastUtils.showShort("暂未开放"));
            tvWalletPageExchange2.setOnClickListener(v -> context.startActivity(new Intent(context, ExchangeActivity.class)));
        }
    }

    class NewsHolder extends RecyclerView.ViewHolder {

        NewsHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
