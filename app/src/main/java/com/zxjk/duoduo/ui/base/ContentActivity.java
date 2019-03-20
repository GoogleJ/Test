package com.zxjk.duoduo.ui.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.msgpage.ConstactsNewFriendFragment;
import com.zxjk.duoduo.ui.msgpage.ContactFragment;
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
            case 0:
//                //添加联系人界面
//                transaction.add(R.id.m_content, PersonalInformationFragment.newInstance());
//                transaction.commit();
                break;
//            case 1:
//                //验证申请界面
//
//                transaction.add(R.id.m_content, VerificationActivity.newInstance());
//                transaction.commit();
//                break;
            case 2:
                //这个是手机联系人界面
//                transaction.remove(VerificationActivity.newInstance());
                transaction.replace(R.id.m_content, ContactFragment.newInstance());
                transaction.commit();
                break;
            case 3:
                //这个是手机通讯录页面
                transaction.remove(ContactFragment.newInstance());
                transaction.replace(R.id.m_content, ConstactsNewFriendFragment.newInstance());
                transaction.commit();
                break;
//            case 4:
//                //这个是新的朋友的Fragment界面
//                transaction.remove(ConstactsNewFriendFragment.newInstance());
//                transaction.replace(R.id.m_content, NewFriendFragment.newInstance());
//                transaction.commit();
//                break;
            default:
                break;
        }
    }
}
