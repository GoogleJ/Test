package com.zxjk.duoduo.ui.grouppage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseFragment;

/**
 * author L
 * create at 2019/5/7
 * description: 社群
 */
public class CommunityFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_community, container, false);
        ImageView ivGame = rootView.findViewById(R.id.ivGame);
        TextView tv_title = rootView.findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.community));
        ivGame.setOnClickListener(v -> startActivity(new Intent(getContext(), AllGroupActivity.class)));
        return rootView;
    }
}
