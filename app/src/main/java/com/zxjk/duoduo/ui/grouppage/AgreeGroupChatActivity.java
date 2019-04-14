package com.zxjk.duoduo.ui.grouppage;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
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
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.ui.widget.TitleBar;

import androidx.annotation.Nullable;
import io.reactivex.functions.Consumer;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author Administrator
 * @// TODO: 2019\3\28 0028  同意加入群聊
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
        requestStoragePermission();
        groupHeader = findViewById(R.id.group_headers);
        groupName = findViewById(R.id.group_chat_name);
        pleaseJoinGroup = findViewById(R.id.invite_to_group_chat);
        joinGroupBtn = findViewById(R.id.join_a_group_chat);
        titleBar=findViewById(R.id.title_bar);
        groupResponse = (GroupResponse) getIntent().getSerializableExtra("groupId");
        groupName.setText(groupResponse.getGroupNikeName());

        pleaseJoinGroup.setText(groupResponse.getGroupOwnerId());
        titleBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        joinGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer sb=new StringBuffer();
                sb.append(Constant.userId);
                sb.append(",");
                sb.append(groupResponse.getId());
                enterGroup(groupResponse.getId(), Constant.userId,sb.substring(0,sb.length()-1));
            }
        });


    }

    private void requestStoragePermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
//            loadWechatBitmap(groupHeader, 9);
        } else {
            EasyPermissions.requestPermissions(this, "need storage permission", 1000, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

//    private void loadWechatBitmap(ImageView imageView, int count) {
//        CombineBitmap.init(this)
//                .setLayoutManager(new WechatLayoutManager())
//                .setSize(180)
//                .setGap(3)
//                .setGapColor(Color.parseColor("#E8E8E8"))
//                .setUrls(groupResponse.getGroupHeadPortrait())
//                .setImageView(imageView)
//                .build();
//    }

    public void enterGroup(String groupId,String inviterId,String customerIds){
        ServiceFactory.getInstance().getBaseService(Api.class)
                .enterGroup(groupId,inviterId,customerIds)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtils.d("DEBUG",s);
                    }
                },this::handleApiError);

    }


}
