package com.zxjk.duoduo.ui.grouppage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

import androidx.annotation.Nullable;

/**
 * @author Administrator
 * @// TODO: 2019\3\26 0026 群组--聊天信息页面
 */
public class GroupChatInformationActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_information);
    }

    /**
     * 跳转群公告
     */
    public void announcement(View view) {
        startActivity(new Intent(this, GroupAnnouncementActivity.class));
    }

    /**
     * 查看全部群成员
     * @param view
     */
    public void groupAllMembers(View view){
        startActivity(new Intent(this,AllGroupMembersActivity.class));
    }
    /**
     * 管理群
     * @param view
     */
    public void groupManagement(View view){
        startActivity(new Intent(this,GroupManagementActivity.class));

    }
}
