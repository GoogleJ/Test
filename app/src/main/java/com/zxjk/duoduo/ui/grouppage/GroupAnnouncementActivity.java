package com.zxjk.duoduo.ui.grouppage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GroupResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;

import androidx.annotation.Nullable;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\26 0026  群公告
 */
@SuppressLint("CheckResult")
public class GroupAnnouncementActivity extends BaseActivity {

    ImageView titleLeftImage;
    TextView titleRight;
    EditText announcementEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_announcement);
        titleLeftImage=findViewById(R.id.title_left_image);
        titleRight=findViewById(R.id.title_right);
        announcementEdit=findViewById(R.id.announcement_edit);
    }

    /**
     * 点击返回的事件
     * @param view
     */
    public void titleLeftImage(View view){finish();}

    /**
     * 处理完成事件
     *
     */
    public void titleRight(View view){
        GroupResponse groupResponse=new GroupResponse();
        if (!TextUtils.isEmpty(announcementEdit.getText().toString())){
            groupResponse.setId(getIntent().getStringExtra("groupId"));
            groupResponse.setGroupSign(announcementEdit.getText().toString());
            updateGroupInfo(GsonUtils.toJson(groupResponse));
        }
        ToastUtils.showShort(getString(R.string.please_input_announcement));
    }
    public void updateGroupInfo(String groupInfo){
        ServiceFactory.getInstance().getBaseService(Api.class)
                .updateGroupInfo(groupInfo)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(groupResponse -> {
                    ToastUtils.showShort(GroupAnnouncementActivity.this.getString(R.string.announcement_edit_successful));
                    GroupAnnouncementActivity.this.finish();
                },this::handleApiError);
    }
}
