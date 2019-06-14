package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.bean.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.NewFriendAdapter;
import com.zxjk.duoduo.ui.msgpage.widget.dialog.DeleteFriendInformationDialog;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.CommandMessage;
import io.rong.message.InformationNotificationMessage;

/**
 * 新的朋友
 */
@SuppressLint("CheckResult")
public class NewFriendActivity extends BaseActivity {

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
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.new_friend));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new NewFriendAdapter();
        getMyFriendsWaiting();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            FriendInfoResponse item = (FriendInfoResponse) adapter.getData().get(position);
            boolean isTrue = item.getStatus().equals("0");
            switch (view.getId()) {
                case R.id.m_item_new_friend_type_btn:
                    if (isTrue) {
                        item.setStatus("2");
                        adapter.notifyItemChanged(position);
                        addFriend(mAdapter.getData().get(position).getId(), item.getNick(), item);
                    }
                    break;
                case R.id.m_add_btn_layout:
                    if (isTrue) {
                    } else {
                        Intent intent = new Intent(NewFriendActivity.this, FriendDetailsActivity.class);
                        intent.putExtra("friendResponse", mAdapter.getData().get(position));
                        startActivity(intent);
                    }
                    break;
                default:
            }
            mAdapter.notifyDataSetChanged();
        });

        if (mAdapter.getData().size() == 0) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_app_null_type, null);
            mAdapter.setEmptyView(view);
        }
        textView.setOnClickListener(v -> startActivity(new Intent(NewFriendActivity.this, GlobalSearchActivity.class)));
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
                    list.addAll(s);
                    mAdapter.setNewData(s);
                }, this::handleApiError);
    }

    /**
     * 同意添加
     *
     * @param friendId
     * @param markName
     * @param item
     */
    public void addFriend(String friendId, String markName, FriendInfoResponse item) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .addFriend(friendId, markName)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(NewFriendActivity.this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    RongUserInfoManager.getInstance().setUserInfo(new UserInfo(item.getId(), item.getNick(), Uri.parse(item.getHeadPortrait())));
                    ToastUtils.showShort(getString(R.string.add_friend_successful));
                    InformationNotificationMessage message = InformationNotificationMessage.obtain(getString(R.string.new_friend1));
                    RongIM.getInstance().sendDirectionalMessage(Conversation.ConversationType.PRIVATE, friendId, message, new String[]{friendId}, null, null, null);
                    CommandMessage commandMessage = CommandMessage.obtain("agreeFriend", "");
                    Message message1 = Message.obtain(friendId, Conversation.ConversationType.PRIVATE, commandMessage);
                    RongIM.getInstance().sendMessage(message1, "", "", new IRongCallback.ISendMessageCallback() {
                        @Override
                        public void onAttached(Message message) {

                        }

                        @Override
                        public void onSuccess(Message message) {

                        }

                        @Override
                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }, this::handleApiError);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
}
