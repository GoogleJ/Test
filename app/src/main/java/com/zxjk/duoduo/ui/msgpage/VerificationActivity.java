package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author L
 * create at 2019/5/10
 * description: 验证申请
 */
@SuppressLint("CheckResult")
public class VerificationActivity extends BaseActivity {

    @BindView(R.id.m_verification_edit)
    EditText verificationEdit;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    int intentType;
    String conversationForAdd;
    String newFriend;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        tvTitle.setText(getString(R.string.m_verification_title_bar));
        int type = 0;
        intentType = getIntent().getIntExtra("intentType", type);
        conversationForAdd = getIntent().getStringExtra("ConversationForAdd");
        newFriend = getIntent().getStringExtra("addFriend");
    }


    public void applyAddFriend(String friendId, String remark) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .applyAddFriend(friendId, remark)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(listBaseResponse -> {
                    KeyboardUtils.hideSoftInput(this);
                    ToastUtils.showShort(getString(R.string.has_bean_sent));
                    finish();
                }, this::handleApiError);
    }

    @OnClick({R.id.rl_back, R.id.m_verification_icon, R.id.m_verification_send_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.m_verification_icon:
                verificationEdit.setText("");
                break;
            case R.id.m_verification_send_btn:
                if (intentType == 0) {
                    applyAddFriend(conversationForAdd, verificationEdit.getText().toString());
                } else {
                    applyAddFriend(newFriend, verificationEdit.getText().toString());
                }
                break;
        }
    }
}
