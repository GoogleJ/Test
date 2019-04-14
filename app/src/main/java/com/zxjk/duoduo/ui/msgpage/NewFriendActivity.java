package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.NewFriendAdapter;
import com.zxjk.duoduo.ui.msgpage.widget.dialog.DeleteFriendInformationDialog;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.ui.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020  新的朋友
 */
@SuppressLint("CheckResult")
public class NewFriendActivity extends BaseActivity {
    @BindView(R.id.m_fragment_new_friend_title_bar)
    TitleBar titleBar;
    @BindView(R.id.m_contact_search_edit_1)
    TextView textView;
    @BindView(R.id.m_fragment_new_friend_recycler_view)
    RecyclerView mRecyclerView;

    NewFriendAdapter mAdapter;
    DeleteFriendInformationDialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        ButterKnife.bind(this);
        initUI();
    }

    List<FriendInfoResponse> list = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("WrongConstant")
    protected void initUI() {
        titleBar.getLeftImageView().setOnClickListener(v -> finish());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new NewFriendAdapter();
        getMyFriendsWaiting();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            View vp = (View) view.getParent();
            TextView mark = vp.findViewById(R.id.m_item_new_friend_message_label);
            FriendInfoResponse item = (FriendInfoResponse) adapter.getData().get(position);
            boolean isTrue = item.getStatus().equals("0");
            switch (view.getId()) {
                case R.id.m_item_new_friend_type_btn:
                    if (isTrue) {
                        item.setStatus("2");
                        adapter.notifyItemChanged(position);
                        addFriend(mAdapter.getData().get(position).getId(), mark.getText().toString());
                    }
                    break;
                case R.id.m_add_btn_layout:
                    if (isTrue) {
                        Intent intent = new Intent(NewFriendActivity.this, AddFriendDetailsActivity.class);
                        intent.putExtra("intentAddType", 1);
                        intent.putExtra("newFriend", mAdapter.getData().get(position));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(NewFriendActivity.this, FriendDetailsActivity.class);
                        intent.putExtra("searchFriendDetails", mAdapter.getData().get(position));
                        startActivity(intent);
                    }
                    break;
                default:
            }
            mAdapter.notifyDataSetChanged();
        });
        mAdapter.setOnItemChildLongClickListener((adapter, view, position) -> {
            dialog = new DeleteFriendInformationDialog(NewFriendActivity.this);
            dialog.setOnClickListener(() -> {
                dialog.dismiss();
                deleteMyfirendsWaiting(list.get(position).getId());
            });
            dialog.show(list.get(position).getNick());
            return false;
        });
        if (mAdapter.getData().size() == 0) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_app_null_type, null);
            mAdapter.setEmptyView(view);
        }
        textView.setOnClickListener(v -> startActivity(new Intent(NewFriendActivity.this, SearchActivity.class)));
    }

    /**
     * 获取待添加好友列表
     */
    public void getMyFriendsWaiting() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getMyfriendsWaiting()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    SPUtils.getInstance().put("newfriendRequestCount", s.size());
                    list.addAll(s);
                    mAdapter.setNewData(s);
                }, this::handleApiError);
    }

    /**
     * 同意添加
     *
     * @param friendId
     * @param markName
     */
    public void addFriend(String friendId, String markName) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .addFriend(friendId, markName)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(NewFriendActivity.this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> ToastUtils.showShort(getString(R.string.add_friend_successful)), this::handleApiError);
    }

    /**
     * 删除好友申请
     *
     * @param friendId
     */

    public void deleteMyfirendsWaiting(String friendId) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .deleteMyfirendsWaiting(friendId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> mAdapter.notifyDataSetChanged(), this::handleApiError);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
}
