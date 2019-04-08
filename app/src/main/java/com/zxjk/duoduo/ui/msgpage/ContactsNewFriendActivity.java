package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.BaseContactAdapter;
import com.zxjk.duoduo.ui.msgpage.widget.IndexView;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.weight.TitleBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 通讯录
 */
@SuppressLint("CheckResult")
public class ContactsNewFriendActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.m_constacts_new_friend_title_bar)
    TitleBar titleBar;
    @BindView(R.id.m_contact_add_friend_icon)
    ImageView addFriendImage;

    @BindView(R.id.m_contact_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.index_view)
    IndexView indexView;
    @BindView(R.id.m_constacts_dialog)
    TextView constactsDialog;


    private BaseContactAdapter mAdapter;
    FriendInfoResponse friendInfoResponse;
    Unbinder unbinder;

    private LinearLayoutManager layoutManager;



    List<FriendInfoResponse> list = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constacts_new_friend);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new BaseContactAdapter();
        getFriendListInfoById();
        mRecyclerView.setAdapter(mAdapter);
        titleBar.getLeftImageView().setOnClickListener(v -> this.finish());
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            FriendInfoResponse friendInfoResponse = mAdapter.getData().get(position);
            Intent intent = new Intent(this, FriendDetailsActivity.class);
            intent.putExtra("intentType", 2);
            intent.putExtra("contactResponse", friendInfoResponse);
            startActivity(intent);
        });
        if (mAdapter.getData().size() == 0) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_app_null_type, null);
            mAdapter.setEmptyView(view);
        }
    }

    @OnClick({R.id.m_constacts_new_friend_group_chat_btn, R.id.m_contact_add_friend_btn, R.id.m_contact_new_friend_btn, R.id.m_contact_search_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_constacts_new_friend_group_chat_btn:
                startActivity(new Intent(this, GroupChatActivity.class));
                break;
            case R.id.m_contact_add_friend_btn:
                startActivityForResult(new Intent(this, NewFriendActivity.class), 10);
                break;
            case R.id.m_contact_new_friend_btn:
                startActivity(new Intent(this, AddContactActivity.class));
                break;
            case R.id.m_contact_search_btn:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 获取好友列表
     */
    public void getFriendListInfoById() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendListById()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendInfoResponses -> {
                    for (int i = 0; i < friendInfoResponses.size(); i++) {
                        friendInfoResponse = new FriendInfoResponse(friendInfoResponses.get(i));
                        if (!friendInfoResponses.get(i).getId().equals(Constant.userId)) {
                            list = friendInfoResponses;
                            mAdapter.setNewData(friendInfoResponses);
                        } else {
                            if (friendInfoResponses.size() >= 0) {
                                friendInfoResponses.remove(i);
                            }
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }, this::handleApiError);
    }

    public void getMyfriendsWaiting() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getMyfriendsWaiting()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendInfoResponses -> {
                    for (int i = 0; i < friendInfoResponses.size(); i++) {
                        if ("0".equals(friendInfoResponses.get(i).getStatus())) {
                            addFriendImage.setImageResource(R.drawable.icon_new_friend_hl);
                        } else {
                            addFriendImage.setImageResource(R.drawable.icon_contract_friend);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }, this::handleApiError);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        getFriendListInfoById();
        getMyfriendsWaiting();
        mAdapter.notifyDataSetChanged();
    }
}
