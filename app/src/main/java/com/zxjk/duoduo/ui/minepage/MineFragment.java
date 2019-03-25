package com.zxjk.duoduo.ui.minepage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.ui.msgpage.MyQrCodeActivity;
import com.zxjk.duoduo.utils.GlideUtil;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

/**
 * @author Administrator
 * @// TODO: 2019\3\19 0019
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private ImageView ivMineHeadImg;
    private ImageView ivMineAuthSign;
    private TextView tvMineNick;
    private TextView tvMineDuNum;
    private TextView tvMineSign;
    private ImageView ivMineQRcode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_mine, container, false);

        LinearLayout llMineSetting = view.findViewById(R.id.llMineSetting);
        LinearLayout llMineBalanceLeft = view.findViewById(R.id.llMineBalanceLeft);
        CardView cardMineInfo = view.findViewById(R.id.cardMineInfo);
        ivMineHeadImg = view.findViewById(R.id.ivMineHeadImg);
        ivMineAuthSign = view.findViewById(R.id.ivMineAuthSign);
        tvMineNick = view.findViewById(R.id.tvMineNick);
        tvMineDuNum = view.findViewById(R.id.tvMineDuNum);
        tvMineSign = view.findViewById(R.id.tvMineSign);
        ivMineQRcode = view.findViewById(R.id.ivMineQRcode);
        tvMineDuNum.setText(Constant.currentUser.getDuoduoId());

        llMineSetting.setOnClickListener(this);
        llMineBalanceLeft.setOnClickListener(this);
        cardMineInfo.setOnClickListener(this);
        ivMineQRcode.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tvMineNick.setText(Constant.currentUser.getNick());
        if (TextUtils.isEmpty(tvMineSign.getText())) {
            tvMineSign.setText(R.string.none);
        } else {
            tvMineSign.setText(Constant.currentUser.getSignature());
        }
        GlideUtil.loadCornerImg(ivMineHeadImg, Constant.currentUser.getHeadPortrait(), 3);

        String isAuthentication = Constant.currentUser.getIsAuthentication();
        if (isAuthentication.equals("0")) {
            ivMineAuthSign.setVisibility(View.VISIBLE);
        } else {
            ivMineAuthSign.setVisibility(View.INVISIBLE);
        }
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
            case R.id.ivMineQRcode:
                startActivity(new Intent(getContext(), MyQrCodeActivity.class));
                break;
            default:
        }
    }
}
