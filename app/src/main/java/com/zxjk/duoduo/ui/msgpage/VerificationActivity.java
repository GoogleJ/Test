package com.zxjk.duoduo.ui.msgpage;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.BaseResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.weight.TitleBar;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020  发送验证
 */
public class VerificationActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.m_verification_title_bar)
    TitleBar titleBar;
    @BindView(R.id.m_verification_edit)
    EditText verificationEdit;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
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

    @OnClick({R.id.m_verification_icon, R.id.m_verification_send_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_verification_send_btn:
                int type=0;
                int intentType=getIntent().getIntExtra("intentType",type);
                String conversationForAdd=getIntent().getStringExtra("ConversationForAdd");

                String newFriend=getIntent().getStringExtra("addFriend");
                if (intentType==0){
                    applyAddFriend(conversationForAdd, verificationEdit.getText().toString());
                    return;
                }else{
                    applyAddFriend(newFriend,verificationEdit.getText().toString());
                }
                break;
            case R.id.m_verification_icon:

                verificationEdit.setText("");
                break;
            default:
                break;
        }
    }

    public void applyAddFriend(String friendId, String remark) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .applyAddFriend(friendId, remark)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .subscribe(new Consumer<BaseResponse<String>>() {
                    @Override
                    public void accept(BaseResponse<String> listBaseResponse) throws Exception {
                        ToastUtils.showShort("添加成功");
                        finish();
                    }
                }, this::handleApiError);

    }


}
