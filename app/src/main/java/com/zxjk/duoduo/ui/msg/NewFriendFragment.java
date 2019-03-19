package com.zxjk.duoduo.ui.msg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.msg.base.BaseFragment;
import com.zxjk.duoduo.weight.TitleBar;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class NewFriendFragment extends BaseFragment {
    @BindView(R.id.m_fragment_new_friend_title_bar)
    TitleBar titleBar;
    @BindView(R.id.m_contact_search_edit_1)
    EditText editText;
    @BindView(R.id.m_fragment_new_friend_recycler_view)
    RecyclerView mRecyclerView;


    NewFriendAdapter mAdapter;
    //填充的假数据
    List<TestBean> list;



    public static Fragment newInstance(){
        NewFriendFragment fragment=new NewFriendFragment();
        Bundle bundle =new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    protected int getRootViewId() {
        return R.layout.fragment_new_friend;
    }

    @Override
    protected void onAfterContentView(Bundle savedInstanceState) {

    }



    @Override
    protected void initUI() {
        super.initUI();
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().finish();
            }
        });
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter=new NewFriendAdapter();
        list=new ArrayList<>();
        for (int i=0;i<20;i++){
            TestBean bean=new TestBean();
            bean.setMessageLabel("这是我的第"+i+"次会话");
            bean.setUserName("15129092372"+i);
            bean.setType("添加");
           list.add(bean);
        }
        mAdapter.setNewData(list);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("tag",0);
                getActivity().startActivity(intent);
            }
        });
    }
}
