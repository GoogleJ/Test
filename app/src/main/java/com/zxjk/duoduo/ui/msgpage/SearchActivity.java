package com.zxjk.duoduo.ui.msgpage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.SearchBean;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.SearchCustomerInfoResponse;
import com.zxjk.duoduo.network.response.SearchResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.HomeActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.base.ContentActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.SearchAdapter;
import com.zxjk.duoduo.weight.TitleBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 搜索页面，需要添加模糊搜索
 */
public class SearchActivity extends BaseActivity {
    @BindView(R.id.m_fragment_search_title_bar)
    TitleBar titleBar;
    @BindView(R.id.m_search_edit)
    EditText searchEdit;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    SearchAdapter mAdapter;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
    }

    List<SearchCustomerInfoResponse> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);
        ButterKnife.bind(this);
        initData();
        initUI();

    }

    private void initData() {
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //搜索按键action
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    LogUtils.d("开始搜索");
                    searchFriendInfo(searchEdit.getText().toString());
                    return true;
                }
                return false;
            }
        });

    }

    private void initUI() {
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new SearchAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                //这里跳转到已添加的可以发消息的好友
                Intent intent = new Intent(SearchActivity.this, PersonalInformationActivity.class);
                intent.putExtra("searchUserId", list.get(position).getId());
                startActivity(intent);

            }
        });
        mAdapter.notifyDataSetChanged();

    }

    public void searchFriendInfo(String data) {
        //模糊搜索用户
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
                        list = searchCustomerInfoResponses;


                    }
                }, throwable -> LogUtils.d("DEBUG", throwable.getMessage()));

    }

}
