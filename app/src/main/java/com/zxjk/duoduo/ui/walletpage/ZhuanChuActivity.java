package com.zxjk.duoduo.ui.walletpage;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

import java.text.DecimalFormat;

public class ZhuanChuActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar seekZhuanchu;
    private TextView tvKuanggongPrice;
    private TextView tvGwei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuan_chu);

        seekZhuanchu = findViewById(R.id.seekZhuanchu);
        tvKuanggongPrice = findViewById(R.id.tvKuanggongPrice);
        tvGwei = findViewById(R.id.tvGwei);

        seekZhuanchu.setMax(1000);
        seekZhuanchu.setOnSeekBarChangeListener(this);
    }

    public void submit(View view) {

    }

    public void back(View view) {
        finish();
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tvKuanggongPrice.setText("≈" + new DecimalFormat("#.0000").format((progress/1000f * 0.006)) + " ether");
        tvGwei.setText(new DecimalFormat("#.00").format((progress/1000f * 100)) + " gwei");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
