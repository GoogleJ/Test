package com.zxjk.duoduo.ui.walletpage;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetTransferAllResponse;
import com.zxjk.duoduo.network.response.GetTransferEthResponse;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;

import androidx.core.content.ContextCompat;


public class BlockOrderDetailActivity extends BaseActivity {

    private ImageView ivBlockOrdersDetailTitle;
    private TextView tvBlockOrdersDetailTitle;
    private TextView tvBlockOrdersDetailTips;
    private TextView tvBlockOrdersDetailMoney;
    private TextView tvItemSecond;
    private TextView tvBlockOrdersDetailCount;
    private TextView tvBlockOrdersDetailKuanggong;
    private TextView tvBlockOrdersDetailTime;
    private TextView tvBlockOrdersDetailAddress1;
    private TextView tvBlockOrdersDetailAddress2;
    private TextView tvBlockOrdersDetailLast1;
    private TextView tvBlockOrdersDetailLast2;
    private TextView tvBlockOrdeerDetailBlock;
    private LinearLayout llBlockOrdeerDetailBlock;
    private TextView tvBlockOrdersDetailCurrency;
    private TextView tvItemFirst;
    private ImageView ivItemArrow;
    private LinearLayout collectionAddress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_order_detail);
        initView();

        initData();
    }

    private void initData() {
        String type = getIntent().getStringExtra("type");
        GetTransferAllResponse.ListBean data = (GetTransferAllResponse.ListBean) getIntent().getSerializableExtra("data");



        if ("0".equals(type )) {
            //eth转入失败-成功-进行中
            if (data.getInOrOut().equals("0") && data.getTxreceiptStatus().equals("0")) {
                ivBlockOrdersDetailTitle.setImageResource(R.drawable.icon_transfer_failed);
                tvBlockOrdersDetailTips.setText(getString(R.string.transfer_in_failed));
                tvBlockOrdersDetailMoney.setText(data.getBalance() + "ETH");
                tvBlockOrdersDetailAddress1.setText(getString(R.string.receiptAddress));
                tvBlockOrdersDetailAddress2.setText(data.getFromAddress().substring(0, 7) + "..." + data.getFromAddress().substring(data.getFromAddress().length() - 5));

            } else if (data.getInOrOut().equals("0") && data.getTxreceiptStatus().equals("1")) {
                ivBlockOrdersDetailTitle.setImageResource(R.drawable.icon_transfer_successful);
                tvBlockOrdersDetailTips.setText(getString(R.string.transfer_in_successful));
                tvBlockOrdersDetailMoney.setText(data.getBalance() + "ETH");
                tvBlockOrdersDetailAddress1.setText(getString(R.string.receiptAddress));
                tvBlockOrdersDetailAddress2.setText(data.getFromAddress().substring(0, 7) + "..." + data.getFromAddress().substring(data.getFromAddress().length() - 5));
            } else if (data.getInOrOut().equals("0") && data.getTxreceiptStatus().equals("2")) {
                ivBlockOrdersDetailTitle.setImageResource(R.drawable.icon_caveat);
                tvBlockOrdersDetailTips.setText(getString(R.string.transfer_in_wait));
                tvBlockOrdersDetailMoney.setText(data.getBalance() + "ETH");
                tvBlockOrdersDetailAddress1.setText(getString(R.string.receiptAddress));
                tvBlockOrdersDetailAddress2.setText(data.getFromAddress().substring(0, 7) + "..." + data.getFromAddress().substring(data.getFromAddress().length() - 5));
                return;
            }

            //eth转出失败-成功-进行中
            if (data.getInOrOut().equals("1") && data.getTxreceiptStatus().equals("0")) {
                ivBlockOrdersDetailTitle.setImageResource(R.drawable.icon_transfer_failed);
                tvBlockOrdersDetailTips.setText(getString(R.string.transfer_out_failed));
                tvBlockOrdersDetailMoney.setText("-" + data.getBalance() + "ETH");
                tvBlockOrdersDetailAddress1.setText(getString(R.string.receiptAddress));
                tvBlockOrdersDetailAddress2.setText(data.getFromAddress().substring(0, 7) + "..." + data.getFromAddress().substring(data.getFromAddress().length() - 5));
            } else if (data.getInOrOut().equals("1") && data.getTxreceiptStatus().equals("1")) {
                ivBlockOrdersDetailTitle.setImageResource(R.drawable.icon_transfer_successful);
                tvBlockOrdersDetailTips.setText(getString(R.string.transfer_out_successful));
                tvBlockOrdersDetailMoney.setText("-" + data.getBalance() + "ETH");
                tvBlockOrdersDetailAddress1.setText(getString(R.string.receiptAddress));
                tvBlockOrdersDetailAddress2.setText(data.getFromAddress().substring(0, 7) + "..." + data.getFromAddress().substring(data.getFromAddress().length() - 5));
            } else if (data.getInOrOut().equals("1") && data.getTxreceiptStatus().equals("2")) {
                ivBlockOrdersDetailTitle.setImageResource(R.drawable.icon_caveat);
                tvBlockOrdersDetailTips.setText(getString(R.string.transfer_out_wait));
                tvBlockOrdersDetailMoney.setText(data.getBalance() + "ETH");
                tvBlockOrdersDetailAddress1.setText(getString(R.string.receiptAddress));
                tvBlockOrdersDetailAddress2.setText(data.getFromAddress().substring(0, 7) + "..." + data.getFromAddress().substring(data.getFromAddress().length() - 5));
                return;
            }
            tvItemFirst.setVisibility(View.GONE);
            ivItemArrow.setVisibility(View.GONE);
            tvBlockOrdersDetailCount.setText(data.getBalance());
            tvBlockOrdersDetailKuanggong.setText(data.getGasUsed());
            tvBlockOrdersDetailTime.setText(CommonUtils.formatTime(Long.parseLong(data.getCreateTime())));
            tvBlockOrdersDetailLast2.setText(data.getTransactionHash());
            tvBlockOrdeerDetailBlock.setText(data.getBlockNumber());
            tvItemSecond.setText("HKB");
            tvBlockOrdersDetailTitle.setText(getString(R.string.transfer_details));
            tvItemSecond.setText("ETH");
        } else if ("1".equals(type)) {
            //HKB转入失败-成功-进行中
            if (data.getInOrOut().equals("0") && data.getTxreceiptStatus().equals("0")) {
                ivBlockOrdersDetailTitle.setImageResource(R.drawable.icon_transfer_failed);
                tvBlockOrdersDetailTips.setText(getString(R.string.transfer_in_failed));
                tvBlockOrdersDetailMoney.setText(data.getBalance() + "HKB");
                tvBlockOrdersDetailAddress1.setText(getString(R.string.receiptAddress));
                tvBlockOrdersDetailAddress2.setText(data.getFromAddress().substring(0, 7) + "..." + data.getFromAddress().substring(data.getFromAddress().length() - 5));

            } else if (data.getInOrOut().equals("0") && data.getTxreceiptStatus().equals("1")) {
                ivBlockOrdersDetailTitle.setImageResource(R.drawable.icon_transfer_successful);
                tvBlockOrdersDetailTips.setText(getString(R.string.transfer_in_successful));
                tvBlockOrdersDetailMoney.setText(data.getBalance() + "HKB");
                tvBlockOrdersDetailAddress1.setText(getString(R.string.receiptAddress));
                tvBlockOrdersDetailAddress2.setText(data.getFromAddress().substring(0, 7) + "..." + data.getFromAddress().substring(data.getFromAddress().length() - 5));
            } else if (data.getInOrOut().equals("0") && data.getTxreceiptStatus().equals("2")) {
                ivBlockOrdersDetailTitle.setImageResource(R.drawable.icon_caveat);
                tvBlockOrdersDetailTips.setText(getString(R.string.transfer_in_wait));
                tvBlockOrdersDetailMoney.setText(data.getBalance() + "HKB");
                tvBlockOrdersDetailAddress1.setText(getString(R.string.receiptAddress));
                tvBlockOrdersDetailAddress2.setText(data.getFromAddress().substring(0, 7) + "..." + data.getFromAddress().substring(data.getFromAddress().length() - 5));
                return;
            }
            //HKB转出失败-成功-进行中
            if (data.getInOrOut().equals("1") && data.getTxreceiptStatus().equals("0")) {
                ivBlockOrdersDetailTitle.setImageResource(R.drawable.icon_transfer_failed);
                tvBlockOrdersDetailTips.setText(getString(R.string.transfer_out_failed));
                tvBlockOrdersDetailMoney.setText("-" + data.getBalance() + "HKB");
                tvBlockOrdersDetailAddress1.setText(getString(R.string.receiptAddress));
                tvBlockOrdersDetailAddress2.setText(data.getFromAddress().substring(0, 7) + "..." + data.getFromAddress().substring(data.getFromAddress().length() - 5));
            } else if (data.getInOrOut().equals("1") && data.getTxreceiptStatus().equals("1")) {
                ivBlockOrdersDetailTitle.setImageResource(R.drawable.icon_transfer_successful);
                tvBlockOrdersDetailTips.setText(getString(R.string.transfer_out_successful));
                tvBlockOrdersDetailMoney.setText("-" + data.getBalance() + "HKB");
                tvBlockOrdersDetailAddress1.setText(getString(R.string.receiptAddress));
                tvBlockOrdersDetailAddress2.setText(data.getFromAddress().substring(0, 7) + "..." + data.getFromAddress().substring(data.getFromAddress().length() - 5));
            } else if (data.getInOrOut().equals("1") && data.getTxreceiptStatus().equals("2")) {
                ivBlockOrdersDetailTitle.setImageResource(R.drawable.icon_caveat);
                tvBlockOrdersDetailTips.setText(getString(R.string.transfer_out_wait));
                tvBlockOrdersDetailMoney.setText(data.getBalance() + "HKB");
                tvBlockOrdersDetailAddress1.setText(getString(R.string.receiptAddress));
                tvBlockOrdersDetailAddress2.setText(data.getFromAddress().substring(0, 7) + "..." + data.getFromAddress().substring(data.getFromAddress().length() - 5));
                return;
            }
            tvItemFirst.setVisibility(View.GONE);
            ivItemArrow.setVisibility(View.GONE);
            tvBlockOrdersDetailCount.setText(data.getBalance());
            tvBlockOrdersDetailKuanggong.setText(data.getGasUsed());
            tvBlockOrdersDetailTime.setText(CommonUtils.formatTime(Long.parseLong(data.getCreateTime())));
            tvBlockOrdersDetailLast2.setText(data.getTransactionHash());
            tvBlockOrdeerDetailBlock.setText(data.getBlockNumber());
            tvItemSecond.setText("HKB");

        } else if ("2".equals(type)||"3".equals(type))  {
            if ("0".equals(data.getTxreceiptStatus())){
                ivBlockOrdersDetailTitle.setImageResource(R.drawable.icon_transfer_failed);
                tvBlockOrdersDetailTips.setText(getString(R.string.transfer_commit_failed));
                tvBlockOrdersDetailMoney.setText("+"+data.getBalance() + "HK");
            }else if ("1".equals(data.getTxreceiptStatus())){
                ivBlockOrdersDetailTitle.setImageResource(R.drawable.icon_transfer_successful);
                tvBlockOrdersDetailTips.setText(getString(R.string.transfer_commit_successful));
                tvBlockOrdersDetailMoney.setText("+"+data.getBalance() + "HK");
            }else{
                ivBlockOrdersDetailTitle.setImageResource(R.drawable.icon_caveat);
                tvBlockOrdersDetailTips.setText(getString(R.string.transfer_commit_wait));
                tvBlockOrdersDetailMoney.setText("+"+data.getBalance()+"HK");
            }
            tvItemFirst.setVisibility(View.VISIBLE);
            ivItemArrow.setVisibility(View.VISIBLE);
            tvBlockOrdersDetailTitle.setText(R.string.transfer_commit_title);
            tvBlockOrdersDetailCurrency.setText(getString(R.string.tvBlockOrdersDetailCurrency));
            tvItemFirst.setText("HK");
            tvItemSecond.setText("HKB");
            tvBlockOrdersDetailCount.setText(data.getBalance());
            tvBlockOrdersDetailKuanggong.setText(data.getGasUsed());
            tvBlockOrdersDetailTime.setText(CommonUtils.formatTime(Long.parseLong(data.getCreateTime())));
            collectionAddress.setVisibility(View.GONE);
            tvBlockOrdersDetailLast2.setText(data.getTransactionHash());
            llBlockOrdeerDetailBlock.setVisibility(View.GONE);



        }

    }

    private void initView() {
        ivBlockOrdersDetailTitle = findViewById(R.id.ivBlockOrdersDetailTitle);
        tvBlockOrdersDetailTitle = findViewById(R.id.tvBlockOrdersDetailTitle);
        tvBlockOrdersDetailTips = findViewById(R.id.tvBlockOrdersDetailTips);
        tvBlockOrdersDetailMoney = findViewById(R.id.tvBlockOrdersDetailMoney);
        tvItemSecond = findViewById(R.id.tvItemSecond);
        tvBlockOrdersDetailCount = findViewById(R.id.tvBlockOrdersDetailCount);
        tvBlockOrdersDetailKuanggong = findViewById(R.id.tvBlockOrdersDetailKuanggong);
        tvBlockOrdersDetailTime = findViewById(R.id.tvBlockOrdersDetailTime);
        tvBlockOrdersDetailAddress1 = findViewById(R.id.tvBlockOrdersDetailAddress1);
        tvBlockOrdersDetailAddress2 = findViewById(R.id.tvBlockOrdersDetailAddress2);
        tvBlockOrdersDetailLast1 = findViewById(R.id.tvBlockOrdersDetailLast1);
        tvBlockOrdersDetailLast2 = findViewById(R.id.tvBlockOrdersDetailLast2);
        tvBlockOrdeerDetailBlock = findViewById(R.id.tvBlockOrdeerDetailBlock);
        llBlockOrdeerDetailBlock = findViewById(R.id.llBlockOrdeerDetailBlock);
        tvBlockOrdersDetailCurrency=findViewById(R.id.tvBlockOrdersDetailCurrency);
        tvItemFirst=findViewById(R.id.tvItemFirst);
        ivItemArrow=findViewById(R.id.ivItemArrow);
        collectionAddress=findViewById(R.id.collectionAddress);
    }

    public void back(View view) {
        finish();
    }
}
