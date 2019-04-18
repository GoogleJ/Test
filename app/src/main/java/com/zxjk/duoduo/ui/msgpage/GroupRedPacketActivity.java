package com.zxjk.duoduo.ui.msgpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zxjk.duoduo.R;
import androidx.appcompat.app.AppCompatActivity;

public class GroupRedPacketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_red_packet);

        String groupId = getIntent().getStringExtra("groupId");
    }

    public void jump2List(View view) {
        startActivity(new Intent(this, RedPackageListActivity.class));
    }

    public void back(View view) {
        finish();
    }
}
