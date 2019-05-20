package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.dialog.SelectPopupWindow;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.MD5Utils;
import com.zxjk.duoduo.utils.MoneyValueFilter;

/**
 * 提取奖励
 */
@SuppressLint({"CheckResult", "SetTextI18n"})
public class ExtractRewardActivity extends BaseActivity implements SelectPopupWindow.OnPopWindowClickListener {
    private TextView tv_amountWithdrawal;
    private EditText et_withdrawalAmount;
    private String groupId;

    SelectPopupWindow selectPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract_reward);
        initView();
        initData();
    }

    private void initView() {
        groupId = getIntent().getStringExtra("groupId");
        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_end = findViewById(R.id.tv_end);
        tv_amountWithdrawal = findViewById(R.id.tv_amountWithdrawal);
        et_withdrawalAmount = findViewById(R.id.et_withdrawalAmount);
        et_withdrawalAmount.setFilters(new InputFilter[]{new MoneyValueFilter()});
        tv_title.setText(getString(R.string.extract_reward));
        tv_end.setVisibility(View.VISIBLE);
        tv_end.setText(getString(R.string.record));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        tv_end.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExtractRewardRecordActivity.class);
            intent.putExtra("groupId", groupId);
            startActivity(intent);
        });

    }

    private void initData() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getRebateAmount(groupId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    CommonUtils.destoryDialog();
                    tv_amountWithdrawal.setText(" " + s + "HK");
                    findViewById(R.id.tv_confirmWithdrawal).setOnClickListener(v -> withdrawDeposit());
                }, this::handleApiError);


    }

    private void withdrawDeposit() {
        if (!TextUtils.isEmpty(et_withdrawalAmount.getText().toString())
                && !et_withdrawalAmount.getText().toString().equals("0")) {
            inoutPsw();
        } else {
            ToastUtils.showShort(getString(R.string.hint_money));
        }


    }

    public void inoutPsw() {
        KeyboardUtils.hideSoftInput(this);
        selectPopupWindow = new SelectPopupWindow(this, this);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        selectPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
    }

    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (!complete) {
            return;
        }
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getWithdrawalAmount(groupId, Constant.ownerIdForGameChat, et_withdrawalAmount.getText().toString(), MD5Utils.getMD5(psw))
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    Intent intent = new Intent(this, WithdrawalSuccessActivity.class);
                    intent.putExtra("money", et_withdrawalAmount.getText().toString());
                    startActivity(intent);

                }, this::handleApiError);


    }
}
