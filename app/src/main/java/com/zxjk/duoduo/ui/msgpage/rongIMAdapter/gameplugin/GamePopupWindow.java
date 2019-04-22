package com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin;

import android.annotation.SuppressLint;
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

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetGroupGameParameterResponse;
import com.zxjk.duoduo.ui.msgpage.GroupGamebettingRequeust;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import razerdp.basepopup.BasePopupWindow;

public class GamePopupWindow extends BasePopupWindow {

    private String groupId;
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

    private OnCommit onCommit;

    public void setOnCommit(OnCommit onCommit) {
        this.onCommit = onCommit;
    }

    public interface OnCommit {
        void onCommit(String data);
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

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

        findViewById(R.id.btnGameStart).setOnClickListener(v -> {
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

            if (Double.parseDouble(et.getText().toString().trim()) > Double.parseDouble(data.getMaxBet())) {
                ToastUtils.showShort(R.string.morethenmax);
                return;
            }

            GroupGamebettingRequeust requeust = new GroupGamebettingRequeust();
            requeust.setBetMoneny(tvGameTotalMoney.getText().toString());
            requeust.setPlayId(currentID);
            requeust.setPlayName(currentName);
            requeust.setCustomerId(Constant.userId);
            requeust.setMultiple(rgGame2.getCheckedRadioButtonId() == R.id.rbGame4 ? rbGame4.getText().toString() : (
                    rgGame2.getCheckedRadioButtonId() == R.id.rbGame5 ? rbGame5.getText().toString().trim() : et.getText().toString().trim()
            ));
            requeust.setBetCardType(currentSlect.getChildList().get(gameAdapter.getCheckedPosition()).getPlayName());
            requeust.setGroupId(groupId);
            if (this.onCommit != null) {
                onCommit.onCommit(GsonUtils.toJson(requeust));
            }
        });

        //游戏类型
        rgGame1.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbGame1:
                    currentID = niuniuID;
                    currentName = "牛牛";
                    llGamePeiLv.setVisibility(View.GONE);
                    break;
                case R.id.rbGame2:
                    currentID = daxiaoID;
                    currentName = "大小单双和";
                    llGamePeiLv.setVisibility(View.VISIBLE);
                    break;
                case R.id.rbGame3:
                    currentID = baijialeID;
                    currentName = "百家乐";
                    llGamePeiLv.setVisibility(View.VISIBLE);
                    break;
                default:
            }
        });

        //下注rg
        rgGame2.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbGame4:
                    et.setText("");
                    break;
                case R.id.rbGame5:
                    et.setText("");
                    break;
                default:
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
                    tvGameTotalMoney.setText(s);
                }
            }
        });

        recyclerGame.setLayoutManager(new GridLayoutManager(getContext(), 4));
        gameAdapter = new GameAdapter();
        gameAdapter.setData(new ArrayList<>());
        recyclerGame.setAdapter(gameAdapter);
    }

    @SuppressLint("CheckResult")
    public void show(GetGroupGameParameterResponse data) {
        Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .take(20)
                .subscribe(l -> {
                    tvGameCountDown.setText((20 - l) + "");
                    if (l == 19) {
                        ToastUtils.showShort(R.string.timeout_game);
                        dismiss();
                    }
                }, t -> {
                });
        this.data = data;
        bindData();
        this.showPopupWindow();
    }

    private String niuniuID;
    private String daxiaoID;
    private String baijialeID;
    private String currentID;
    private String currentName;
    private GetGroupGameParameterResponse.ParentListBean currentSlect;

    private void bindData() {
        for (GetGroupGameParameterResponse.ParentListBean parentListBean : data.getParentList()) {
            currentSlect = parentListBean;
            if (parentListBean.getPlayName().equals("牛牛")) {
                niuniuID = parentListBean.getPlayId();
                rbGame1.setClickable(true);
            } else if (parentListBean.getPlayName().equals("大小单双和")) {
                daxiaoID = parentListBean.getPlayId();
                rbGame2.setClickable(true);
            } else if (parentListBean.getPlayName().equals("百家乐")) {
                baijialeID = parentListBean.getPlayId();
                rbGame3.setClickable(true);
            }
        }

        rbGame4.setText(getFormatNum(Double.parseDouble(data.getMinBet())));
        rbGame5.setText(getFormatNum(Double.parseDouble(data.getMaxBet())));
        gameAdapter.setData(data.getParentList().get(0).getChildList());
    }

    private String getFormatNum(double number) {
        return String.valueOf(number > 10000 ? ((number - number % 1000) / 10000 + 'W') : number);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_game);
    }
}
