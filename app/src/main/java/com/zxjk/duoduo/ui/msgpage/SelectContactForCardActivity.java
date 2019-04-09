package com.zxjk.duoduo.ui.msgpage;

import android.os.Bundle;
import android.widget.EditText;

import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.SelectForCardAdapter;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.BusinessCardMessage;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.weight.TitleBar;
import com.zxjk.duoduo.weight.dialog.BaseAddTitleDialog;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;


/**
 * @author Administrator
 * @// TODO: 2019\4\4 0004 选择联系人发送名片
 */
public class SelectContactForCardActivity extends BaseActivity {
    TitleBar titleBar;
    EditText searchEdit;
    RecyclerView mRecyclerView;
    SelectForCardAdapter mAdapter;
    UserInfo userId;
    int type = 0;
    int intentType;
    String friendInfoResponseId;
    String friendInfoId;
    String contactResponseId;
    String businessCardMessageId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact_for_card);
        initView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        intentType = getIntent().getIntExtra("userType", type);
        friendInfoResponseId = getIntent().getStringExtra("friendInfoResponseId");
        friendInfoId = getIntent().getStringExtra("friendInfoId");
        contactResponseId = getIntent().getStringExtra("contactResponseId");
        userId = getIntent().getParcelableExtra("user");
        businessCardMessageId=getIntent().getStringExtra("businessCardMessageId");


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new SelectForCardAdapter();
        getFriendListById();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int groupType=0;
            int privateOrGroupType=getIntent().getIntExtra("intentType",groupType);
            if (privateOrGroupType==0){
                if (intentType==0){
                    SelectContactForCardActivity.this.getFriendGourp(mAdapter.getData().get(position).getId(), position);
                }
                return;
            }else{
                if (intentType == 0) {
                    SelectContactForCardActivity.this.getFriendInfo(userId.getUserId(), position);
                } else if (intentType == 1) {
                    SelectContactForCardActivity.this.getFriendInfo(friendInfoResponseId, position);
                } else if (intentType == 2) {
                    SelectContactForCardActivity.this.getFriendInfo(friendInfoId, position);
                } else if (intentType == 3) {
                    SelectContactForCardActivity.this.getFriendInfo(contactResponseId, position);
                }else{
                    SelectContactForCardActivity.this.getFriendInfo(businessCardMessageId,position);
                }
            }

        });
    }

    private void initView() {
        titleBar = findViewById(R.id.title_bar);
        searchEdit = findViewById(R.id.search_select_contact);
        mRecyclerView = findViewById(R.id.card_recycler_view);
        titleBar.getLeftImageView().setOnClickListener(v -> finish());
    }

    /**
     * 获取好友列表
     */
    private void getFriendListById() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendListById()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendInfoResponses -> {

                    mAdapter.setNewData(friendInfoResponses);
                }, this::handleApiError);
    }

    /**
     * 获取好友详情
     *
     * @param userId
     * @param position
     */
    private void getFriendInfo(String userId, int position) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendInfoById(userId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendInfoResponse -> {
                    BaseAddTitleDialog dialog = new BaseAddTitleDialog(SelectContactForCardActivity.this);
                    dialog.setOnClickListener(() -> {
                        BusinessCardMessage message = new BusinessCardMessage();
                        message.setDuoduo(friendInfoResponse.getDuoduoId());
                        message.setIcon(friendInfoResponse.getHeadPortrait());
                        message.setUserId(friendInfoResponse.getId());
                        message.setName(friendInfoResponse.getNick());
                        Message message1 = Message.obtain(mAdapter.getData().get(position).getId(), Conversation.ConversationType.PRIVATE, message);
                        RongIM.getInstance().sendMessage(message1, null, null, new IRongCallback.ISendMessageCallback() {
                            @Override
                            public void onAttached(Message message) {
                            }

                            @Override
                            public void onSuccess(Message message) {
                                RongIM.getInstance().startPrivateChat(SelectContactForCardActivity.this, mAdapter.getData().get(position).getId(), mAdapter.getData().get(position).getNick());
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                            }
                        });
                    });
                    dialog.show(getString(R.string.share_business_card));
                }, this::handleApiError);
    }
    /**
     * 获取好友详情
     *
     * @param userId
     * @param position
     */
    private void getFriendGourp(String userId, int position) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendInfoById(userId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendInfoResponse -> {
                    BaseAddTitleDialog dialog = new BaseAddTitleDialog(SelectContactForCardActivity.this);
                    dialog.setOnClickListener(() -> {
                        BusinessCardMessage message = new BusinessCardMessage();
                        message.setDuoduo(friendInfoResponse.getDuoduoId());
                        message.setIcon(friendInfoResponse.getHeadPortrait());
                        message.setUserId(friendInfoResponse.getId());
                        message.setName(friendInfoResponse.getNick());
                        Message message1 = Message.obtain(Constant.groupChatResponse.getId(), Conversation.ConversationType.GROUP, message);
                        RongIM.getInstance().sendMessage(message1, null, null, new IRongCallback.ISendMessageCallback() {
                            @Override
                            public void onAttached(Message message) {
                            }

                            @Override
                            public void onSuccess(Message message) {
                                RongIM.getInstance().startGroupChat(SelectContactForCardActivity.this, Constant.groupChatResponse.getId(), Constant.groupChatResponse.getGroupNikeName());
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                            }
                        });
                    });
                    dialog.show(getString(R.string.share_business_card));
                }, this::handleApiError);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
