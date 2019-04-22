package com.zxjk.duoduo.ui.grouppage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GetAllPlayGroupResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.adapter.AllGroupAdapter;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class AllGroupActivity extends BaseActivity {

    private EditText etSearch;
    private RecyclerView recycler;
    private AllGroupAdapter adapter;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_group);

        etSearch = findViewById(R.id.etSearch);
        recycler = findViewById(R.id.recycler);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AllGroupAdapter();
        recycler.setAdapter(adapter);

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getAllPlayGroup(Constant.userId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(response -> {
                    List<GetAllPlayGroupResponse.GroupListBean> groupList = new ArrayList<>();
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
                }, this::handleApiError);
    }

    public void back(View view) {
        finish();
    }
}
