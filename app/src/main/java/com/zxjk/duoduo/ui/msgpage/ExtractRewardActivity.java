package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.utils.MoneyTextWatcher;
import com.zxjk.duoduo.utils.CommonUtils;

/**
 * 提取奖励
 */
@SuppressLint({"CheckResult", "SetTextI18n"})
public class ExtractRewardActivity extends BaseActivity {
    private TextView tv_amountWithdrawal;
    private EditText et_withdrawalAmount;
    private String groupId;


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
        et_withdrawalAmount.addTextChangedListener(new MoneyTextWatcher(et_withdrawalAmount));
        et_withdrawalAmount.setSelection(et_withdrawalAmount.getText().toString().length());
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
                    tv_amountWithdrawal.setText(s + "HK");
                    findViewById(R.id.tv_confirmWithdrawal).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            withdrawDeposit();
                        }
                    });
                }, this::handleApiError);


    }

    private void withdrawDeposit() {
        if (!TextUtils.isEmpty(et_withdrawalAmount.getText().toString())) {

        } else {
            ToastUtils.showShort(getString(R.string.hint_money));
        }


    }
}
