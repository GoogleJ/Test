package com.zxjk.duoduo.ui.msgpage;

import androidx.fragment.app.FragmentActivity;
import io.rong.imkit.RongIM;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jrmf360.rylib.JrmfClient;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.RedPackageResponse;
import com.zxjk.duoduo.weight.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

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
        titleBar=findViewById(R.id.conversation_title);
        initView();
//        Uri uri = getIntent().getData();
        //获取 title
//        title = uri.getQueryParameter("title").toString();
//        targetId = uri.getQueryParameter("targetId").toString();



    }

    private void initView() {
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setTitle("用户");
        titleBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
