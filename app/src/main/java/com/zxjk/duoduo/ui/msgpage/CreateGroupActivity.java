package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.response.GroupResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.HomeActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.AllGroupActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.CreateGroupAdapter;
import com.zxjk.duoduo.ui.msgpage.adapter.CreateGroupTopAdapter;
import com.zxjk.duoduo.ui.msgpage.adapter.GroupMemberAdapter;
import com.zxjk.duoduo.ui.msgpage.adapter.GroupMemberTopAdapter;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.GroupCardMessage;
import com.zxjk.duoduo.ui.msgpage.widget.IndexView;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.PinYinUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * author L
 * create at 2019/5/8
 * description: 群添加成员  添加联系人 我的游戏群 新建游戏群
 */
@SuppressLint("CheckResult")
public class CreateGroupActivity extends BaseActivity {

    private List<String> selectedIds = new ArrayList<>();


    private RecyclerView recycler1;
    private RecyclerView recycler2;
    private CreateGroupTopAdapter adapter1; //建群适配器（顶部）
    private CreateGroupAdapter adapter2; //建群列表适配器
    private TextView tv_hit_letter;
    private IndexView indexCreateGroup;

    private List<FriendInfoResponse> data = new ArrayList<>(); //总list
    private List<FriendInfoResponse> data1 = new ArrayList<>(); //top数据

    private List<GroupResponse.CustomersBean> data2 = new ArrayList<>(); //总list（群成员）
    private List<GroupResponse.CustomersBean> data3 = new ArrayList<>(); //top数据（群成员）

    private GroupMemberTopAdapter adapter3; //群成员适配器（顶部）
    private GroupMemberAdapter adapter4; //群成员列表适配器

    CharSequence confirmText;

    private int eventType;

    private final int EVENT_CREATEGROUP = 1; //创建群
    private final int EVENT_ADDMENBER = 2; //添加成员
    private final int EVENT_DELETEMEMBER = 3; //删除成员

    //temp
    List<GroupResponse.CustomersBean> c = new ArrayList<>();

    private TextView tv_commit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ButterKnife.bind(this);
        tv_commit = findViewById(R.id.tv_commit);
        tv_commit.setVisibility(View.VISIBLE);
        tv_commit.setText(getString(R.string.ok));
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.select_contacts));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());

        GroupResponse group = (GroupResponse) getIntent().getSerializableExtra("members");
        if (group != null) {
            List<GroupResponse.CustomersBean> customers = group.getCustomers();
            c.addAll(customers);
        }

        eventType = getIntent().getIntExtra("eventType", -1);


        confirmText = tv_commit.getText();
        recycler1 = findViewById(R.id.recycler1);
        recycler2 = findViewById(R.id.recycler2);
        tv_hit_letter = findViewById(R.id.tv_hit_letter);
        indexCreateGroup = findViewById(R.id.indexCreateGroup);

        indexCreateGroup.setShowTextDialog(tv_hit_letter);
        indexCreateGroup.setOnTouchingLetterChangedListener(letter -> recycler2.scrollToPosition(getScrollPosition(letter)));

        if (eventType == EVENT_CREATEGROUP) {
            handleCreateGroup();
        }

        if (eventType == EVENT_ADDMENBER) {
            handleAddMember();
        }

        if (eventType == EVENT_DELETEMEMBER) {
            tv_title.setText(R.string.remove_from_group);
            handleDeleteMember();
        }

        tv_commit.setOnClickListener(v -> confirm());
    }

    //删除成员逻辑
    private void handleDeleteMember() {
        initData2();
        addFirstLetterForList1(data2);
        recycler1.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recycler2.setLayoutManager(new LinearLayoutManager(this));
        recycler2.setItemAnimator(null);
        adapter3 = new GroupMemberTopAdapter();
        adapter4 = new GroupMemberAdapter();
        recycler1.setAdapter(adapter3);
        recycler2.setAdapter(adapter4);
        adapter4.setData(data2);
        adapter3.setData(data3);
        adapter4.setOnClickListener((item, check, position) -> {

            int realPosition = data3.indexOf(item);
            if (data3.contains(item)) {
                selectedIds.remove(item.getId());
                data3.remove(item);
                adapter3.notifyItemRemoved(realPosition);
            } else {
                selectedIds.add(item.getId());
                data3.add(item);
                adapter3.notifyItemInserted(data2.size() - 1);
            }
            tv_commit.setText(confirmText + "(" + data3.size() + ")");
        });
    }

    //添加成员逻辑
    private void handleAddMember() {
        initData2();
        recycler1.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        adapter1 = new CreateGroupTopAdapter();
        recycler1.setAdapter(adapter1);

        adapter2 = new CreateGroupAdapter(true);
        recycler2.setLayoutManager(new LinearLayoutManager(this));
        recycler2.setAdapter(adapter2);
        recycler2.setItemAnimator(null);

        adapter2.setOnClickListener((item, check, position) -> {

            int realPosition = data1.indexOf(item);
            if (data1.contains(item)) {
                item.setChecked(!item.isChecked());
                adapter2.notifyItemChanged(position);
                selectedIds.remove(item.getId());
                data1.remove(item);
                adapter1.notifyItemRemoved(realPosition);

                tv_commit.setText(confirmText + "(" + data1.size() + ")");
                adapter2.notifyDataSetChanged();
            } else {
                if (adapter1.getItemCount() < 5) {
                    item.setChecked(!item.isChecked());
                    adapter2.notifyItemChanged(position);
                    selectedIds.add(item.getId());
                    data1.add(item);
                    adapter1.notifyItemInserted(data.size() - 1);
                    tv_commit.setText(confirmText + "(" + data1.size() + ")");
                    adapter2.notifyDataSetChanged();
                } else {
                    ToastUtils.showShort("群聊邀请每次最多可邀请五名好友");
                }
            }

        });
        adapter2.setData1(data2);
        initData();
    }

    //创建群逻辑
    private void handleCreateGroup() {
        recycler1.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        adapter1 = new CreateGroupTopAdapter();
        recycler1.setAdapter(adapter1);

        adapter2 = new CreateGroupAdapter();
        recycler2.setLayoutManager(new LinearLayoutManager(this));
        recycler2.setAdapter(adapter2);
        recycler2.setItemAnimator(null);

        adapter2.setOnClickListener((item, check, position) -> {
            int realPosition = data1.indexOf(item);
            if (data1.contains(item)) {
                selectedIds.remove(item.getId());
                data1.remove(item);
                adapter1.notifyItemRemoved(realPosition);
            } else {
                selectedIds.add(item.getId());
                data1.add(item);
                adapter1.notifyItemInserted(data.size() - 1);
            }
            tv_commit.setText(confirmText + "(" + data1.size() + ")");
        });

        initData();
    }

    private void initData() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendListById()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(response -> {
                    addFirstLetterForList(response);
                    data = response;
                    adapter2.setData(data);
                    adapter1.setData(data1);
                }, this::handleApiError);
    }

    private void initData2() {
        //获取群成员
        GroupResponse group = (GroupResponse) getIntent().getSerializableExtra("members");
        data2 = group.getCustomers();
        for (GroupResponse.CustomersBean c : data2) {
            if (c.getId().equals(Constant.userId)) {
                data2.remove(c);
                break;
            }
        }
    }

    private void addFirstLetterForList1(List<GroupResponse.CustomersBean> list) {
        Comparator<GroupResponse.CustomersBean> comparator = (f1, f2) -> f1.getFirstLetter().compareTo(f2.getFirstLetter());
        for (GroupResponse.CustomersBean c : list) {
            c.setFirstLetter(PinYinUtils.converterToFirstSpell(c.getNick()));
        }
        Collections.sort(list, comparator);
    }

    private void addFirstLetterForList(List<FriendInfoResponse> list) {
        Comparator<FriendInfoResponse> comparator = (f1, f2) -> f1.getFirstLeter().compareTo(f2.getFirstLeter());
        for (FriendInfoResponse f : list) {
            f.setFirstLeter(PinYinUtils.converterToFirstSpell(f.getNick()));
        }
        Collections.sort(list, comparator);
    }

    private int getScrollPosition(String letter) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getFirstLeter().equals(letter)) {
                return i;
            }
        }
        return -1;
    }


    public void confirm() {
        if (eventType == EVENT_CREATEGROUP) {
            //创建群
            if (data1.size() < 2) {
                ToastUtils.showShort(R.string.create_group_tips);
                return;
            }
            createGroup();
        }

        if (eventType == EVENT_ADDMENBER) {
            //添加成员
            if (data1.size() == 0) {
                ToastUtils.showShort(R.string.select_invite_member);
                return;
            }
            inviteMember();
        }

        if (eventType == EVENT_DELETEMEMBER) {
            //删除成员
            if (data3.size() == 0) {
                ToastUtils.showShort(R.string.select_remove_member);
                return;
            }
            deleteMember();
        }
    }

    private void inviteMember() {
        GroupResponse group = (GroupResponse) getIntent().getSerializableExtra("members");
        StringBuilder stringBuilder = new StringBuilder();
        for (GroupResponse.CustomersBean b : c) {
            stringBuilder.append(b.getHeadPortrait() + ",");
        }

        GroupCardMessage groupCardMessage = new GroupCardMessage();
        groupCardMessage.setIcon(stringBuilder.substring(0, stringBuilder.length() - 1));
        groupCardMessage.setGroupId(group.getGroupInfo().getId());
        groupCardMessage.setInviterId(Constant.userId);
        groupCardMessage.setName(Constant.currentUser.getNick());
        groupCardMessage.setGroupName(group.getGroupInfo().getGroupNikeName());
        for (FriendInfoResponse f : data1) {
            Message message = Message.obtain(f.getId(), Conversation.ConversationType.PRIVATE, groupCardMessage);
            RongIM.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
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
        }
        finish();
    }

    private void deleteMember() {
        GroupResponse group = (GroupResponse) getIntent().getSerializableExtra("members");
        StringBuilder sb = new StringBuilder();
        for (String ids : selectedIds) {
            sb.append(ids + ",");
        }
        ServiceFactory.getInstance().getBaseService(Api.class)
                .moveOutGroup(group.getGroupInfo().getId(), sb.substring(0, sb.length() - 1))
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(CreateGroupActivity.this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    Intent intent = new Intent(this, ConversationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }, this::handleApiError);
    }

    private void createGroup() {
        StringBuilder sb = new StringBuilder();
        for (String ids : selectedIds) {
            sb.append(ids + ",");
        }
        sb.append(Constant.userId);

        ServiceFactory.getInstance().getBaseService(Api.class)
                .makeGroup(Constant.userId, sb.toString())
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    RongIM.getInstance().startGroupChat(this, s.getId(), s.getGroupNikeName());
                }, this::handleApiError);
    }

    @OnClick({R.id.rl_myGameGroup, R.id.rl_newGameGroup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //我的游戏群
            case R.id.rl_myGameGroup:
                startActivity(new Intent(this, AllGroupActivity.class));
                finish();
                break;
            //新建游戏群
            case R.id.rl_newGameGroup:
                startActivity(new Intent(this, CreateGameGroupActivity.class));
                finish();
                break;
        }
    }
}
