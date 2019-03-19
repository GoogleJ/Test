package com.zxjk.duoduo.ui.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.msg.ConstactsNewFriendFragment;

import androidx.appcompat.app.AppCompatActivity;


/**
 * @author Administrator
 * @// TODO: 2019\3\18 0018 Fragment跳转的基础activity 
 */
@SuppressLint("Registered")
public class ContentActivity extends FragmentActivity {
    int i;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        initFragment(i);
    }
    @SuppressLint("ResourceType")
    private void initFragment(int i) {
        this.i = i;
        Intent intent = getIntent();
        int a = intent.getIntExtra("tag", i);
        i = a;
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        switch (i) {
            case 0:
                transaction.add(R.id.m_content,ConstactsNewFriendFragment.newInstance());
                transaction.commit();
                break;
            default:
                break;
        }
    }


}
