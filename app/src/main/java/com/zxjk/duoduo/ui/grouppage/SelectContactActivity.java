package com.zxjk.duoduo.ui.grouppage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.adapter.AddGroupTopAdapter;
import com.zxjk.duoduo.ui.grouppage.adapter.SelectContactAdapter;
import com.zxjk.duoduo.utils.CommonUtils;

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\28 0028 选择联系人
 */
@SuppressLint("CheckResult")
public class SelectContactActivity extends BaseActivity implements View.OnClickListener , TextWatcher {
    /**
     * 确定按钮
     */
    TextView titleRight;
    /**
     * 返回按钮
     */
    ImageView titleBarLeftIamge;
    /**
     * 选中后在上方展示的RecyclerView
     */
    RecyclerView selectRecycler;
    /**
     * 选中添加的群成员
     */
    RecyclerView recyclerView;
    /**
     * 搜索好友
     *
     * @param savedInstanceState
     */
    EditText searchEdit;
    SelectContactAdapter mAdapter;
    AddGroupTopAdapter topAdapter;

    private boolean fromZhuanChu;


    int position;
    List<FriendInfoResponse> lists = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        fromZhuanChu = getIntent().getBooleanExtra("fromZhuanChu", false);

        initView();
    }

    List<FriendInfoResponse> list = new ArrayList<>();

    private void initView() {
        titleBarLeftIamge = findViewById(R.id.title_left_image);
        titleRight = findViewById(R.id.title_right);
        selectRecycler = findViewById(R.id.recycler_view_select);
        recyclerView = findViewById(R.id.all_members_recycler_view);
        searchEdit = findViewById(R.id.search_select_contact);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        mAdapter = new SelectContactAdapter();
        getFriendListById();
        recyclerView.setAdapter(mAdapter);
        titleBarLeftIamge.setOnClickListener(this);
        titleRight.setOnClickListener(this);
        searchEdit.addTextChangedListener(SelectContactActivity.this);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (fromZhuanChu) {
                String walletAddress = list.get(position).getWalletAddress();
                Intent intent = new Intent();
                intent.putExtra("walletAddress", walletAddress);
                setResult(3, intent);
                finish();
                return;
            }
            selectRecycler.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SelectContactActivity.this);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            selectRecycler.setLayoutManager(linearLayoutManager);
            topAdapter = new AddGroupTopAdapter();
            this.position = position;
            FriendInfoResponse response;

            for (int i = 0; i <list.size(); i++) {
                if (list.get(position).getId().equals(list.get(i).getId())) {
                    response = new FriendInfoResponse();
                    response.setId(list.get(i).getId());
                    response.setHeadPortrait(list.get(i).getHeadPortrait());
                    lists.add(response);
                }
            }



//            lists = getRemoveList(lists);
            topAdapter.setNewData(lists);
            selectRecycler.setAdapter(topAdapter);
            topAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if (topAdapter.getData().size() >= 0) {
                        topAdapter.getData().remove(position);
                        mAdapter.notifyDataSetChanged();
                        topAdapter.notifyDataSetChanged();
                    } else {
                        return;
                    }

                }
            });
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_image:
                finish();
                break;
            case R.id.title_right:
                int types = 0;
                int type = getIntent().getIntExtra("addGroupType", types);

                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < lists.size(); i++) {
                    sb.append(lists.get(i).getId());
                    sb.append(",");
                }
                if (type == 0) {
                    makeGroup(Constant.userId, Constant.userId + "," + sb.substring(0, sb.length() - 1));
                } else {
                    enterGroup(getIntent().getStringExtra("groupId"), Constant.userId, sb.substring(0, sb.length() - 1));
                }
                break;
            default:
                break;
        }

    }

    /**
     * 获取好友列表
     */
    public void getFriendListById() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendListById()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<List<FriendInfoResponse>>() {
                    @Override
                    public void accept(List<FriendInfoResponse> friendInfoResponses) throws Exception {
                        mAdapter.setNewData(friendInfoResponses);
                        list = friendInfoResponses;
                    }
                }, this::handleApiError);
    }

    /**
     * 创建群
     */

    public void makeGroup(String groupOwnerId, String customerIds) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .makeGroup(groupOwnerId, customerIds)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    SelectContactActivity.this.finish();
                    Intent intent = new Intent(SelectContactActivity.this, AgreeGroupChatActivity.class);
                    intent.putExtra("groupId", s.getId());
                    startActivity(intent);
                }, this::handleApiError);
    }

    /**
     * 关于同意添加群的实现
     *
     * @param groupId
     * @param inviterId
     * @param customerIds
     */
    public void enterGroup(String groupId, String inviterId, String customerIds) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .enterGroup(groupId, inviterId, customerIds)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ToastUtils.showShort(getString(R.string.add_group_chat));
                    }
                }, this::handleApiError);

    }

    /**
     * 得到去除重复后的集合
     *
     * @param list
     * @return
     */
    private static List<FriendInfoResponse> getRemoveList(List<FriendInfoResponse> list) {
        Set set = new HashSet();
        List<FriendInfoResponse> newList = new ArrayList<>();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            FriendInfoResponse object = (FriendInfoResponse) iter.next();
            if (set.add(object)) {
                newList.add(object);
            }
        }
        return newList;
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
