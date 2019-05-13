package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GetGroupRedPackageInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.RedPacketMessage;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;

import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

public class PeopleRedEnvelopesActivity extends BaseActivity {
    ImageView titleLeftImage;

    ImageView m_people_red_envelopes_header;
    TextView m_people_red_envelopes_user_name_text;
    TextView m_people_red_envelopes_money_text;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_red_envelopes);

        boolean fromGroup = getIntent().getBooleanExtra("fromGroup", false);

        titleLeftImage = findViewById(R.id.m_people_red_envelopes_title_bar);
        m_people_red_envelopes_header = findViewById(R.id.m_people_red_envelopes_header);
        m_people_red_envelopes_user_name_text = findViewById(R.id.m_people_red_envelopes_user_name_text);
        m_people_red_envelopes_money_text = findViewById(R.id.m_people_red_envelopes_money_text);
        titleLeftImage.setOnClickListener(v -> finish());

        Message message = getIntent().getParcelableExtra("msg");
        RedPacketMessage redPacketMessage = (RedPacketMessage) message.getContent();
        UserInfo sender = RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId());
        m_people_red_envelopes_user_name_text.setText(sender.getName() + "的红包");
        GlideUtil.loadCornerImg(m_people_red_envelopes_header, sender.getPortraitUri().toString(), 5);

        if (fromGroup) {
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getGroupRedPackageInfo(redPacketMessage.getRedId())
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .compose(RxSchedulers.normalTrans())
                    .subscribe(response -> {
                        for (GetGroupRedPackageInfoResponse.CustomerInfoBean bean : response.getCustomerInfo()) {
                            if (String.valueOf(bean.getCustomerId()).equals(Constant.userId)) {
                                m_people_red_envelopes_money_text.setText(String.valueOf(bean.getMoney()));
                            }
                        }
                    }, this::handleApiError);
        } else {
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .personalRedPackageInfo(redPacketMessage.getRedId(), Integer.parseInt(Constant.userId))
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .compose(RxSchedulers.normalTrans())
                    .subscribe(response -> m_people_red_envelopes_money_text.setText(response.getRedPachageInfo().getMoney()), this::handleApiError);
        }
    }
}
