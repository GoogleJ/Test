package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.GlobalSearchAdapter;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.ui.widget.TitleBar;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 搜索--搜索用户，添加好友
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

    FriendInfoResponse friendInfoResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_search);
        ButterKnife.bind(this);
        getFriendListById();
        initData();
        initUI();
    }

    private void initData() {
        searchEdit.setOnEditorActionListener((v, actionId, event) -> {
            //搜索按键action
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!TextUtils.isEmpty(searchEdit.getText().toString())) {

                    searchCustomerInfo(searchEdit.getText().toString());
                } else {
                    ToastUtils.showShort(getString(R.string.input_search_edit));
                }

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
            FriendInfoResponse user = mAdapter.getData().get(position);
            getFriendListById();

            if (friendInfoResponse == null || friendInfoResponse.getId().equals(null) || friendInfoResponse.getId().equals("")) {
                intent = new Intent(GlobalSearchActivity.this, AddFriendDetailsActivity.class);
                intent.putExtra("newFriend", user);
                startActivity(intent);
                return;
            } else {
                if (friendInfoResponse.getId().equals(user.getId())) {
                    intent = new Intent(this, FriendDetailsActivity.class);
                    intent.putExtra("intentType", 1);
                    intent.putExtra("globalSearchFriendDetails", user);
                    startActivity(intent);
                } else {
                    intent = new Intent(GlobalSearchActivity.this, AddFriendDetailsActivity.class);
                    intent.putExtra("newFriend", user);
                    startActivity(intent);
                }
                return;
            }
        });
    }

    //模糊搜索好友
    public void searchCustomerInfo(String data) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .searchCustomerInfo(data)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(list -> {
                    for (int i = 0; i < list.size(); i++) {
                        if (!list.get(i).getId().equals(Constant.userId)) {
                            mAdapter.addData(list.get(i));
                        } else {
                            if (list.size() >= 0) {
                                list.remove(i);
                            }
                        }
                    }

                }, this::handleApiError);
    }


    /**
     * 查询已有的好友列表
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
                        for (int i = 0; i < friendInfoResponses.size(); i++) {
                            friendInfoResponse = friendInfoResponses.get(i);
                        }
                    }
                }, this::handleApiError);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
