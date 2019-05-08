package com.zxjk.duoduo.ui.grouppage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

/**
 * author L
 * create at 2019/5/80
 * description: 群管理
 */
public class GroupManagementActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_management);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.group_management));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
    }

    public void transferGroup(View view) {
        Intent intent = new Intent(GroupManagementActivity.this, ChooseNewOwnerActivity.class);
        intent.putExtra("groupId", getIntent().getStringExtra("groupId"));
        startActivity(intent);
    }
}
