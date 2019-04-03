package com.zxjk.duoduo.ui.msgpage;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.weight.TitleBar;
import androidx.annotation.Nullable;

/**
 * @author Administrator
 * @// TODO: 2019\4\3 0003 转账详情
 */
public class TransferInfoActivity extends BaseActivity {
    TitleBar titleBar;
    TextView commitBtn;

    public static void start(Activity activity){
        Intent intent=new Intent(activity,TransferInfoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_info);
        titleBar=findViewById(R.id.m_transfer_info_title_bar);
        commitBtn=findViewById(R.id.m_transfer_info_commit_btn);
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
