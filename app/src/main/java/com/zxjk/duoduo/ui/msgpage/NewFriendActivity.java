package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.NewFriendAdapter;
import com.zxjk.duoduo.ui.msgpage.widget.dialog.DeleteFriendDialog;
import com.zxjk.duoduo.ui.msgpage.widget.dialog.DeleteFriendInformationDialog;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.weight.TitleBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

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
    FriendInfoResponse friendInfoResponse;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_friend);
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
        getFriendListById();
        getMyFriendsWaiting();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            View vp = (View) view.getParent();
            TextView typeBtn = view.findViewById(R.id.m_item_new_friend_type_btn);
            TextView mark = vp.findViewById(R.id.m_item_new_friend_message_label);
            switch (view.getId()) {
                case R.id.m_item_new_friend_type_btn:
                    addFriend(mAdapter.getData().get(position).getId(), mark.getText().toString());
                    break;
                case R.id.m_add_btn_layout:
                    Intent intent = new Intent(NewFriendActivity.this, AddFriendDetailsActivity.class);
                    intent.putExtra("intentAddType", 1);
                    intent.putExtra("newFriend", mAdapter.getData().get(position));
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            mAdapter.notifyDataSetChanged();
        });
        mAdapter.setOnItemChildLongClickListener((adapter, view, position) -> {
            dialog = new DeleteFriendInformationDialog(NewFriendActivity.this);

            dialog.setOnClickListener(() -> deleteMyfirendsWaiting(list.get(position).getId()));
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
                            if (friendInfoResponses.size() >= 0) {
                                friendInfoResponse = new FriendInfoResponse(friendInfoResponses.get(i));
                            } else {
                                return;
                            }
                        }

                    }
                }, this::handleApiError);
    }


    /**
     * 获取待添加好友列表
     */
    public void getMyFriendsWaiting() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getMyFirendsWaiting()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<List<FriendInfoResponse>>() {
                    @Override
                    public void accept(List<FriendInfoResponse> s) throws Exception {
                        list.addAll(s);
                        for (int i = 0; i < s.size(); i++) {
                            if (friendInfoResponse == null || friendInfoResponse.getId().equals(null) || friendInfoResponse.getId().equals("")) {
                                mAdapter.addData(s.get(i));
                            } else {
                                if (s.get(i).getId().equals(friendInfoResponse.getId()) && s.get(i).getId().equals(Constant.userId) && !TextUtils.isEmpty(friendInfoResponse.getId())) {
                                    mAdapter.getData().remove(i);
                                } else {
                                    LogUtils.d("DEBUG", s.toString());
                                    mAdapter.addData(s.get(i));
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                        mAdapter.setNewData(s);
                    }
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
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {

                    ToastUtils.showShort(getString(R.string.add_friend_successful));

                }, this::handleApiError);
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
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mAdapter.notifyDataSetChanged();
                    }
                }, this::handleApiError);
    }


}
