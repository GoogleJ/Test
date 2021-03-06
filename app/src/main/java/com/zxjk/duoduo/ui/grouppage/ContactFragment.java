package com.zxjk.duoduo.ui.grouppage;

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

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.response.FriendInfoResponse;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.HomeActivity;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.ui.msgpage.AddContactActivity;
import com.zxjk.duoduo.ui.msgpage.FriendDetailsActivity;
import com.zxjk.duoduo.ui.msgpage.GroupChatActivity;
import com.zxjk.duoduo.ui.msgpage.NewFriendActivity;
import com.zxjk.duoduo.ui.msgpage.SearchActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.BaseContactAdapter;
import com.zxjk.duoduo.ui.msgpage.widget.IndexView;
import com.zxjk.duoduo.utils.MMKVUtils;
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

    List<FriendInfoResponse> list = new ArrayList<>();

    public View getDotNewFriend() {
        return dotNewFriend;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootview = LayoutInflater.from(getContext()).inflate(R.layout.activity_constacts_new_friend, container, false);
        ButterKnife.bind(this, rootview);
        initView(rootview);
        return rootview;
    }

    private void initView(View rootview) {
        if (MMKVUtils.getInstance().decodeLong("newFriendCount") != 0) {
            dotNewFriend.setVisibility(View.VISIBLE);
        }
        tv_title = rootview.findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.phone_tunxun));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new BaseContactAdapter();
        indexView.setShowTextDialog(constactsDialog);
        indexView.setOnTouchingLetterChangedListener(letter -> {
            for (int i = 0; i < list.size(); i++) {
                String letters = list.get(i).getSortLetters();
                if (letters.equals(letter)) {
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
                ((HomeActivity) getActivity()).badgeItem2.hide();
                MMKVUtils.getInstance().enCode("newFriendCount", 0);
                dotNewFriend.setVisibility(View.INVISIBLE);
                startActivity(new Intent(getActivity(), NewFriendActivity.class));
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

    @Override
    public void onResume() {
        super.onResume();
        getFriendListInfoById();
    }
}
