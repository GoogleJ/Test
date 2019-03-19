package com.zxjk.duoduo.ui.minepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

/**
 * @author Administrator
 * @// TODO: 2019\3\19 0019
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_mine, container, false);

        LinearLayout llMineSetting = view.findViewById(R.id.llMineSetting);
        LinearLayout llMineBalanceLeft = view.findViewById(R.id.llMineBalanceLeft);
        CardView cardMineInfo = view.findViewById(R.id.cardMineInfo);
        llMineSetting.setOnClickListener(this);
        llMineBalanceLeft.setOnClickListener(this);
        cardMineInfo.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llMineSetting:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.cardMineInfo:
                startActivity(new Intent(getContext(), UserInfoActivity.class));
                break;
            case R.id.llMineBalanceLeft:
                startActivity(new Intent(getContext(), BalanceLeftActivity.class));
                break;
            default:
        }
    }
}
