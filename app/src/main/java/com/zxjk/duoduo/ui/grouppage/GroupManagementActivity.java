package com.zxjk.duoduo.ui.grouppage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GroupManagementActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private String groupId;
    private boolean isGameGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_management);
        ButterKnife.bind(this);
        initView();
        tvTitle.setText(getString(R.string.group_management));

    }

    private void initView() {
        groupId = getIntent().getStringExtra("groupId");
        isGameGroup = getIntent().getBooleanExtra("isGameGroup", false);

    }


    @OnClick({R.id.rl_back, R.id.rl_groupTransfer})
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.rl_back:
                finish();
                break;
            //群管理
            case R.id.rl_groupTransfer:
                if (!isGameGroup) {
                    Intent intent = new Intent(GroupManagementActivity.this, ChooseNewOwnerActivity.class);
                    intent.putExtra("groupId", groupId);
                    startActivity(intent);
                } else {
                    ToastUtils.showShort(getString(R.string.non_transferable));
                }

                break;
        }
    }
}
