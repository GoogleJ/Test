package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
import com.zxjk.duoduo.ui.msgpage.adapter.CreateGroupAdapter;
import com.zxjk.duoduo.ui.msgpage.adapter.CreateGroupTopAdapter;
import com.zxjk.duoduo.ui.msgpage.adapter.GroupMemberAdapter;
import com.zxjk.duoduo.ui.msgpage.adapter.GroupMemberTopAdapter;
import com.zxjk.duoduo.ui.msgpage.widget.IndexView;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.PinYinUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

@SuppressLint("CheckResult")
public class CreateGroupActivity extends BaseActivity {

    private List<String> selectedIds = new ArrayList<>();

    private TextView tvCreateGroupConfirm;
    private TextView tvUpdateInfoTitle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        eventType = getIntent().getIntExtra("eventType", -1);

        tvCreateGroupConfirm = findViewById(R.id.tvCreateGroupConfirm);
        tvUpdateInfoTitle = findViewById(R.id.tvUpdateInfoTitle);
        confirmText = tvCreateGroupConfirm.getText();
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
            tvUpdateInfoTitle.setText(R.string.remove_from_group);
            handleDeleteMember();
        }
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
            tvCreateGroupConfirm.setText(confirmText + "(" + data3.size() + ")");
        });
    }

    //添加成员逻辑
    private void handleAddMember() {
        initData2();
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
            tvCreateGroupConfirm.setText(confirmText + "(" + data1.size() + ")");
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
            tvCreateGroupConfirm.setText(confirmText + "(" + data1.size() + ")");
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

    public void back(View view) {
        finish();
    }

    public void confirm(View view) {
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
                    intent.putExtra("fromCreateGroup", true);
                    intent.putExtra("fromCreateGroupInfo", s);
                    startActivity(intent);
                }, this::handleApiError);
    }
}