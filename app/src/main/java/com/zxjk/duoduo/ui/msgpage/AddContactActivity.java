package com.zxjk.duoduo.ui.msgpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.weight.TitleBar;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Administrator
 * @// TODO: 2019\3\19 0019 添加联系人的界面
 */
public class AddContactActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.m_add_friend_title_bar)
    TitleBar titleBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        initUI();
    }

    @OnClick({R.id.m_add_friend_wechat_btn
            , R.id.m_add_friend_contact_btn
            , R.id.m_my_qr_code_btn
            , R.id.m_add_friend_search_edit
            , R.id.m_add_friend_scan_it_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_add_friend_wechat_btn:
                break;
            case R.id.m_add_friend_contact_btn:

                startActivity(new Intent(this, PhoneContactActivity.class));
                break;
            case R.id.m_add_friend_scan_it_btn:
                startActivity(new Intent(this,QrCodeActivity.class));
                break;
            case R.id.m_my_qr_code_btn:
                MyQrCodeActivity.start(this);
                break;
            case R.id.m_add_friend_search_edit:
                startActivity(new Intent(this, GlobalSearchActivity.class));
                break;
            default:
                break;
        }
    }

    protected void initUI() {
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
