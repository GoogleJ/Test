package com.zxjk.duoduo.ui.msg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.SearchBean;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msg.adapter.SearchAdapter;
import com.zxjk.duoduo.weight.TitleBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class SearchActivity extends BaseActivity {
    @BindView(R.id.m_fragment_search_title_bar)
    TitleBar titleBar;
    @BindView(R.id.m_search_edit)
    EditText searchEdit;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    public static void start(Activity activity){
        Intent intent=new Intent(activity,SearchActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 全部匹配的适配器
     */
    private SearchAdapter adapter;
    /**
     * 所有数据 可以是联网获取 如果有需要可以将其储存在数据库中 我们用简单的String做演示
     */
    private List<SearchBean> wholeList;
    /**
     * 此list用来保存符合我们规则的数据
     */
    private List<SearchBean> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);
        initUI();
        initData();
    }





    protected void initUI() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshUI();
        setListener();
    }





    protected void initData() {

        //假数据  实际开发中请从网络或者数据库获取
        wholeList = new ArrayList<>();
        list = new ArrayList<>();
        SearchBean bean=new SearchBean();
        bean.setUserName("第一天一天");
        bean.setUserName("第二天一天");
        bean.setUserName("第三天一天");
        bean.setUserName("第四天一天");
        bean.setUserName("第五天五天");
        bean.setUserName("第六天一天");
        bean.setUserName("第七天七天");
        bean.setUserName("第一天八天");
        bean.setUserName("第一天九天");
        bean.setUserName("第一天十天");

        bean.setDuoduoId("第一天一天");
        bean.setDuoduoId("第二天一天");
        bean.setDuoduoId("第三天一天");
        bean.setDuoduoId("第四天一天");
        bean.setDuoduoId("第五天五天");
        bean.setDuoduoId("第六天一天");
        bean.setDuoduoId("第七天七天");
        bean.setDuoduoId("第一天八天");
        bean.setDuoduoId("第一天九天");
        bean.setDuoduoId("第一天十天");

        wholeList.add(bean);





        //初次进入程序时 展示全部数据
        list.addAll(wholeList);
    }
    private void refreshUI() {
        if (adapter == null) {
            adapter = new SearchAdapter(this, list);
            mRecyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
    private void setListener() {
        //edittext的监听
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //每次edittext内容改变时执行 控制删除按钮的显示隐藏
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
//                    mImgvDelete.setVisibility(View.GONE);
                } else {
//                    mImgvDelete.setVisibility(View.VISIBLE);
                }
                //匹配文字 变色
                doChangeColor(editable.toString().trim());
            }
        });
        //recyclerview的点击监听
        adapter.setOnItemClickListener(new SearchAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                ToastUtils.showShort("PPP"+pos);
            }
        });

    }
    /**
     * 字体匹配方法
     */
    private void doChangeColor(String text) {
        //clear是必须的 不然只要改变edittext数据，list会一直add数据进来
        list.clear();
        //不需要匹配 把所有数据都传进来 不需要变色
        if (text.equals("")) {
            list.addAll(wholeList);
            //防止匹配过文字之后点击删除按钮 字体仍然变色的问题
            adapter.setText(null);
            refreshUI();
        } else {
            //如果edittext里面有数据 则根据edittext里面的数据进行匹配 用contains判断是否包含该条数据 包含的话则加入到list中
            for (SearchBean i : wholeList) {
                if (i.getUserName().contains(text)) {
                    list.add(i);
                }
                if (i.getDuoduoId().contains(text)){
                    list.add(i);
                }
            }
            //设置要变色的关键字
            adapter.setText(text);
            refreshUI();
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
