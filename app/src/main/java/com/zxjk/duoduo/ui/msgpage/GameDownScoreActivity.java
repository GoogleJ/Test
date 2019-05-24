package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.BaseResponse;
import com.zxjk.duoduo.network.response.GetGroupMemberPointsResponse;
import com.zxjk.duoduo.network.response.GroupResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.dialog.SelectPopupWindow;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.MD5Utils;
import com.zxjk.duoduo.utils.MoneyValueFilter;

import java.text.DecimalFormat;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * author L
 * create at 2019/5/8
 * description: 下分
 */
@SuppressLint("CheckResult")
public class GameDownScoreActivity extends BaseActivity {

    private GetGroupMemberPointsResponse getGroupMemberPointsResponse;
    private GroupResponse groupResponse;

    private EditText et;
    private TextView tv;
    private String total;
    private TextView tv1;
    private TextView tv2;

    private String groupId;

    private SelectPopupWindow selectPopupWindow;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_down_score);
        TextView tv_title = findViewById(R.id.tv_title);
        tv2 = findViewById(R.id.tv2);
        tv_title.setText(getString(R.string.downscore));
        TextView tv_end = findViewById(R.id.tv_end);
        tv_end.setVisibility(View.GONE);
        tv_end.setText(getString(R.string.jilu));

        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        //记录
        tv_end.setOnClickListener(v -> {

        });

        et = findViewById(R.id.et);
        et.setFilters(new InputFilter[]{new MoneyValueFilter()});
        tv = findViewById(R.id.tv);
        tv1 = findViewById(R.id.tv1);

        groupId = getIntent().getStringExtra("groupId");

        Api api = ServiceFactory.getInstance().getBaseService(Api.class);
        selectPopupWindow = new SelectPopupWindow(this, (psw, complete) -> {
            if (complete) {
                api.upPoints(groupId, et.getText().toString().trim(), MD5Utils.getMD5(psw))
                        .compose(RxSchedulers.normalTrans())
                        .compose(bindToLifecycle())
                        .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                        .subscribe(s -> {
                            ToastUtils.showShort(R.string.input_downscore2);
                            finish();
                        }, this::handleApiError);
            }
        });
        api.getGroupMemberPoints(groupId)
                .compose(RxSchedulers.normalTrans())
                .flatMap((Function<GetGroupMemberPointsResponse, ObservableSource<BaseResponse<GroupResponse>>>) s -> {
                    runOnUiThread(() -> {
                        getGroupMemberPointsResponse = s;
                        total = s.getHk();
                        tv.setText("当前可下分总额" + s.getHk() + "HK");
                    });
                    return api.getGroupByGroupId(groupId);
                })
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(s -> {
                    groupResponse = s;
                    if (s.getGroupInfo().getGroupOwnerId().equals(Constant.userId)) {
                        double string = CommonUtils.mul(Double.parseDouble(s.getGroupInfo().getSystemPumpingRate()), Double.parseDouble("100"));
                        tv1.setText("(手续费" + new DecimalFormat("0.00").format(string) + "%" + ")");
                    } else {
                        tv1.setText("");
                    }

                    if (s.getGroupInfo().getGameType().equals("4") && s.getGroupInfo().getGroupOwnerId().equals(Constant.userId)) {
                        tv2.setVisibility(View.VISIBLE);
                        tv2.setText("实际可下分总额：" + getGroupMemberPointsResponse.getHkAmount());
                    }
                }, this::handleApiError);
    }

    //下分
    public void commit(View view) {
        if (TextUtils.isEmpty(et.getText().toString().trim())) {
            ToastUtils.showShort(R.string.input_downscore);
            return;
        }
        if (Double.parseDouble(et.getText().toString().trim()) == 0) {
            ToastUtils.showShort(R.string.input_downscore3);
            return;
        }
        if (groupResponse.getGroupInfo().getGameType().equals("4")) {
            if (Double.parseDouble(getGroupMemberPointsResponse.getHkAmount()) < Double.parseDouble(et.getText().toString().trim())) {
                ToastUtils.showShort(R.string.input_downscore1);
                return;
            }
        }
        if (Double.parseDouble(total) < Double.parseDouble(et.getText().toString().trim())) {
            ToastUtils.showShort(R.string.input_downscore1);
            return;
        }
        KeyboardUtils.hideSoftInput(this);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        selectPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
    }


}
