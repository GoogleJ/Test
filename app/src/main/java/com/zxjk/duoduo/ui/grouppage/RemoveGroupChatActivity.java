package com.zxjk.duoduo.ui.grouppage;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.AllGroupMembersResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.adapter.AddOrDelAdapter;
import com.zxjk.duoduo.ui.grouppage.adapter.GroupAddOrRemoveAdapter;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

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
    RecyclerView selectRecyclerView;


    AddOrDelAdapter topAdapter;
    GroupAddOrRemoveAdapter mAdapter;
    List<AllGroupMembersResponse> lists=new ArrayList<>();
    List<AllGroupMembersResponse> list=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_group_chat);
        titleLeftImage = findViewById(R.id.titleLeftImage);
        titleBar = findViewById(R.id.titleBar);
        titleRight = findViewById(R.id.titleRight);
        mRecyclerView = findViewById(R.id.recycler_view);
        selectRecyclerView=findViewById(R.id.recycler_view_select);


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new GroupAddOrRemoveAdapter();
        String groupId = getIntent().getStringExtra("groupId");
        getGroupMemByGroupId(groupId);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            CheckBox checkBox = view.findViewById(R.id.selected_delete);
            selectRecyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            selectRecyclerView.setLayoutManager(linearLayoutManager);
            topAdapter=new AddOrDelAdapter();

            AllGroupMembersResponse response=null;
            for (AllGroupMembersResponse friendInfoResponse : list) {

                if (list.get(position).getId().equals(friendInfoResponse.getId())) {
                    response=new AllGroupMembersResponse();

                    response.setId(friendInfoResponse.getId());
                    response.setHeadPortrait(friendInfoResponse.getHeadPortrait());
                    lists.add(response);
                }
            }
            topAdapter.setNewData(lists);

            selectRecyclerView.setAdapter(topAdapter);

            topAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if (topAdapter.getData().size()>=0){
                        topAdapter.getData().remove(position);
                        topAdapter.notifyDataSetChanged();
                        mAdapter.notifyDataSetChanged();
                    }else{
                        return;
                    }
                }
            });

        });

    }

    /**
     * 返回上一层
     *
     * @param view
     */
    public void titleLeftImageClick(View view) {
        finish();
    }

    /**
     * titleRight
     * 处理点击请求接口的事件
     */
    public void titleRightClick(View view) {


        //移除群组
        StringBuffer sb=new StringBuffer();
        for (int i=0;i<lists.size();i++){
            sb.append(lists.get(i).getId());
            sb.append(",");
        }
            titleBar.setText(getString(R.string.remove_group_chat));
            moveOutGroup(Constant.groupId,sb.substring(0,sb.length()-1));

    }

    /**
     * 移除群组
     *
     * @param groupId
     * @param customerIds
     */
    public void moveOutGroup(String groupId, String customerIds) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .moveOutGroup(groupId, customerIds)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ToastUtils.showShort(R.string.del_group_members);
                    }
                }, this::handleApiError);
    }



    /**
     * 查询群成员
     *
     * @param groupId
     */
    public void getGroupMemByGroupId(String groupId) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getGroupMemByGroupId(groupId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<List<AllGroupMembersResponse>>() {
                    @Override
                    public void accept(List<AllGroupMembersResponse> allGroupMembersResponses) throws Exception {
                        mAdapter.setNewData(allGroupMembersResponses);
                        list = allGroupMembersResponses;
                    }
                }, this::handleApiError);
    }


}
