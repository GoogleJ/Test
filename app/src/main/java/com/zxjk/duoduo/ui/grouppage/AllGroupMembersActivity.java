package com.zxjk.duoduo.ui.grouppage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.AllGroupMembersResponse;
import com.zxjk.duoduo.network.response.GroupChatResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.adapter.AllGroupMemebersAdapter;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.ui.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\26 0026 全部群成员
 */
@SuppressLint("CheckResult")
public class AllGroupMembersActivity extends BaseActivity implements TextWatcher {
    TitleBar titleBar;
    EditText searchEdit;
    RecyclerView mRecyclerView;

    AllGroupMemebersAdapter mAdapter;

    List<AllGroupMembersResponse> list = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_group_members);
        titleBar = findViewById(R.id.title_bar);
        searchEdit = findViewById(R.id.search_edit);
        mRecyclerView = findViewById(R.id.recycler_view);
        searchEdit.addTextChangedListener(this);
        initView();
    }

    private void initView() {
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new AllGroupMemebersAdapter();
        GroupChatResponse groupResponse= (GroupChatResponse) getIntent().getSerializableExtra("allGroupMembers");
        getGroupMemByGroupId(groupResponse.getId());

        mRecyclerView.setAdapter(mAdapter);
    }

    public void getGroupMemByGroupId(String groupId) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getGroupMemByGroupId(groupId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<List<AllGroupMembersResponse>>() {
                    @Override
                    public void accept(List<AllGroupMembersResponse> allGroupMembersResponses) throws Exception {
                         list.addAll(allGroupMembersResponses);
                         mAdapter.setNewData(list);
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
            List<AllGroupMembersResponse> groupnamelist = search(groupname); //查找对应的群组数据
            mAdapter.setNewData(list);
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
    private List<AllGroupMembersResponse> search(String str) {
        List<AllGroupMembersResponse> filterList = new ArrayList<AllGroupMembersResponse>();// 过滤后的list
        if (str.matches("^([0-9]|[/+]).*")) {// 正则表达式 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
            String simpleStr = str.replaceAll("\\-|\\s", "");
            for (AllGroupMembersResponse contact : list) {
                if (contact.getNick() != null) {
                    if (contact.getNick().contains(simpleStr) || contact.getNick().contains(str)) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        } else {
            for (AllGroupMembersResponse contact : list) {
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
