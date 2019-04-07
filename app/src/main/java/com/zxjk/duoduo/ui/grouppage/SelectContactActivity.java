package com.zxjk.duoduo.ui.grouppage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.adapter.AddGroupTopAdapter;
import com.zxjk.duoduo.ui.grouppage.adapter.SelectContactAdapter;
import com.zxjk.duoduo.utils.CommonUtils;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\28 0028 选择联系人
 */
@SuppressLint("CheckResult")
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

    private boolean fromZhuanChu;


    int position;
    List<FriendInfoResponse> lists=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);

        fromZhuanChu = getIntent().getBooleanExtra("fromZhuanChu", false);
        initView();
    }
    List<FriendInfoResponse> list = new ArrayList<>();
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
            if (fromZhuanChu) {
                String walletAddress = list.get(position).getWalletAddress();
                Intent intent = new Intent();
                intent.putExtra("walletAddress", walletAddress);
                setResult(3, intent);
                finish();
                return;
            }
            selectRecycler.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SelectContactActivity.this);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            selectRecycler.setLayoutManager(linearLayoutManager);
            topAdapter = new AddGroupTopAdapter();
            this.position = position;
            FriendInfoResponse response;
            for (FriendInfoResponse friendInfoResponse : list) {

                if (list.get(position).getId().equals(friendInfoResponse.getId())) {
                    response=new FriendInfoResponse();

                    response.setId(friendInfoResponse.getId());
                    response.setHeadPortrait(friendInfoResponse.getHeadPortrait());
                    lists.add(response);
                }
            }
            topAdapter.setNewData(lists);

            selectRecycler.setAdapter(topAdapter);

            topAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if (topAdapter.getData().size()>=0){
                        topAdapter.getData().remove(position);
                        mAdapter.notifyDataSetChanged();
                        topAdapter.notifyDataSetChanged();
                    }else{
                        return;
                    }

                }
            });
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_image:
                finish();
                break;
            case R.id.title_right:
                int types=0;
                int type=getIntent().getIntExtra("addGroupType",types);

                    StringBuffer sb=new StringBuffer();
                    for (int i=0;i<lists.size();i++){
                        sb.append(lists.get(i).getId());
                        sb.append(",");
                    }
                if (type==0){
                    makeGroup(Constant.userId,Constant.userId+","+sb.substring(0,sb.length()-1));
                }else{
                    enterGroup(getIntent().getStringExtra("groupId"),Constant.userId,sb.substring(0,sb.length()-1));
                }
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
                .subscribe(new Consumer<List<FriendInfoResponse>>() {
                    @Override
                    public void accept(List<FriendInfoResponse> friendInfoResponses) throws Exception {
                        mAdapter.setNewData(friendInfoResponses);
                        list = friendInfoResponses;
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
                .subscribe(s -> {
                    SelectContactActivity.this.finish();
                    Intent intent=new Intent(SelectContactActivity.this,AgreeGroupChatActivity.class);
                    intent.putExtra("groupId",s.getId());
                    startActivity(intent);
                },this::handleApiError);
    }

    /**
     * 关于同意添加群的实现
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
                        ToastUtils.showShort(getString(R.string.add_group_chat));
                    }
                },this::handleApiError);

    }
}
