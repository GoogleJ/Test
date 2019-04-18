package com.zxjk.duoduo.ui.msgpage;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.SelectForCardAdapter;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.BusinessCardMessage;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.ui.widget.TitleBar;
import com.zxjk.duoduo.ui.widget.dialog.BaseAddTitleDialog;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
 */
public class SelectContactForCardActivity extends BaseActivity implements TextWatcher {
    TitleBar titleBar;
    EditText searchEdit;
    RecyclerView mRecyclerView;
    SelectForCardAdapter mAdapter;
    UserInfo userId;
    int intentType;
    String friendInfoResponseId;
    String friendInfoId;
    String contactResponseId;
    String businessCardMessageId;

    List<FriendInfoResponse> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact_for_card);

        initView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        intentType = getIntent().getIntExtra("userType", 0);
        friendInfoResponseId = getIntent().getStringExtra("friendInfoResponseId");
        friendInfoId = getIntent().getStringExtra("friendInfoId");
        contactResponseId = getIntent().getStringExtra("contactResponseId");
        userId = getIntent().getParcelableExtra("user");
        businessCardMessageId = getIntent().getStringExtra("businessCardMessageId");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        searchEdit.addTextChangedListener(SelectContactForCardActivity.this);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new SelectForCardAdapter();
        mRecyclerView.setAdapter(mAdapter);
        getFriendListById();

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int privateOrGroupType = getIntent().getIntExtra("intentType", 0);
            if (privateOrGroupType == 0) {
                if (intentType == 0) {
                    SelectContactForCardActivity.this.getFriendGourp(mAdapter.getData().get(position).getId());
                }
            } else {
                if (intentType == 0) {
                    SelectContactForCardActivity.this.getFriendInfo(userId.getUserId(), position);
                } else if (intentType == 1) {
                    SelectContactForCardActivity.this.getFriendInfo(friendInfoResponseId, position);
                } else if (intentType == 2) {
                    SelectContactForCardActivity.this.getFriendInfo(friendInfoId, position);
                } else if (intentType == 3) {
                    SelectContactForCardActivity.this.getFriendInfo(contactResponseId, position);
                } else {
                    SelectContactForCardActivity.this.getFriendInfo(businessCardMessageId, position);
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
                    this.list = friendInfoResponses;
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
                .getCustomerInfoById(userId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(response -> {
                    BaseAddTitleDialog dialog = new BaseAddTitleDialog(SelectContactForCardActivity.this);
                    dialog.setOnClickListener(() -> {
                        BusinessCardMessage message = new BusinessCardMessage();
                        message.setDuoduo(response.getDuoduoId());
                        message.setIcon(response.getHeadPortrait());
                        message.setUserId(response.getId());
                        message.setName(response.getNick());
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
     */
    private void getFriendGourp(String userId) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getCustomerInfoById(userId)
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String groupname = s.toString();
        if (groupname != null || groupname.length() > 0) {
            List<FriendInfoResponse> groupnamelist = search(groupname); //查找对应的群组数据
            mAdapter.setNewData(groupnamelist);
        } else {
            mAdapter.setNewData(list);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 模糊查询
     *
     * @param str
     * @return
     */
    private List<FriendInfoResponse> search(String str) {
        List<FriendInfoResponse> filterList = new ArrayList<FriendInfoResponse>();// 过滤后的list
        if (str.matches("^([0-9]|[/+]).*")) {// 正则表达式 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
            String simpleStr = str.replaceAll("\\-|\\s", "");
            for (FriendInfoResponse contact : list) {
                if (contact.getNick() != null) {
                    if (contact.getNick().contains(simpleStr) || contact.getNick().contains(str)) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        } else {
            for (FriendInfoResponse contact : list) {
                if (contact.getNick() != null) {
                    //姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
                    boolean isNameContains = contact.getNick().toLowerCase(Locale.CHINESE)
                            .contains(str.toLowerCase(Locale.CHINESE));

//                    boolean isSortKeyContains = contact.sortKey.toLowerCase(Locale.CHINESE).replace(" ", "")
//                            .contains(str.toLowerCase(Locale.CHINESE));
//
//                    boolean isSimpleSpellContains = contact.sortToken.simpleSpell.toLowerCase(Locale.CHINESE)
//                            .contains(str.toLowerCase(Locale.CHINESE));
//
//                    boolean isWholeSpellContains = contact.sortToken.wholeSpell.toLowerCase(Locale.CHINESE)
//                            .contains(str.toLowerCase(Locale.CHINESE));

                    if (isNameContains) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        }
        return filterList;
    }
}
