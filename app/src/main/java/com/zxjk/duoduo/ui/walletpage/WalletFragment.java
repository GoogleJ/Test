package com.zxjk.duoduo.ui.walletpage;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.ui.minepage.BalanceLeftActivity;
import com.zxjk.duoduo.ui.msgpage.QrCodeActivity;
import com.zxjk.duoduo.ui.walletpage.adapter.WalletPageAdapter;

import java.util.ArrayList;

/**
 * 钱包模块
 */
public class WalletFragment extends BaseFragment {

    private TextView tvWalletPageTop1;
    private TextView tvWalletPageTop2;
    private TextView tvWalletPageTop3;
    private TextView tvWalletPageTop4;

    private RecyclerView recyclerWalletPage;
    private TextView tv_title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_wallet, container, false);

        tvWalletPageTop1 = rootView.findViewById(R.id.tvWalletPageTop1);
        tvWalletPageTop2 = rootView.findViewById(R.id.tvWalletPageTop2);
        tvWalletPageTop3 = rootView.findViewById(R.id.tvWalletPageTop3);
        tvWalletPageTop4 = rootView.findViewById(R.id.tvWalletPageTop4);
        tv_title = rootView.findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.duoduo_wallet));
        recyclerWalletPage = rootView.findViewById(R.id.recyclerWalletPage);

        recyclerWalletPage.setAdapter(new WalletPageAdapter(new ArrayList<>(5)));
        recyclerWalletPage.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        getPermisson(tvWalletPageTop1, result -> {
            if (result) {
                startActivity(new Intent(getContext(), QrCodeActivity.class));
            }
        }, Manifest.permission.CAMERA);

        tvWalletPageTop3.setOnClickListener(v -> startActivity(new Intent(getContext(), BlockWalletActivity.class)));
        tvWalletPageTop4.setOnClickListener(v -> startActivity(new Intent(getContext(), BalanceLeftActivity.class)));
        tvWalletPageTop2.setOnClickListener(v -> startActivity(new Intent(getContext(), RecipetQRActivity.class)));
        recyclerWalletPage.setHasFixedSize(true);
        recyclerWalletPage.setNestedScrollingEnabled(false);
        return rootView;
    }
}
