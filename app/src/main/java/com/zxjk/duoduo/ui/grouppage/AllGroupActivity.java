package com.zxjk.duoduo.ui.grouppage;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.bean.response.GetAllPlayGroupResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.adapter.AllGroupAdapter;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * author L
 * create at 2019/5/7
 * description: 一起游戏
 */
@SuppressLint("SetTextI18n")
public class AllGroupActivity extends BaseActivity {

    private EditText etSearch;
    private RecyclerView recycler;
    private AllGroupAdapter adapter;
    private List<GetAllPlayGroupResponse.GroupListBean> groupList;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_group);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.game_together));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());

        etSearch = findViewById(R.id.etSearch);
        recycler = findViewById(R.id.recycler);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    adapter.setData(groupList);
                } else {
                    adapter.setData(search(s.toString()));
                }
            }
        });

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AllGroupAdapter();
        recycler.setAdapter(adapter);

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getAllPlayGroup(Constant.userId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(response -> {
                    groupList = new ArrayList<>();
                    groupList.addAll(response.getGroupList());
                    for (GetAllPlayGroupResponse.GroupListBean bean : response.getGroupList()) {
                        for (String groupid : response.getJoin()) {
                            if (groupid.equals(bean.getId())) {
                                groupList.remove(bean);
                                bean.setHasJoined(true);
                                groupList.add(0, bean);
                            }
                        }
                    }
                    adapter.setData(groupList);
                    adapter.setOnItemClickLitener(position -> {
                        GetAllPlayGroupResponse.GroupListBean groupListBean = groupList.get(position);
                        NiceDialog.init().setLayoutId(R.layout.layout_general_dialog1).setConvertListener(new ViewConvertListener() {

                            @Override
                            protected void convertView(ViewHolder viewHolder, BaseNiceDialog baseNiceDialog) {
                                TextView tv_title = viewHolder.getView(R.id.tv_title);
                                TextView tv_content = viewHolder.getView(R.id.tv_content);
                                TextView tv_notarize = viewHolder.getView(R.id.tv_notarize);
                                tv_title.setText(getString(R.string.hint_duoduo_id));
                                tv_content.setText(getString(R.string.hint_duoduo_id2) + " " + groupListBean.getDuoduoId());
                                tv_notarize.setText(getString(R.string.fuzhi_duoduo_id));
                                viewHolder.getView(R.id.tv_notarize).setOnClickListener(v -> {
                                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    cm.setText(groupListBean.getDuoduoId());
                                    ToastUtils.showShort(getString(R.string.duplicated_to_clipboard));
                                    baseNiceDialog.dismiss();

                                });
                            }
                        }).setDimAmount(0.5f)
                                .setOutCancel(true)
                                .show(getSupportFragmentManager());
                    });
                }, this::handleApiError);
    }

    private List<GetAllPlayGroupResponse.GroupListBean> search(String str) {
        List<GetAllPlayGroupResponse.GroupListBean> filterList = new ArrayList<>();// 过滤后的list
        if (str.matches("^([0-9]|[/+]).*")) {// 正则表达式 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
            String simpleStr = str.replaceAll("\\-|\\s", "");
            for (GetAllPlayGroupResponse.GroupListBean contact : groupList) {
                if (contact.getGroupNikeName() != null) {
                    if (contact.getGroupNikeName().contains(simpleStr) || contact.getGroupNikeName().contains(str)) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        } else {
            for (GetAllPlayGroupResponse.GroupListBean contact : groupList) {
                if (contact.getGroupNikeName() != null) {
                    //姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
                    boolean isNameContains = contact.getGroupNikeName().toLowerCase(Locale.CHINESE)
                            .contains(str.toLowerCase(Locale.CHINESE));

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
