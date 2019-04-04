package com.zxjk.duoduo.ui.msgpage;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.SelectForCardAdapter;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.BusinessCardMessage;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.weight.TitleBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * @author Administrator
 * @// TODO: 2019\4\4 0004 选择联系人发送名片
 */
public class SelectContactForCardActivity extends BaseActivity {
    TitleBar titleBar;
    EditText searchEdit;
    RecyclerView mRecyclerView;

    SelectForCardAdapter mAdapter;
    List<FriendInfoResponse> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact_for_card);
        titleBar = findViewById(R.id.title_bar);
        searchEdit = findViewById(R.id.search_select_contact);
        mRecyclerView = findViewById(R.id.card_recycler_view);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new SelectForCardAdapter();
        getFriendListById();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            RongIM.getInstance().startPrivateChat(this,list.get(position).getId(),list.get(position).getNick());
            BusinessCardMessage message = new BusinessCardMessage();
            message.setDuoduoId(list.get(position).getDuoduoId());
            message.setHeaderUrl(list.get(position).getHeadPortrait());
            message.setUserId(list.get(position).getId());
            message.setUserName(list.get(position).getNick());

            Message message1 = Message.obtain("55", Conversation.ConversationType.PRIVATE, message);

            RongIM.getInstance().sendMessage(message1, null, null, new IRongCallback.ISendMessageCallback() {
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

        });


    }

    private void getFriendListById() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendListById()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendInfoResponses -> {
                    list = friendInfoResponses;
                    mAdapter.setNewData(friendInfoResponses);
                }, this::handleApiError);
    }
}
