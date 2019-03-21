package com.zxjk.duoduo.ui.msgpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendListResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.ui.msgpage.adapter.BaseContactAdapter;
import com.zxjk.duoduo.ui.msgpage.utils.PinyinComparator;
import com.zxjk.duoduo.ui.msgpage.widget.HoverItemDecoration;
import com.zxjk.duoduo.ui.msgpage.widget.IndexView;
import com.zxjk.duoduo.ui.msgpage.widget.dialog.DeleteFriendDialog;
import com.zxjk.duoduo.weight.TitleBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;
import io.rong.imkit.tools.CharacterParser;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 通讯录
 */
public class ConstactsNewFriendFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.m_constacts_new_friend_title_bar)
    TitleBar titleBar;

    @BindView(R.id.m_contact_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.index_view)
    IndexView indexView;
    @BindView(R.id.m_constacts_dialog)
    TextView constactsDialog;


    private BaseContactAdapter adapter;


    Unbinder unbinder;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private LinearLayoutManager layoutManager;

    List<FriendListResponse> list = new ArrayList<>();

    public static ConstactsNewFriendFragment newInstance() {
        ConstactsNewFriendFragment fragment = new ConstactsNewFriendFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_constacts_new_friend, null);
        unbinder=ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initView() {
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        list = filledData(list);
        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        //一行代码实现吸顶悬浮的效果
        mRecyclerView.addItemDecoration(new HoverItemDecoration(getContext(), new HoverItemDecoration.BindItemTextCallback() {
            @Override
            public String getItemText(int position) {
                //悬浮的信息
                return list.get(position).getSortLetters();
            }
        }));
        adapter = new BaseContactAdapter();
        getFriendListInfoById();
        mRecyclerView.setAdapter(adapter);
        initIndexView();
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), PeopleInformationActivity.class);
                intent.putExtra("peopleInformatinoUserId", list.get(position).getId());
                getActivity().startActivity(intent);
            }
        });
    }

    /**
     * 初始化右边字幕索引view
     */
    private void initIndexView() {
        indexView.setShowTextDialog(constactsDialog);
        indexView.setOnTouchingLetterChangedListener(new IndexView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String letter) {
                // 该字母首次出现的位置
                int position = getPositionForSection(letter);
                if (position != -1) {
                    layoutManager.scrollToPositionWithOffset(position, 0);
                    layoutManager.setStackFromEnd(false);
                }
            }
        });
    }

    public int getPositionForSection(String section) {
        for (int i = 0; i < list.size(); i++) {
            String sortStr = list.get(i).getSortLetters();
            if (sortStr.equals(section)) {
                return i;
            }
        }
        return -1;
    }


    private List<FriendListResponse> filledData(List<FriendListResponse> sortList) {
        for (int i = 0; i < sortList.size(); i++) {
            if ("".equals(sortList.get(i).getRealname())) {
                sortList.get(i).setSortLetters("#");
            } else {
                // 汉字转换成拼音
                String pinyin = characterParser.getSelling(sortList.get(i).getRealname());
                String sortString = pinyin.substring(0, 1).toUpperCase();

                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sortList.get(i).setSortLetters(sortString.toUpperCase());
                } else {
                    sortList.get(i).setSortLetters("#");
                }
            }
        }
        Collections.sort(sortList, pinyinComparator);
        return sortList;
    }

    @OnClick({R.id.m_constacts_new_friend_group_chat_btn, R.id.m_contact_add_friend_btn, R.id.m_contact_new_friend_btn, R.id.m_contact_search_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_constacts_new_friend_group_chat_btn:
                ToastUtils.showShort("此功能暂未实现");
                break;
            case R.id.m_contact_add_friend_btn:
                startActivity(new Intent(getActivity(), NewFriendActivity.class));

                break;
            case R.id.m_contact_new_friend_btn:
                AddFriendActivity.start(getActivity());
                break;
            case R.id.m_contact_search_btn:
                SearchActivity.start(getActivity());
                break;
            default:
                break;
        }
    }

    public void getFriendListInfoById() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendListById()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendListResponses -> {
                    list = friendListResponses;
                    adapter.setNewData(friendListResponses);
                    for (int i = 0; i < friendListResponses.size(); i++) {
                        LogUtils.d("DEBUG", friendListResponses.get(i).toString());
                    }

                }, throwable -> LogUtils.d("DDD", throwable.getMessage()));

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
