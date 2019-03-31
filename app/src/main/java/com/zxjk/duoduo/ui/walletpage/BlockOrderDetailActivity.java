package com.zxjk.duoduo.ui.walletpage;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetTransferEthResponse;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;


public class BlockOrderDetailActivity extends BaseActivity {

    private ImageView ivBlockOrdersDetailTitle;
    private TextView tvBlockOrdersDetailTitle;
    private TextView tvBlockOrdersDetailTips;
    private TextView tvBlockOrdersDetailMoney;
    private TextView tvBlockOrdersDetailCoin;
    private TextView tvBlockOrdersDetailCount;
    private TextView tvBlockOrdersDetailKuanggong;
    private TextView tvBlockOrdersDetailTime;
    private TextView tvBlockOrdersDetailAddress1;
    private TextView tvBlockOrdersDetailAddress2;
    private TextView tvBlockOrdersDetailLast1;
    private TextView tvBlockOrdersDetailLast2;
    private TextView tvBlockOrdeerDetailBlock;
    private LinearLayout llBlockOrdeerDetailBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_order_detail);

        initView();

        String type = getIntent().getStringExtra("type");
        if (type.equals("ETH")) {
            GetTransferEthResponse.ListBean data = (GetTransferEthResponse.ListBean) getIntent().getSerializableExtra("data");
            String inOrOut = data.getInOrOut();
            if (inOrOut.equals("0")) {
                tvBlockOrdersDetailTitle.setText(R.string.zhuanru);
                tvBlockOrdersDetailTips.setText(R.string.receipt_success);
                tvBlockOrdersDetailMoney.setText("+" + data.getBalance() + "ETH");
                tvBlockOrdersDetailAddress1.setText(R.string.payAddress);
                tvBlockOrdersDetailAddress2.setText(data.getFromAddress().substring(0, 7) + "..." + data.getFromAddress().substring(data.getFromAddress().length() - 5));
            }
            if (inOrOut.equals("1")) {
                tvBlockOrdersDetailTitle.setText(R.string.zhuanchu);
                tvBlockOrdersDetailTips.setText(R.string.pay_success);
                tvBlockOrdersDetailMoney.setText("-" + data.getBalance() + "ETH");
                tvBlockOrdersDetailAddress1.setText(R.string.receiptAddress);
                tvBlockOrdersDetailAddress2.setText(data.getToAddress().substring(0, 7) + "..." + data.getToAddress().substring(data.getToAddress().length() - 5));
            }

            tvBlockOrdersDetailCoin.setText("ETH");
            tvBlockOrdersDetailCount.setText(data.getBalance());
            tvBlockOrdersDetailKuanggong.setText(data.getGasUsed());
            tvBlockOrdersDetailTime.setText(CommonUtils.formatTime(Long.parseLong(data.getCreateTime())));
            tvBlockOrdersDetailLast2.setText(data.getTransactionHash());
            tvBlockOrdeerDetailBlock.setText(data.getBlockNumber());
        }
    }

    private void initView() {
        ivBlockOrdersDetailTitle = findViewById(R.id.ivBlockOrdersDetailTitle);
        tvBlockOrdersDetailTitle = findViewById(R.id.tvBlockOrdersDetailTitle);
        tvBlockOrdersDetailTips = findViewById(R.id.tvBlockOrdersDetailTips);
        tvBlockOrdersDetailMoney = findViewById(R.id.tvBlockOrdersDetailMoney);
        tvBlockOrdersDetailCoin = findViewById(R.id.tvBlockOrdersDetailCoin);
        tvBlockOrdersDetailCount = findViewById(R.id.tvBlockOrdersDetailCount);
        tvBlockOrdersDetailKuanggong = findViewById(R.id.tvBlockOrdersDetailKuanggong);
        tvBlockOrdersDetailTime = findViewById(R.id.tvBlockOrdersDetailTime);
        tvBlockOrdersDetailAddress1 = findViewById(R.id.tvBlockOrdersDetailAddress1);
        tvBlockOrdersDetailAddress2 = findViewById(R.id.tvBlockOrdersDetailAddress2);
        tvBlockOrdersDetailLast1 = findViewById(R.id.tvBlockOrdersDetailLast1);
        tvBlockOrdersDetailLast2 = findViewById(R.id.tvBlockOrdersDetailLast2);
        tvBlockOrdeerDetailBlock = findViewById(R.id.tvBlockOrdeerDetailBlock);
        llBlockOrdeerDetailBlock = findViewById(R.id.llBlockOrdeerDetailBlock);
    }

    public void back(View view) {
        finish();
    }
}
