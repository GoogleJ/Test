package com.zxjk.duoduo.ui.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.EditPersonalInformationFragment;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 * @author Administrator
 * @// TODO: 2019\3\18 0018 Fragment跳转的基础activity 
 */
@SuppressLint("Registered")
public class ContentActivity extends BaseActivity {
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
//            case 0:
//                transaction.add(R.id.m_content, EditPersonalInformationFragment.newInstance());
//                transaction.commit();
//                break;
            default:
                break;
        }
    }


}
