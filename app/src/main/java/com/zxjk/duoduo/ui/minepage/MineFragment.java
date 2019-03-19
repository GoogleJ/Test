package com.zxjk.duoduo.ui.minepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.zxjk.duoduo.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

/**
 * @author Administrator
 * @// TODO: 2019\3\19 0019  
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_mine, container, false);

        LinearLayout llMineSetting = view.findViewById(R.id.llMineSetting);
        CardView cardMineInfo = view.findViewById(R.id.cardMineInfo);
        llMineSetting.setOnClickListener(this);
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
            default:
        }
    }
}
