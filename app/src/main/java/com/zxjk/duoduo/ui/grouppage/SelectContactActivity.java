package com.zxjk.duoduo.ui.grouppage;

import android.app.Service;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.SelectContactBean;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.BaseResponse;
import com.zxjk.duoduo.network.response.FriendListResponse;
import com.zxjk.duoduo.network.response.GroupResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.adapter.AddGroupTopAdapter;
import com.zxjk.duoduo.ui.grouppage.adapter.SelectContactAdapter;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.functions.Consumer;
import razerdp.util.log.LogTag;

/**
 * @author Administrator
 * @// TODO: 2019\3\28 0028 选择联系人
 */
public class SelectContactActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 确定按钮
     */
    TextView titleRight;
    /**
     * 返回按钮
     */
    ImageView titleBarLeftIamge;
    /**
     * 选中后在上方展示的RecyclerView
     */
    RecyclerView selectRecycler;
    /**
     * 选中添加的群成员
     */
    RecyclerView recyclerView;
    /**
     * 搜索好友
     *
     * @param savedInstanceState
     */
    EditText searchEdit;
    SelectContactAdapter mAdapter;
    AddGroupTopAdapter topAdapter;


    int position;
    List<FriendListResponse> lists=new ArrayList<>();
    List<FriendListResponse> listss;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        initView();

    }

    List<FriendListResponse> list = new ArrayList<>();

    private void initView() {
        titleBarLeftIamge = findViewById(R.id.title_left_image);
        titleRight = findViewById(R.id.title_right);
        selectRecycler = findViewById(R.id.recycler_view_select);
        recyclerView = findViewById(R.id.all_members_recycler_view);
        searchEdit = findViewById(R.id.search_select_contact);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        mAdapter = new SelectContactAdapter();
        getFriendListById();
        recyclerView.setAdapter(mAdapter);
        titleBarLeftIamge.setOnClickListener(this);
        titleRight.setOnClickListener(this);


        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            View v = (View) view.getParent();
            ImageView selectImage = view.findViewById(R.id.selected_delete);
            boolean isTrue = true;
            if (isTrue) {
                selectImage.setImageResource(R.drawable.icon_document_selection_no);
                isTrue = false;

            } else {
                selectImage.setImageResource(R.drawable.icon_document_selection_successful);

            }


            selectRecycler.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SelectContactActivity.this);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            selectRecycler.setLayoutManager(linearLayoutManager);
            topAdapter = new AddGroupTopAdapter();
            this.position = position;
            FriendListResponse response = null;
            listss    = new ArrayList<>();
            for (FriendListResponse friendListResponse : list) {

                if (list.get(position).getId().equals(friendListResponse.getId())) {
                    response=new FriendListResponse();

                    response.setId(friendListResponse.getId());
                    response.setHeadPortrait(friendListResponse.getHeadPortrait());
                    lists.add(response);
                }
                if (list.get(position).getId().equals(lists.get(position).getId())){
                    return;
                }

            }
            topAdapter.setNewData(lists);
            selectRecycler.setAdapter(topAdapter);

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_image:
                finish();
                break;
            case R.id.title_right:
                StringBuffer sb=new StringBuffer();
                for (int i=0;i<lists.size();i++){
                    sb.append(lists.get(i).getId());
                    sb.append(",");
                    Log.d("DEBUGSSS",""+sb.substring(0,sb.length()-1));
                }
                makeGroup(Constant.userId,Constant.userId+","+sb.substring(0,sb.length()-1));

                break;
            default:
                break;
        }

    }

    /**
     * 获取好友列表
     */
    public void getFriendListById() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendListById()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<List<FriendListResponse>>() {
                    @Override
                    public void accept(List<FriendListResponse> friendListResponses) throws Exception {
                        mAdapter.setNewData(friendListResponses);
                        LogUtils.d("DEBUG", "成功");
                        list = friendListResponses;

                    }
                }, this::handleApiError);
    }

    /**
     * 创建群
     */
    public void makeGroup(String groupOwnerId, String customerIds) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .makeGroup(groupOwnerId,customerIds)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<GroupResponse>() {
                    @Override
                    public void accept(GroupResponse s) throws Exception {
                        LogUtils.d("DEBUG", s);
                        finish();
                    }
                },this::handleApiError);

    }



}
