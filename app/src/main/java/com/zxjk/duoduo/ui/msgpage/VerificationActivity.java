package com.zxjk.duoduo.ui.msgpage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.BaseResponse;
import com.zxjk.duoduo.network.response.FriendListResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.weight.TitleBar;

import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020  发送验证
 */
public class VerificationActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.m_verification_title_bar)
    TitleBar titleBar;
    @BindView(R.id.m_verification_edit)
    EditText verificationEdit;
int addFriendType=0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_verification);
        ButterKnife.bind(this);
        initUI();
    }

    protected void initUI() {
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @OnClick({R.id.m_verification_icon,R.id.m_verification_send_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.m_verification_send_btn:
               String searchFriendId=getIntent().getStringExtra("searchAddFriendId");
               String globalAddUserId=getIntent().getStringExtra("globalAddUserId");
               int type=getIntent().getIntExtra("addFriendType",addFriendType);
               if (type==0){
                   applyAddFriend(globalAddUserId,verificationEdit.getText().toString());
               }else{
                   applyAddFriend(searchFriendId,verificationEdit.getText().toString());
               }

                break;
            case R.id.m_verification_icon:

                verificationEdit.setText("");
                break;
                default:
                    break;
        }
    }
    public void applyAddFriend(String friendId,String remark){
        ServiceFactory.getInstance().getBaseService(Api.class)
                .applyAddFriend(friendId,remark)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .subscribe(new Consumer<BaseResponse<String>>() {
                    @Override
                    public void accept(BaseResponse<String> listBaseResponse) throws Exception {
                        ToastUtils.showShort("添加成功");
                        finish();


                    }
                },this::handleApiError);

    }


}
