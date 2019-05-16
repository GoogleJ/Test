package com.zxjk.duoduo.ui.minepage;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.LoginActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Administrator
 */
public class AccountSwitchActivity extends BaseActivity {
    String actionReceiver = "com.zxjk.duoduo.logout";
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_switch);
        ButterKnife.bind(this);
        tvTitle.setText(getString(R.string.account_switch_title));

        initView();
    }

    private void initView() {


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void changeAccountLogin(View view) {
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

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }
}
