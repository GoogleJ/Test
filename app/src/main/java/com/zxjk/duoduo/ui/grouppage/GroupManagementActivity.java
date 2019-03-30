package com.zxjk.duoduo.ui.grouppage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GroupChatResponse;
import com.zxjk.duoduo.network.response.GroupResponse;
import com.zxjk.duoduo.ui.base.BaseActivity;

import androidx.annotation.Nullable;

/**
 * @author Administrator
 * @// TODO: 2019\3\26 0026 群管理
 */
public class GroupManagementActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_management);
    }

    public void transferGroup(View view) {

        startActivity(new Intent(GroupManagementActivity.this, ChooseNewOwnerActivity.class));
    }
}
