package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.GlobalSearchAdapter;
import com.zxjk.duoduo.ui.widget.TitleBar;
import com.zxjk.duoduo.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Administrator
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

    View emptyView;

    FriendInfoResponse friendInfoResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_search);
        ButterKnife.bind(this);
        emptyView = getLayoutInflater().inflate(R.layout.view_app_null_type, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        ImageView app_type = emptyView.findViewById(R.id.app_type);
        TextView app_prompt_text = emptyView.findViewById(R.id.app_prompt_text);
        app_type.setImageResource(R.drawable.icon_no_search);
        app_prompt_text.setText(getString(R.string.no_search));
        app_prompt_text.setVisibility(View.GONE);

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
        titleBar.getLeftImageView().setOnClickListener(v -> {
            CommonUtils.hideInputMethod(this);
            finish();
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new GlobalSearchAdapter(this);
        mAdapter.setEmptyView(emptyView);
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
        if (TextUtils.isEmpty(data)) {
            ToastUtils.showShort(R.string.input_empty);
            return;
        }
        ServiceFactory.getInstance().getBaseService(Api.class)
                .searchCustomerInfo(data)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(list -> {
                    if (list.size() == 0) {
                        ToastUtils.showShort(R.string.no_search);
                    }
                    FriendInfoResponse friendInfoResponse = new FriendInfoResponse();
                    for (FriendInfoResponse f : list) {
                        if (f.getId().equals(Constant.userId)) {
                            friendInfoResponse = f;
                        }
                    }
                    if (!TextUtils.isEmpty(friendInfoResponse.getId())) {
                        list.remove(friendInfoResponse);
                    }
                    mAdapter.setNewData(list);
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
                .subscribe(friendInfoResponses -> {
                    for (int i = 0; i < friendInfoResponses.size(); i++) {
                        friendInfoResponse = friendInfoResponses.get(i);
                    }
                }, this::handleApiError);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
