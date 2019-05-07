package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.BaseContactAdapter;
import com.zxjk.duoduo.ui.msgpage.widget.IndexView;
import com.zxjk.duoduo.ui.msgpage.widget.dialog.DeleteFriendInformationDialog;
import com.zxjk.duoduo.utils.CommonUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.CommandMessage;

/**
 * author L
 * create at 2019/5/7
 * description: 通讯录
 */
@SuppressLint("CheckResult")
public class ContactsNewFriendActivity extends BaseActivity implements View.OnClickListener {

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

    private LinearLayoutManager layoutManager;
    List<FriendInfoResponse> list = new ArrayList<>();

    DeleteFriendInformationDialog deleteDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constacts_new_friend);
        ButterKnife.bind(this);
        deleteDialog = new DeleteFriendInformationDialog(this);
        initView();
        getMyfriendsWaiting();
    }

    private void initView() {
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.address_list));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new BaseContactAdapter();
        getFriendListInfoById();
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
            Intent intent = new Intent(this, FriendDetailsActivity.class);
            intent.putExtra("intentType", 2);
            intent.putExtra("contactResponse", friendInfoResponse);
            startActivity(intent);
        });
        mAdapter.setOnItemChildLongClickListener((adapter, view, position) -> {
            FriendInfoResponse friendInfoResponse = mAdapter.getData().get(position);
            deleteDialog.show(friendInfoResponse.getNick());
            deleteDialog.setOnClickListener(() -> ServiceFactory.getInstance().getBaseService(Api.class)
                    .deleteFriend(friendInfoResponse.getId())
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ContactsNewFriendActivity.this)))
                    .compose(RxSchedulers.normalTrans())
                    .subscribe(s -> {
                        ToastUtils.showShort(R.string.delete_friend_succesed);
                        adapter.getData().remove(position);
                        adapter.notifyItemRemoved(position);

                        Message myMessage = Message.obtain(friendInfoResponse.getId(), Conversation.ConversationType.PRIVATE, CommandMessage.obtain("deleteFriend", ""));
                        RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
                            @Override
                            public void onAttached(Message message) {

                            }

                            @Override
                            public void onSuccess(Message message) {
                                RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE
                                        , friendInfoResponse.getId(), null);
                                RongIMClient.getInstance().cleanHistoryMessages(Conversation.ConversationType.PRIVATE,
                                        friendInfoResponse.getId(), 0, false, null);
                            }

                            @Override
                            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                            }
                        });
                    }, ContactsNewFriendActivity.this::handleApiError));
            return false;
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
            //群聊
            case R.id.m_constacts_new_friend_group_chat_btn:
                startActivity(new Intent(this, GroupChatActivity.class));
                break;
            //新的朋友
            case R.id.m_contact_add_friend_btn:
                dotNewFriend.setVisibility(View.GONE);
                startActivityForResult(new Intent(this, NewFriendActivity.class), 10);
                break;
            //
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
                    list = friendInfoResponses;
                    mapList(list);
                    mAdapter.setNewData(list);
                    if (Constant.friendsList == null) {
                        Constant.friendsList = list;
                    }
                }, this::handleApiError);
    }

    private void mapList(List<FriendInfoResponse> list) {
        for (FriendInfoResponse f : list) {
            f.setSortLetters(converterToFirstSpell(f.getNick()));
        }
        Comparator<FriendInfoResponse> comparator = (o1, o2) -> o1.getSortLetters().compareTo(o2.getSortLetters());
        Collections.sort(list, comparator);
    }

    public static String converterToFirstSpell(String chines) {
        StringBuilder pinyinName = new StringBuilder();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char aNameChar : nameChar) {
            if (aNameChar > 128) {
                try {
                    String[] strings = PinyinHelper.toHanyuPinyinStringArray(
                            aNameChar, defaultFormat);
                    if (strings == null) {
                        return "#";
                    }

                    pinyinName.append(strings[0].charAt(0));
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName.append(aNameChar);
            }
        }
        return pinyinName.toString().substring(0, 1).toUpperCase();
    }

    public void getMyfriendsWaiting() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getMyfriendsWaiting()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendInfoResponses -> {
                    int newfriendRequestCount = SPUtils.getInstance().getInt("newfriendRequestCount", 0);
                    if (friendInfoResponses.size() > newfriendRequestCount) {
                        dotNewFriend.setVisibility(View.VISIBLE);
                    }
                }, this::handleApiError);
    }

    @Override
    public void onResume() {
        super.onResume();
        getFriendListInfoById();
        mAdapter.notifyDataSetChanged();
    }

}
