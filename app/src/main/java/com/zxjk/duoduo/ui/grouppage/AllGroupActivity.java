package com.zxjk.duoduo.ui.grouppage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
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

import io.reactivex.functions.Consumer;

public class AllGroupActivity extends BaseActivity {

    private EditText etSearch;
    private RecyclerView recycler;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_group);

        etSearch = findViewById(R.id.etSearch);
        recycler = findViewById(R.id.recycler);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new AllGroupAdapter());

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getAllPlayGroup(Constant.userId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<GetAllPlayGroupResponse>() {
                    @Override
                    public void accept(GetAllPlayGroupResponse getAllPlayGroupResponse) throws Exception {

                    }
                }, this::handleApiError);
    }

    public void back(View view) {
        finish();
    }
}
