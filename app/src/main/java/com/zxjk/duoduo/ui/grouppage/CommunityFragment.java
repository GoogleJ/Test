package com.zxjk.duoduo.ui.grouppage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Administrator
 */
public class CommunityFragment extends BaseFragment {

    private ImageView ivGame;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_community, container, false);
        ivGame = rootView.findViewById(R.id.ivGame);

        ivGame.setOnClickListener(v -> startActivity(new Intent(getContext(), AllGroupActivity.class)));
        return rootView;
    }
}
