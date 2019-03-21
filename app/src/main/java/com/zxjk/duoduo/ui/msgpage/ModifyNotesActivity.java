package com.zxjk.duoduo.ui.msgpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\21 0021 修改备注
 */
public class ModifyNotesActivity extends BaseActivity implements View.OnClickListener {
@BindView(R.id.m_modify_notes_edit)
    EditText modifyNotesEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_notes);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.m_add_btn_cancel,R.id.m_title_bar_right})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.m_add_btn_cancel:
                finish();
                break;
            case R.id.m_title_bar_right:
                Intent intent=getIntent();
                String userId=intent.getStringExtra("peopelUserId");
                updateRemark(userId,modifyNotesEdit.getText().toString());

                break;
        }
    }
    public void updateRemark(String friendId,String remark){
        ServiceFactory.getInstance().getBaseService(Api.class)
                .updateRemark(friendId, remark)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<FriendInfoResponse>() {
                    @Override
                    public void accept(FriendInfoResponse friendInfoResponse) throws Exception {
                        ToastUtils.showShort("修改成功");
                        finish();

                    }
                },this::handleApiError);

    }

}
