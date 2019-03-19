package com.zxjk.duoduo.ui.msg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.base.ContentActivity;
import com.zxjk.duoduo.weight.TitleBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Administrator
 * @// TODO: 2019\3\19 0019 添加还有的界面
 */
public class AddFriendActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.m_add_friend_title_bar)
    TitleBar titleBar;


    public static void start(FragmentActivity activity) {
        Intent intent = new Intent(activity, AddFriendActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        initUI();
    }


    @NonNull


    @OnClick({R.id.m_add_friend_wechat_btn
            ,R.id.m_add_friend_contact_btn
            ,R.id.m_my_qr_code_btn
            ,R.id.m_add_friend_scan_it_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_add_friend_wechat_btn:



                break;
            case R.id.m_add_friend_contact_btn:
                Intent intent=new Intent(this, ContentActivity.class);
                intent.putExtra("tag",2);
                startActivity(intent);

                break;
            case R.id.m_add_friend_scan_it_btn:
                QrCodeActivity.start(this);

                break;
            case R.id.m_my_qr_code_btn:
                MyQrCodeActivity.start(this);
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
}
