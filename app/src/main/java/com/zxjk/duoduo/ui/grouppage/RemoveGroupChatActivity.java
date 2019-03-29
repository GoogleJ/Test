package com.zxjk.duoduo.ui.grouppage;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CommonUtils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\26 0026 移除群聊 和添加联系人都在这个里头
 */
public class RemoveGroupChatActivity extends BaseActivity {
    ImageView titleLeftImage;
    TextView titleBar;
    TextView titleRight;
    RecyclerView mRecyclerView;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_group_chat);
        titleLeftImage=findViewById(R.id.titleLeftImage);
        titleBar=findViewById(R.id.titleBar);
        titleRight=findViewById(R.id.titleRight);


        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
    }

    /**
     * 返回上一层
     * @param view
     */
    public void titleLeftImageClick(View view ){ finish(); }

    /**
     * titleRight
     * 处理点击请求接口的事件
     */
    public void titleRightClick(View view){
    }

    /**
     * 移除群组
     * @param groupId
     * @param customerIds
     */
     public void moveOutGroup(String groupId,String customerIds){
         ServiceFactory.getInstance().getBaseService(Api.class)
                 .moveOutGroup(groupId,customerIds)
                 .compose(bindToLifecycle())
                 .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                 .compose(RxSchedulers.normalTrans())
                 .subscribe(new Consumer<String>() {
                     @Override
                     public void accept(String s) throws Exception {
                     }
                 },this::handleApiError);
     }

    /**
     * 添加群成员
     * @param groupId
     * @param inviterId
     * @param customerIds
     */
     public void enterGroup(String groupId,String inviterId,String customerIds){
         ServiceFactory.getInstance().getBaseService(Api.class)
                 .enterGroup(groupId,inviterId,customerIds)
                 .compose(bindToLifecycle())
                 .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                 .compose(RxSchedulers.normalTrans())
                 .subscribe(new Consumer<String>() {
                     @Override
                     public void accept(String s) throws Exception {

                     }
                 },this::handleApiError);

     }



}
