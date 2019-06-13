package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.bean.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.UserInfo;

/**
 * author L
 * create at 2019/5/10
 * description: 备注
 */
@SuppressLint("CheckResult")
public class ModifyNotesActivity extends BaseActivity {
    @BindView(R.id.m_modify_notes_edit)
    EditText modifyNotesEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_notes);
        ButterKnife.bind(this);
        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_commit = findViewById(R.id.tv_commit);
        tv_title.setText(getString(R.string.note));
        tv_commit.setVisibility(View.VISIBLE);
        tv_commit.setText(getString(R.string.queding));
        tv_commit.setOnClickListener(v -> {
            String s = modifyNotesEdit.getText().toString();
            if (TextUtils.isEmpty(s)) {
                ToastUtils.showShort(R.string.input_skin);
                return;
            }
            updateRemark(getIntent().getStringExtra("friendId"), s);
        });
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
    }

    public void updateRemark(String friendId, String remark) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .updateRemark(friendId, remark)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(r -> {
                    RongUserInfoManager.getInstance().setUserInfo(new UserInfo(r.getId(),
                            remark, Uri.parse(r.getHeadPortrait())));
                    ToastUtils.showShort(getString(R.string.successfully_modified));
                    for (FriendInfoResponse f : Constant.friendsList) {
                        if (f.getId().equals(friendId)) {
                            f.setRemark(remark);
                            break;
                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra("remark", remark);
                    setResult(1, intent);
                    finish();
                }, this::handleApiError);
    }
}
