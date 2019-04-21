package com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetGroupGameParameterResponse;

import java.util.ArrayList;

import razerdp.basepopup.BasePopupWindow;

public class GamePopupWindow extends BasePopupWindow {

    private TextView tvGameCountDown;
    private TextView tvGameTotalMoney;
    private RecyclerView recyclerGame;
    private LinearLayout llGamePeiLv;
    private RadioGroup rgGame1;
    private RadioGroup rgGame2;
    private EditText et;
    private RadioButton rbGame1;
    private RadioButton rbGame2;
    private RadioButton rbGame3;
    private RadioButton rbGame4;
    private RadioButton rbGame5;

    private GameAdapter gameAdapter;
    private GetGroupGameParameterResponse data;

    public GamePopupWindow(Context context) {
        super(context);

        rbGame1 = findViewById(R.id.rbGame1);
        rbGame2 = findViewById(R.id.rbGame2);
        rbGame3 = findViewById(R.id.rbGame3);
        rbGame4 = findViewById(R.id.rbGame4);
        rbGame5 = findViewById(R.id.rbGame5);
        et = findViewById(R.id.et);
        tvGameCountDown = findViewById(R.id.tvGameCountDown);
        rgGame1 = findViewById(R.id.rgGame1);
        rgGame2 = findViewById(R.id.rgGame2);
        llGamePeiLv = findViewById(R.id.llGamePeiLv);
        recyclerGame = findViewById(R.id.recyclerGame);
        tvGameTotalMoney = findViewById(R.id.tvGameTotalMoney);

        findViewById(R.id.ivGameClose).setOnClickListener(v -> dismiss());

        findViewById(R.id.btnGameStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始游戏
                if (rgGame1.getCheckedRadioButtonId() == -1) {
                    ToastUtils.showShort(R.string.select_game_type);
                    return;
                }

                if (rgGame2.getCheckedRadioButtonId() == -1 && et.getText().toString().trim().length() == 0) {
                    ToastUtils.showShort(R.string.qingxiazhu);
                    return;
                }

                if (gameAdapter.getCheckedPosition() == -1 && llGamePeiLv.getVisibility() == View.VISIBLE) {
                    ToastUtils.showShort(R.string.select_beilv);
                    return;
                }

//                ServiceFactory.getInstance().getBaseService(Api.class)
//                        .xiazhu();
            }
        });

        //游戏类型
        rgGame1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbGame1:
                        llGamePeiLv.setVisibility(View.GONE);
                        break;
                    case R.id.rbGame2:
                        llGamePeiLv.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rbGame3:
                        llGamePeiLv.setVisibility(View.VISIBLE);
                        break;
                    default:
                }
            }
        });

        //下注rg
        rgGame2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbGame4:
                        et.setText("");
                        break;
                    case R.id.rbGame5:
                        et.setText("");
                        break;
                    default:
                }
            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    rgGame2.clearCheck();
                }
            }
        });

        recyclerGame.setLayoutManager(new GridLayoutManager(getContext(), 4));
        gameAdapter = new GameAdapter();
        gameAdapter.setData(new ArrayList<>());
        recyclerGame.setAdapter(gameAdapter);
    }

    public void show(GetGroupGameParameterResponse data) {
        this.data = data;
        bindData();
        this.showPopupWindow();
    }

    private void bindData() {
        for (GetGroupGameParameterResponse.ParentListBean parentListBean : data.getParentList()) {
            if (parentListBean.getPlayName().equals("牛牛")) {
                rbGame1.setClickable(true);
            } else if (parentListBean.getPlayName().equals("大小单双和")) {
                rbGame2.setClickable(true);
            } else if (parentListBean.getPlayName().equals("百家乐")) {
                rbGame3.setClickable(true);
            }
        }

        rbGame4.setText(data.getMinBet());
        rbGame5.setText(data.getMinBet());
        gameAdapter.setData(data.getParentList().get(0).getChildList());
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_game);
    }
}
