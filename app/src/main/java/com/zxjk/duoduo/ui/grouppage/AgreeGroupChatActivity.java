package com.zxjk.duoduo.ui.grouppage;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GroupResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.TitleBar;
import com.zxjk.duoduo.utils.CommonUtils;

import androidx.annotation.Nullable;

/**
 * @author Administrator
 */
public class AgreeGroupChatActivity extends BaseActivity {
    ImageView groupHeader;
    TextView groupName;
    TextView pleaseJoinGroup;
    TextView joinGroupBtn;
    GroupResponse groupResponse;
    TitleBar titleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_agree_group_chat);
        groupHeader = findViewById(R.id.group_headers);
        groupName = findViewById(R.id.group_chat_name);
        pleaseJoinGroup = findViewById(R.id.invite_to_group_chat);
        joinGroupBtn = findViewById(R.id.join_a_group_chat);
        titleBar=findViewById(R.id.title_bar);
        groupResponse = (GroupResponse) getIntent().getSerializableExtra("groupId");
        groupName.setText(groupResponse.getGroupInfo().getGroupNikeName());

        pleaseJoinGroup.setText(groupResponse.getGroupInfo().getGroupOwnerId());
        titleBar.getRightImageView().setOnClickListener(v -> finish());
        joinGroupBtn.setOnClickListener(v -> {
            StringBuffer sb=new StringBuffer();
            sb.append(Constant.userId);
            sb.append(",");
            sb.append(groupResponse.getGroupInfo().getId());
            enterGroup(groupResponse.getGroupInfo().getId(), Constant.userId,sb.substring(0,sb.length()-1));
        });
    }

    public void enterGroup(String groupId,String inviterId,String customerIds){
        ServiceFactory.getInstance().getBaseService(Api.class)
                .enterGroup(groupId,inviterId,customerIds)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> LogUtils.d("DEBUG",s),this::handleApiError);

    }
}
