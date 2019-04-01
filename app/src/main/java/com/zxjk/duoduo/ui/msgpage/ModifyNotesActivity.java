package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Administrator
 * @// TODO: 2019\3\21 0021 修改备注
 */
@SuppressLint("CheckResult")
public class ModifyNotesActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.m_modify_notes_edit)
    EditText modifyNotesEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_notes);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.m_add_btn_cancel, R.id.m_title_bar_right})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_add_btn_cancel:
                finish();
                break;
            case R.id.m_title_bar_right:
//                updateRemark(Constant.friendInfoResponse.getId(), modifyNotesEdit.getText().toString());
                break;
                default:
                    break;
        }
    }


    public void updateRemark(String friendId, String remark) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .updateRemark(friendId, remark)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendInfoResponse -> {
                    ToastUtils.showShort(getString(R.string.update_success));
                    finish();

                }, this::handleApiError);

    }

}
