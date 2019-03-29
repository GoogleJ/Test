package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendListResponse;
import com.zxjk.duoduo.network.response.SearchCustomerInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.SearchAdapter;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.weight.TitleBar;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 搜索页面，需要添加模糊搜索
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

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
    }

    List<FriendListResponse> friendList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);
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
                    searchFriendInfo(searchEdit.getText().toString());
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

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            String userid=null;
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                getFriendListInfoById();

                SearchCustomerInfoResponse searchCustomerInfoResponse=mAdapter.getData().get(position);


                for (int i=0;i<friendList.size();i++) {
                    userid = friendList.get(i).getId();
                    if (userid.equals(searchCustomerInfoResponse.getId())) {
                        intent = new Intent(SearchActivity.this, FriendDetailsActivity.class);
                        intent.putExtra("searchFriendDetails", searchCustomerInfoResponse);
                        intent.putExtra("searchDetailsType", 1);
                      intent.putExtra("searchDetailsUserId",searchCustomerInfoResponse.getId());
                        startActivity(intent);
                    } else {
                        intent = new Intent(SearchActivity.this, AddFriendDetailsActivity.class);
                        intent.putExtra("searchAddFriendDetails", searchCustomerInfoResponse);
                        intent.putExtra("searchAddType", 1);
                        intent.putExtra("searchAddUserId",searchCustomerInfoResponse.getId());
                        startActivity(intent);
                    }
                }
            }
        });
        mAdapter.notifyDataSetChanged();

    }

    public void searchFriendInfo(String data) {
        //模糊搜索用户
        ServiceFactory.getInstance().getBaseService(Api.class)
                .searchCustomerInfo(data)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(searchCustomerInfoResponses -> {
                    mAdapter.setNewData(searchCustomerInfoResponses);
                }, this::handleApiError);

    }


    public void getFriendListInfoById(){
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendListById()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<List<FriendListResponse>>() {
                    @Override
                    public void accept(List<FriendListResponse> friendListResponses) throws Exception {

                        friendList=friendListResponses;
                    }
                },this::handleApiError);
    }

}
