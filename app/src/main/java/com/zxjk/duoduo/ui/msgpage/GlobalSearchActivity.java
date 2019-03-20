package com.zxjk.duoduo.ui.msgpage;


import android.app.Activity;
import android.app.Person;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.SearchCustomerInfoResponse;
import com.zxjk.duoduo.network.response.SearchResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.base.ContentActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.GlobalSearchAdapter;
import com.zxjk.duoduo.ui.msgpage.adapter.SearchAdapter;
import com.zxjk.duoduo.weight.TitleBar;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 全局搜索
 */
public class GlobalSearchActivity extends BaseActivity {
    @BindView(R.id.m_search_title_bar)
    TitleBar titleBar;
    @BindView(R.id.m_search_edit)
    EditText searchEdit;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    GlobalSearchAdapter mAdapter;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, GlobalSearchActivity.class);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_search);
        ButterKnife.bind(this);
        initData();
        initUI();
    }
    List<SearchCustomerInfoResponse> list=new ArrayList<>();
    private void initData() {
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //搜索按键action
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    LogUtils.d("开始搜索");
                    searchCustomerInfo(searchEdit.getText().toString());
                    return true;
                }
                return false;
            }
        });

    }

    private void initUI() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new GlobalSearchAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                Intent intent=new Intent(GlobalSearchActivity.this, PersonalInformationActivity.class);
                intent.putExtra("userId",list.get(position).getId());
                startActivity(intent);


            }
        });
        mAdapter.notifyDataSetChanged();

    }
    public void searchCustomerInfo(String data) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .searchCustomerInfo(data)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<List<SearchCustomerInfoResponse>>() {
                    @Override
                    public void accept(List<SearchCustomerInfoResponse> searchCustomerInfoResponses) throws Exception {
                        mAdapter.setNewData(searchCustomerInfoResponses);
                        for (int i = 0; i < searchCustomerInfoResponses.size(); i++) {
                            LogUtils.d("DEBUG", searchCustomerInfoResponses.get(i).toString());
                        }
                        list=searchCustomerInfoResponses;

                    }
                }, throwable -> LogUtils.d("DEBUG", throwable.getMessage()));

    }
}
