package com.zxjk.duoduo.ui.msgpage;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.weight.TitleBar;
/**
 * @author Administrator
 * @// TODO: 2019\4\1 0001 单聊页面
 */
public class ConversationActivity extends FragmentActivity {

    TitleBar titleBar;
    String title;
    String targetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        titleBar = findViewById(R.id.conversation_title);
        initView();

        initRongIM();


    }

    private void initRongIM() {

    }

    private void initView() {
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setTitle(Constant.friendInfoResponse.getNick());

        titleBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
