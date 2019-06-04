package com.zxjk.duoduo.skin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxException;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.ui.msgpage.AddContactActivity;
import com.zxjk.duoduo.ui.msgpage.FriendDetailsActivity;
import com.zxjk.duoduo.ui.msgpage.GroupChatActivity;
import com.zxjk.duoduo.ui.msgpage.NewFriendActivity;
import com.zxjk.duoduo.ui.msgpage.SearchActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.BaseContactAdapter;
import com.zxjk.duoduo.ui.msgpage.widget.IndexView;
import com.zxjk.duoduo.ui.msgpage.widget.dialog.DeleteFriendInformationDialog;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.PinYinUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.m_contact_add_friend_icon)
    ImageView addFriendImage;
    @BindView(R.id.m_contact_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.index_view)
    IndexView indexView;
    @BindView(R.id.m_constacts_dialog)
    TextView constactsDialog;
    @BindView(R.id.dotNewFriend)
    View dotNewFriend;
    private BaseContactAdapter mAdapter;

    private TextView tv_title;

    private LinearLayoutManager layoutManager;
    List<FriendInfoResponse> list = new ArrayList<>();

    DeleteFriendInformationDialog deleteDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootview = LayoutInflater.from(getContext()).inflate(R.layout.activity_constacts_new_friend, container, false);
        ButterKnife.bind(this, rootview);
        deleteDialog = new DeleteFriendInformationDialog(getActivity());
        initView(rootview);
        getMyfriendsWaiting();

        return rootview;
    }

    private void initView(View rootview) {
        tv_title = rootview.findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.phone_tunxun));

        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new BaseContactAdapter();
        indexView.setShowTextDialog(constactsDialog);
        indexView.setOnTouchingLetterChangedListener(letter -> {
            for (int i = 0; i < list.size(); i++) {
                //获取名字的首字母
                String letters = list.get(i).getSortLetters();
                if (letters.equals(letter)) {
                    //第一次出现的位置
                    mRecyclerView.scrollToPosition(i);
                    break;
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            FriendInfoResponse friendInfoResponse = mAdapter.getData().get(position);
            Intent intent = new Intent(getActivity(), FriendDetailsActivity.class);
            intent.putExtra("friendResponse", friendInfoResponse);
            startActivity(intent);
        });
        if (mAdapter.getData().size() == 0) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_app_null_type, null);
            mAdapter.setEmptyView(view);
        }
    }

    @OnClick({R.id.m_constacts_new_friend_group_chat_btn, R.id.m_contact_add_friend_btn, R.id.m_contact_new_friend_btn, R.id.m_contact_search_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_constacts_new_friend_group_chat_btn:
                startActivity(new Intent(getActivity(), GroupChatActivity.class));
                break;
            case R.id.m_contact_add_friend_btn:
                dotNewFriend.setVisibility(View.GONE);
                startActivityForResult(new Intent(getActivity(), NewFriendActivity.class), 10);
                break;
            case R.id.m_contact_new_friend_btn:
                startActivity(new Intent(getActivity(), AddContactActivity.class));
                break;
            case R.id.m_contact_search_btn:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            default:
        }
    }

    /**
     * 获取好友列表
     */
    @SuppressLint("CheckResult")
    private void getFriendListInfoById() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendListById()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendInfoResponses -> {
                    list = friendInfoResponses;
                    mapList(list);
                    mAdapter.setNewData(list);
                }, this::handleApiError);
    }

    private void mapList(List<FriendInfoResponse> list) {
        for (FriendInfoResponse f : list) {
            f.setSortLetters(PinYinUtils.converterToFirstSpell(TextUtils.isEmpty(f.getRemark()) ? f.getNick() : f.getRemark()));
        }
        Comparator<FriendInfoResponse> comparator = (o1, o2) -> o1.getSortLetters().compareTo(o2.getSortLetters());
        Collections.sort(list, comparator);
    }

    @SuppressLint("CheckResult")
    private void getMyfriendsWaiting() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getMyfriendsWaiting()
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(getActivity())))
                .compose(RxSchedulers.normalTrans())
                .compose(bindToLifecycle())
                .subscribe(friendInfoResponses -> {
                    int newfriendRequestCount = SPUtils.getInstance().getInt("newfriendRequestCount", 0);
                    if (friendInfoResponses.size() > newfriendRequestCount) {
                        dotNewFriend.setVisibility(View.VISIBLE);
                    }
                }, t -> ToastUtils.showShort(RxException.getMessage(t)));
    }

    @Override
    public void onResume() {
        super.onResume();
        getFriendListInfoById();
        mAdapter.notifyDataSetChanged();
    }
}
