package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.SearchAdapter;
import com.zxjk.duoduo.ui.widget.TitleBar;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 搜索--添加好友，搜索范围，全局
 */
@SuppressLint("CheckResult")
public class SearchActivity extends BaseActivity {
    @BindView(R.id.m_fragment_search_title_bar)
    TitleBar titleBar;
    @BindView(R.id.m_search_edit)
    EditText searchEdit;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    SearchAdapter mAdapter;
    Intent intent;
    FriendInfoResponse friendInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initData();
        initUI();
    }

    private void initData() {
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //搜索按键action
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(searchEdit.getText().toString())){
                        searchFriendInfo(searchEdit.getText().toString());
                    }else{
                        ToastUtils.showShort(getString(R.string.input_search_edit));
                    }
                    return true;
                }
                return false;
            }
        });

    }

    private void initUI() {
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new SearchAdapter();
        mRecyclerView.setAdapter(mAdapter);
        getFriendListInfoById();
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            FriendInfoResponse friendInfoResponse=mAdapter.getData().get(position);
            if (friendInfo.getId().equals(friendInfoResponse.getId())){
                intent=new Intent(SearchActivity.this,FriendDetailsActivity.class);
                intent.putExtra("searchFriendDetails",friendInfoResponse);
                intent.putExtra("intentType",0);
                startActivity(intent);
                return;
            }
        });
        mAdapter.notifyDataSetChanged();

    }

    @SuppressLint("CheckResult")
    public void searchFriendInfo(String data) {
        //模糊搜索用户
        ServiceFactory.getInstance().getBaseService(Api.class)
                .searchFriend(data)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(data1 -> {
                    //此处是进行比较，如果搜索出来的数据在好友列表中已存在，那么我们添加到我们的适配器中，如果不存在，我们就删除这个好友
                    for (int i=0;i<data1.size();i++){
                        if (friendInfo.getId().equals(data1.get(i).getId())&&!data1.get(i).getId().equals(Constant.userId)){
                            mAdapter.addData(data1.get(i));
                        }else{
                            if (data1.size()>=0){
                                data1.remove(i);
                                ToastUtils.showShort(getString(R.string.please_add_friend));
                            }


                        }
                    }
                }, this::handleApiError);
    }
    public void getFriendListInfoById() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendListById()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendInfoResponses -> {
                    for(int i=0;i<friendInfoResponses.size();i++){
                        //获取好友列表中的数据，跟已有的数据进行比对
                        friendInfo=new FriendInfoResponse(friendInfoResponses.get(i));
                    }
                }, this::handleApiError);

    }

}
