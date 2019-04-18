package com.zxjk.duoduo.ui.msgpage;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.SharePopWindow;
import com.zxjk.duoduo.ui.widget.TitleBar;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Administrator
 */
public class AddContactActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.m_add_friend_title_bar)
    TitleBar titleBar;

    View m_add_friend_contact_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        ButterKnife.bind(this);
        initUI();

        m_add_friend_contact_btn = findViewById(R.id.m_add_friend_contact_btn);
        getPermisson(m_add_friend_contact_btn, result -> {
            if (result) startActivity(new Intent(this, PhoneContactActivity.class));
        }, Manifest.permission.READ_CONTACTS);

        getPermisson(findViewById(R.id.m_add_friend_scan_it_btn), result -> {
            if (result) startActivity(new Intent(this, QrCodeActivity.class));
        }, Manifest.permission.CAMERA);
    }

    @OnClick({R.id.m_add_friend_wechat_btn
            , R.id.m_my_qr_code_btn
            , R.id.m_add_friend_search_edit
            , R.id.m_add_friend_scan_it_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_add_friend_wechat_btn:
                new SharePopWindow(this)
                        .showPopupWindow();
                break;
            case R.id.m_my_qr_code_btn:
                MyQrCodeActivity.start(this);
                break;
            case R.id.m_add_friend_search_edit:
                startActivity(new Intent(this, GlobalSearchActivity.class));
                break;
            default:
        }
    }

    private void initUI() {
        titleBar.getLeftImageView().setOnClickListener(v -> finish());
    }
}
