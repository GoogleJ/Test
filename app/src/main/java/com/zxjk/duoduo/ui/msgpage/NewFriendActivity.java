package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendListResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.NewFriendAdapter;
import com.zxjk.duoduo.ui.msgpage.widget.dialog.DeleteFriendDialog;
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
 * @// TODO: 2019\3\20 0020  新的朋友
 */
@SuppressLint("CheckResult")
public class NewFriendActivity extends BaseActivity {
    @BindView(R.id.m_fragment_new_friend_title_bar)
    TitleBar titleBar;
    @BindView(R.id.m_contact_search_edit_1)
    TextView textView;
    @BindView(R.id.m_fragment_new_friend_recycler_view)
    RecyclerView mRecyclerView;

    NewFriendAdapter mAdapter;
    DeleteFriendDialog dialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_friend);
        ButterKnife.bind(this);
        initUI();
    }
    List<FriendListResponse> list=new ArrayList<>();

    @SuppressLint("WrongConstant")

    protected void initUI() {

        titleBar.getLeftImageView().setOnClickListener(v -> finish());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new NewFriendAdapter();
        getMyFriendsWaiting();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {

            View vp = (View)view.getParent();

            TextView typeBtn=view.findViewById(R.id.m_item_new_friend_type_btn);
            TextView mark=vp.findViewById(R.id.m_item_new_friend_message_label);
            switch (view.getId()) {
                case R.id.m_item_new_friend_type_btn:
                    typeBtn.setBackgroundColor(Color.WHITE);
                    typeBtn.setText("已添加");
                    typeBtn.setTextColor(Color.GRAY);


                    addFriend(list.get(position).getId(),mark.getText().toString(),position);


                    break;
                case R.id.m_add_btn_layout:
                    Intent intent=new Intent(NewFriendActivity.this, AddFriendDetailsActivity.class);
                    intent.putExtra("newFriendActivityUserId",list.get(position).getId());
                    intent.putExtra("type",1);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            mAdapter.notifyDataSetChanged();
        });
        mAdapter.setOnItemChildLongClickListener((adapter, view, position) -> {
            dialog=new DeleteFriendDialog(NewFriendActivity.this);
            dialog.show();
            dialog.setOnClickListener(() -> deleteMyfirendsWaiting(list.get(position).getId()));
            return false;
        });
      textView.setOnClickListener(v -> startActivity(new Intent(NewFriendActivity.this,SearchActivity.class)));
    }

    /**
     * 获取待添加好友列表
     */
    public void getMyFriendsWaiting() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getMyFirendsWaiting()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<List<FriendListResponse>>() {
                    @Override
                    public void accept(List<FriendListResponse> s) throws Exception {
                        FriendListResponse friendListResponse=null;
                        for (int i=0;i<s.size();i++){
                            friendListResponse =s.get(i);
                        }
                        if (friendListResponse.getId().equals(Constant.userId)){
                            mAdapter.getData().remove(Constant.userId);
                            mAdapter.setNewData(s);
                        }
                        list=s;
                    }
                }, this::handleApiError);
    }

    /**
     * 同意添加
     * @param friendId
     * @param markName
     */
    public void addFriend(String friendId,String markName,int position){
        ServiceFactory.getInstance().getBaseService(Api.class)
                .addFriend(friendId,markName)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    ToastUtils.showShort("添加好友成功");
                    LogUtils.d("DEBUG",s);
                    //这里顺便加入值的传递，用好友列表中的id比对获取待添加好友列表的Id，然后进行删除已有的东西
                    if (mAdapter.getData().size()>=0){
                        mAdapter.getData().remove(position);
                        mAdapter.notifyDataSetChanged();
                    }
                },this::handleApiError);
    }

    /**
     * 删除好友申请
     * @param friendId
     */

    public void deleteMyfirendsWaiting(String friendId){
        ServiceFactory.getInstance().getBaseService(Api.class)
                .deleteMyfirendsWaiting(friendId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                    }
                },this::handleApiError);
    }


}
