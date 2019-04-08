package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.Group;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GroupChatResponse;
import com.zxjk.duoduo.network.response.GroupResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.AllGroupMembersActivity;
import com.zxjk.duoduo.ui.grouppage.GroupChatInformationActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.GroupChatAdapter;
import com.zxjk.duoduo.weight.TitleBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * @author Administrator
 * @// TODO: 2019\3\29 0029 群聊页面
 */
public class GroupChatActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.m_group_chat_title_bar)
    TitleBar mGroupChatTitleBar;
    @BindView(R.id.m_group_chat_edit_1)
    EditText mGroupChatEdit1;
    @BindView(R.id.m_group_chat_edit)
    LinearLayout mGroupChatEdit;
    @BindView(R.id.m_group_chat_recycler_view)
    RecyclerView mGroupChatRecyclerView;

    GroupChatAdapter groupChatAdapter;

    List<GroupChatResponse> list = new ArrayList<>();

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        ButterKnife.bind(this);

        //关闭当前活动
        mGroupChatTitleBar.getLeftImageView().setOnClickListener(v -> finish());
        LinearLayoutManager manage = new LinearLayoutManager(this);
        manage.setOrientation(LinearLayoutManager.VERTICAL);
        mGroupChatRecyclerView.setLayoutManager(manage);
        groupChatAdapter = new GroupChatAdapter();
        mGroupChatEdit1.addTextChangedListener(GroupChatActivity.this);
        //从网络获取用户所有群组信息
        getMyGroupChat(Constant.userId);

        mGroupChatRecyclerView.setAdapter(groupChatAdapter);
        groupChatAdapter.notifyDataSetChanged();
        groupChatAdapter.setOnItemChildClickListener((adapter, view, position) -> {
           Constant.groupChatResponse=groupChatAdapter.getData().get(position);
            RongIM.getInstance().startGroupChat(this, groupChatAdapter.getData().get(position).getId(), groupChatAdapter.getData().get(position).getGroupNikeName());
//            Intent intent=new Intent(this,GroupChatInformationActivity.class);
//            intent.putExtra("groupChatInformation", groupChatAdapter.getData().get(position));
//            startActivity(intent);
        });
    }

    @SuppressLint("CheckResult")
    private void getMyGroupChat(String userId) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getMygroupinformation(userId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<List<GroupChatResponse>>() {
                    @Override
                    public void accept(List<GroupChatResponse> s) throws Exception {
                        groupChatAdapter.setNewData(s);
                        list = s;
                    }
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
            List<GroupChatResponse> groupnamelist = search(groupname); //查找对应的群组数据
            groupChatAdapter.setNewData(groupnamelist);
        } else {
            groupChatAdapter.setNewData(list);
        }
        groupChatAdapter.notifyDataSetChanged();
    }

    /**
     * 模糊查询
     *
     * @param str
     * @return
     */
    private List<GroupChatResponse> search(String str) {
        List<GroupChatResponse> filterList = new ArrayList<GroupChatResponse>();// 过滤后的list
        if (str.matches("^([0-9]|[/+]).*")) {// 正则表达式 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
            String simpleStr = str.replaceAll("\\-|\\s", "");
            for (GroupChatResponse contact : list) {
                if (contact.getGroupNikeName() != null) {
                    if (contact.getGroupNikeName().contains(simpleStr) || contact.getGroupNikeName().contains(str)) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        } else {
            for (GroupChatResponse contact : list) {
                if (contact.getGroupNikeName() != null) {
                    //姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
                    boolean isNameContains = contact.getGroupNikeName().toLowerCase(Locale.CHINESE)
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
