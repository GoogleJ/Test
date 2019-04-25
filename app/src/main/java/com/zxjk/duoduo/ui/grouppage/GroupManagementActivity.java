package com.zxjk.duoduo.ui.grouppage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.TitleBar;

/**
 * @author Administrator
 */
public class GroupManagementActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_management);

        TitleBar tittle = findViewById(R.id.tittle);
        tittle.setLeftLayoutClickListener(v -> finish());
    }

    public void transferGroup(View view) {
        Intent intent = new Intent(GroupManagementActivity.this, ChooseNewOwnerActivity.class);
        intent.putExtra("groupId", getIntent().getStringExtra("groupId"));
        startActivity(intent);
    }
}
