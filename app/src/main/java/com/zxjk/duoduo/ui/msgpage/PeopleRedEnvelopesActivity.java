package com.zxjk.duoduo.ui.msgpage;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.RedPacketMessage;
import com.zxjk.duoduo.utils.GlideUtil;

import androidx.annotation.Nullable;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * @author Administrator
 * @// TODO: 2019\4\3 0003 领取红包后跳转的页面
 */
public class PeopleRedEnvelopesActivity extends BaseActivity {
    ImageView titleLeftImage;

    ImageView m_people_red_envelopes_header;
    TextView m_people_red_envelopes_user_name_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_red_envelopes);
        titleLeftImage = findViewById(R.id.m_people_red_envelopes_title_bar);
        titleLeftImage.setOnClickListener(v -> finish());
        m_people_red_envelopes_header = findViewById(R.id.m_people_red_envelopes_header);
        m_people_red_envelopes_user_name_text = findViewById(R.id.m_people_red_envelopes_user_name_text);

        Message message = getIntent().getParcelableExtra("msg");
        RedPacketMessage redPacketMessage = (RedPacketMessage) message.getContent();

        UserInfo sender = RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId());

        m_people_red_envelopes_user_name_text.setText(sender.getName() + "的红包");
        GlideUtil.loadCornerImg(m_people_red_envelopes_header, sender.getPortraitUri().toString(), 3);
    }
}
