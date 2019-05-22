package com.zxjk.duoduo.ui.walletpage;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * *********************
 * Administrator
 * *********************
 * 2019/5/22
 * *********************
 * 订单正在申诉
 * 订单申诉失败
 * 订单申诉成功
 * *********************
 */
public class OrderComplaintActivity extends BaseActivity {
    //标题
    @BindView(R.id.tv_title)
    TextView tvTitle;
    //订单申诉状态图标
    @BindView(R.id.iv_complaint)
    ImageView ivComplaint;
    //订单申诉状态文字描述
    @BindView(R.id.tv_complaint)
    TextView tvComplaint;
    //交易单号
    @BindView(R.id.tv_transactionNumber)
    TextView tvTransactionNumber;
    //申诉人
    @BindView(R.id.tv_plaintiff)
    TextView tvPlaintiff;
    //被申诉人
    @BindView(R.id.tv_byPlaintiff)
    TextView tvByPlaintiff;
    //数量
    @BindView(R.id.tv_number)
    TextView tvNumber;
    //金额
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    //申诉凭证
    @BindView(R.id.iv_proofComplaint)
    ImageView ivProofComplaint;
    //处理时间
    @BindView(R.id.tv_7)
    TextView tv7;
    @BindView(R.id.tv_processingTime)
    TextView tvProcessingTime;
    //申诉结果
    @BindView(R.id.tv_8)
    TextView tv8;
    @BindView(R.id.tv_complaintResults)
    TextView tvComplaintResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complaint);
        ButterKnife.bind(this);
        initView();
        initData();
    }


    private void initView() {
        tvTitle.setText("订单申诉");

        tvComplaint.setText("订单正在申诉");
        ivComplaint.setImageResource(R.drawable.icon_pending);
        tv7.setVisibility(View.GONE);
        tvProcessingTime.setVisibility(View.GONE);
        tv8.setVisibility(View.GONE);
        tvComplaintResults.setVisibility(View.GONE);

        tvComplaint.setText("订单申诉失败");
        ivComplaint.setImageResource(R.drawable.icon_transfer_failed);

        tvComplaint.setText("订单申诉完成");
        ivComplaint.setImageResource(R.drawable.icon_transfer_successful);


    }

    private void initData() {

    }

    @OnClick({R.id.rl_back, R.id.iv_proofComplaint})
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.rl_back:
                finish();
                break;
            //查看凭证
            case R.id.iv_proofComplaint:
                break;
        }
    }
}
