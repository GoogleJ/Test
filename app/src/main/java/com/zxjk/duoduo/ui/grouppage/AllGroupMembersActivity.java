package com.zxjk.duoduo.ui.grouppage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.AllGroupMembersResponse;
import com.zxjk.duoduo.network.response.GroupResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.adapter.AllGroupMemebersAdapter;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.weight.TitleBar;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\26 0026 全部群成员
 */
@SuppressLint("CheckResult")
public class AllGroupMembersActivity extends BaseActivity {
    TitleBar titleBar;
    EditText searchEdit;
    RecyclerView mRecyclerView;

    AllGroupMemebersAdapter mAdapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_group_members);
        titleBar=findViewById(R.id.title_bar);
        searchEdit=findViewById(R.id.search_edit);
        mRecyclerView=findViewById(R.id.recycler_view);
   initView();
    }

    private void initView() {
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,5);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter=new AllGroupMemebersAdapter();
        GroupResponse groupResponse= (GroupResponse) getIntent().getSerializableExtra("groupId");
        getGroupMemByGroupId(groupResponse.getId());
        mRecyclerView.setAdapter(mAdapter);
    }

    public void getGroupMemByGroupId(String groupId){
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getGroupMemByGroupId(groupId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<List<AllGroupMembersResponse>>() {
                    @Override
                    public void accept(List<AllGroupMembersResponse> allGroupMembersResponses) throws Exception {
                        mAdapter.setNewData(allGroupMembersResponses);
                    }
                },this::handleApiError);
    }
}
