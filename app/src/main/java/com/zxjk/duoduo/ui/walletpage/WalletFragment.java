package com.zxjk.duoduo.ui.walletpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.ui.walletpage.adapter.WalletPageAdapter;
import com.zxjk.duoduo.ui.walletpage.model.WalletPageData;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WalletFragment extends BaseFragment {

    private TextView tvWalletPageTop1;
    private TextView tvWalletPageTop2;
    private TextView tvWalletPageTop3;
    private TextView tvWalletPageTop4;

    private RecyclerView recyclerWalletPage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_wallet, container, false);

        tvWalletPageTop1 = rootView.findViewById(R.id.tvWalletPageTop1);
        tvWalletPageTop2 = rootView.findViewById(R.id.tvWalletPageTop2);
        tvWalletPageTop3 = rootView.findViewById(R.id.tvWalletPageTop3);
        tvWalletPageTop4 = rootView.findViewById(R.id.tvWalletPageTop4);
        recyclerWalletPage = rootView.findViewById(R.id.recyclerWalletPage);

        recyclerWalletPage.setAdapter(new WalletPageAdapter(new ArrayList<>(5)));
        recyclerWalletPage.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        return rootView;
    }
}
