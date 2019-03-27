package com.zxjk.duoduo.ui.grouppage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Administrator
 * @// TODO: 2019\3\27 0027 关于底部导航来的群组的实现
 */
public class CommunityFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_community, container, false);
        return inflate;
    }
}
