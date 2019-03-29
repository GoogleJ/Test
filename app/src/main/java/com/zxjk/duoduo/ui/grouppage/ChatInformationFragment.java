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
 * @// TODO: 2019\3\29 0029 关于群组详情的实现 
 */
public class ChatInformationFragment extends BaseFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat_information,null);
        return view;
    }
}
