package com.zxjk.duoduo.ui.minepage;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.LoginActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.weight.TitleBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * @author Administrator
 * @// TODO: 2019\3\23 0023 切换账号登录
 */
public class AccountSwitchActivity extends BaseActivity {
    TitleBar titleBar;
    String actionReceiver = "com.zxjk.duoduo.logout";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_switch);
        titleBar=findViewById(R.id.account_switch_title);
        initView();
    }

    private void initView() {
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void  changeAccountLogin(View view){
        ServiceFactory.getInstance().getBaseService(Api.class)
                .loginOut()
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    ToastUtils.showShort(R.string.login_out);
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }, this::handleApiError);
    }
}
