package com.zxjk.duoduo.ui.msgpage.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
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
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.response.GetGroupGameParameterResponse;
import com.zxjk.duoduo.ui.msgpage.GroupGamebettingRequeust;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.game.GameAdapter;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
    private GetGroupGameParameterResponse.ParentListBean currentSelect;

    private OnCommit onCommit;

    public void setOnCommit(OnCommit onCommit) {
        this.onCommit = onCommit;
    }

    public interface OnCommit {
        void onCommit(String data, long parseLong);
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public GamePopupWindow(Context context) {
        super(context);

        setAllowDismissWhenTouchOutside(false);

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

        findViewById(R.id.ivGameClose).setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(et);
            dismiss();
        });

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

            if (Double.parseDouble(tvGameTotalMoney.getText().toString().trim()) > Double.parseDouble(data.getMaxBet())) {
                ToastUtils.showShort(R.string.morethenmax);
                return;
            }

            GroupGamebettingRequeust requeust = new GroupGamebettingRequeust();
            requeust.setBetMoneny(tvGameTotalMoney.getText().toString());
            requeust.setPlayId(currentID);
            requeust.setPlayName(currentName);
            requeust.setCustomerId(Constant.userId);
            requeust.setMultiple("0.00");
            requeust.setBetCardType("");
            if (llGamePeiLv.getVisibility() == View.VISIBLE) {
                requeust.setMultiple(currentSelect.getChildList().get(gameAdapter.getCheckedPosition()).getMultiple());
                requeust.setBetCardType(currentSelect.getChildList().get(gameAdapter.getCheckedPosition()).getPlayName());
            }
            requeust.setGroupId(groupId);
            if (this.onCommit != null) {
                onCommit.onCommit(GsonUtils.toJson(requeust), Long.parseLong(tvGameCountDown.getText().toString()));
            }
        });

        //游戏类型
        rgGame1.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbGame1:
                    currentID = niuniuID;
                    currentName = "牛牛";
                    llGamePeiLv.setVisibility(View.GONE);
                    currentSelect = null;
                    break;
                case R.id.rbGame2:
                    currentID = daxiaoID;
                    currentName = "大小单双合";
                    llGamePeiLv.setVisibility(View.VISIBLE);
                    gameAdapter.setData(data.getParentList().get(0).getChildList());
                    for (GetGroupGameParameterResponse.ParentListBean bean : data.getParentList()) {
                        if (bean.getPlayName().equals("大小单双合")) {
                            currentSelect = bean;
                            gameAdapter.setData(currentSelect.getChildList());
                        }
                    }
                    break;
                case R.id.rbGame3:
                    currentID = baijialeID;
                    currentName = "百家乐";
                    llGamePeiLv.setVisibility(View.VISIBLE);
                    gameAdapter.setData(data.getParentList().get(0).getChildList());
                    for (GetGroupGameParameterResponse.ParentListBean bean : data.getParentList()) {
                        if (bean.getPlayName().equals("百家乐")) {
                            currentSelect = bean;
                            gameAdapter.setData(currentSelect.getChildList());
                        }
                    }
                    break;
                default:
            }
        });

        //下注rg
        rgGame2.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbGame4:
                    if (!TextUtils.isEmpty(et.getText().toString().trim())) {
                        et.setText("");
                    }
                    tvGameTotalMoney.setText(rbGame4.getText().toString());
                    break;
                case R.id.rbGame5:
                    if (!TextUtils.isEmpty(et.getText().toString().trim())) {
                        et.setText("");
                    }
                    tvGameTotalMoney.setText(rbGame5.getText().toString());
                    break;
                default:
            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (rgGame2.getCheckedRadioButtonId() != -1) {
                    rgGame2.clearCheck();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    tvGameTotalMoney.setText(s);
                }
            }
        });

        recyclerGame.setLayoutManager(new GridLayoutManager(getContext(), 4));
        gameAdapter = new GameAdapter();
        gameAdapter.setData(new ArrayList<>());
        recyclerGame.setAdapter(gameAdapter);
    }

    private Disposable subscribe;

    public Disposable show(GetGroupGameParameterResponse data, long time) {
        this.data = data;
        bindData();
        this.showPopupWindow();
        subscribe = Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .take(time)
                .subscribe(l -> {
                    tvGameCountDown.setText((time - l) + "");
                    if (l == (time - 1)) {
                        ToastUtils.showShort(R.string.timeout_game);
                        dismiss();
                    }
                }, t -> {
                });
        return subscribe;
    }

    @Override
    public void onDismiss() {
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
        }
        super.onDismiss();
    }

    private String niuniuID;
    private String daxiaoID;
    private String baijialeID;
    private String currentID;
    private String currentName;

    private void bindData() {
        for (GetGroupGameParameterResponse.ParentListBean parentListBean : data.getParentList()) {
            if (parentListBean.getPlayName().equals("牛牛")) {
                niuniuID = parentListBean.getPlayId();
                rbGame1.setClickable(true);
                rbGame1.setAlpha(1f);
            } else if (parentListBean.getPlayName().equals("大小单双合")) {
                daxiaoID = parentListBean.getPlayId();
                rbGame2.setClickable(true);
                rbGame2.setAlpha(1f);
            } else if (parentListBean.getPlayName().equals("百家乐")) {
                baijialeID = parentListBean.getPlayId();
                rbGame3.setClickable(true);
                rbGame3.setAlpha(1f);
            }
        }

        rbGame4.setText(data.getMinBet());
        rbGame5.setText(data.getMaxBet());
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_game);
    }
}
