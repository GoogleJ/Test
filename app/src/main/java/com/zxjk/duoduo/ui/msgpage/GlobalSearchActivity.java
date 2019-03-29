package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendListResponse;
import com.zxjk.duoduo.network.response.SearchResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.GlobalSearchAdapter;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.weight.TitleBar;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 全局搜索
 */
@SuppressLint("CheckResult")
public class GlobalSearchActivity extends BaseActivity {
    @BindView(R.id.m_search_title_bar)
    TitleBar titleBar;
    @BindView(R.id.m_search_edit)
    EditText searchEdit;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    GlobalSearchAdapter mAdapter;
    Intent intent;

    List<FriendListResponse> list = new ArrayList<>();

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, GlobalSearchActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_search);
        ButterKnife.bind(this);
        initData();
        initUI();
    }

    private void initData() {
        searchEdit.setOnEditorActionListener((v, actionId, event) -> {
            //搜索按键action
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchCustomerInfo(searchEdit.getText().toString());
                return true;
            }
            return false;
        });
    }

    private void initUI() {
        titleBar.getLeftImageView().setOnClickListener(v -> finish());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new GlobalSearchAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            SearchResponse user = mAdapter.getData().get(position);
            getFriendListById();

            for (int i = 0; i < list.size(); i++) {

                if (user.getId().equals(list.get(i).getId())) {
                    intent = new Intent(this, FriendDetailsActivity.class);
                    intent.putExtra("globalUserDetails", user);
                    intent.putExtra("searchDetailsType",0);
                    intent.putExtra("globalUserDetailsId", user.getId());
                    startActivity(intent);
                } else {
                    intent = new Intent(this, AddFriendDetailsActivity.class);
                    intent.putExtra("globalAddUserDetails", user);
                    intent.putExtra("searchUserId", user.getId());
                    startActivity(intent);
                }

            }
        });
    }

    public void searchCustomerInfo(String data) {
        //模糊搜索好友
        ServiceFactory.getInstance().getBaseService(Api.class)
                .searchFriend(data)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(list -> mAdapter.setNewData(list), this::handleApiError);
    }

    public void getFriendListById() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendListById()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<List<FriendListResponse>>() {
                    @Override
                    public void accept(List<FriendListResponse> friendListResponses) throws Exception {

                        GlobalSearchActivity.this.list = friendListResponses;
                    }
                }, this::handleApiError);
    }
}
