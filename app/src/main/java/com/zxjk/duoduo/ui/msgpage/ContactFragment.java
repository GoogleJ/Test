package com.zxjk.duoduo.ui.msgpage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.blankj.utilcode.util.LogUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.UserBean;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendListResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.ui.msgpage.adapter.BaseContactAdapter;
import com.zxjk.duoduo.ui.msgpage.utils.PinyinComparator;
import com.zxjk.duoduo.ui.msgpage.widget.HoverItemDecoration;
import com.zxjk.duoduo.ui.msgpage.widget.IndexView;
import com.zxjk.duoduo.weight.TitleBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import io.rong.imkit.tools.CharacterParser;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 手机联系人界面
 */
public class ContactFragment extends BaseFragment {

    @BindView(R.id.m_contact_title_bar)
    TitleBar titleBar;
    @BindView(R.id.previewText)
    TextView textView;
    @BindView(R.id.index_view)
    IndexView indexView;
    @BindView(R.id.m_contact_recycler_view)
    RecyclerView mRecyclerView;


    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private LinearLayoutManager layoutManager;

    BaseContactAdapter adapter;

    List<FriendListResponse> friendListResponseList = new ArrayList<>();


    public static Fragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, null);
        ButterKnife.bind(this, view);
        initUI();
        initData();

        return view;
    }

    private void initData() {
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();


        friendListResponseList = filledData(friendListResponseList);

        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        //一行代码实现吸顶悬浮的效果
        mRecyclerView.addItemDecoration(new HoverItemDecoration(getContext(), new HoverItemDecoration.BindItemTextCallback() {
            @Override
            public String getItemText(int position) {
                //悬浮的信息
                return friendListResponseList.get(position).getSortLetters();
            }
        }));

        adapter = new BaseContactAdapter();
        getFriendListInfoById();
        mRecyclerView.setAdapter(adapter);

        initIndexView();
    }

    protected void initUI() {
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    /**
     * 初始化右边字幕索引view
     */
    private void initIndexView() {
        indexView.setShowTextDialog(textView);
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
        for (int i = 0; i < friendListResponseList.size(); i++) {
            String sortStr = friendListResponseList.get(i).getSortLetters();
            if (sortStr.equals(section)) {
                return i;
            }
        }
        return -1;
    }


    private List<FriendListResponse> filledData(List<FriendListResponse> sortList) {

        for (int i = 0; i < sortList.size(); i++) {

            if ("".equals(sortList.get(i).getNick())) {
                sortList.get(i).setSortLetters("#");
            } else {
                // 汉字转换成拼音
                String pinyin = characterParser.getSelling(sortList.get(i).getNick());
                String sortString = pinyin.substring(0, 1).toUpperCase();

                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sortList.get(i).setSortLetters(sortString.toUpperCase());
                } else {
                    sortList.get(i).setSortLetters("#");
                }
            }
        }
        // 根据a-z进行排序源数据
        Collections.sort(sortList, pinyinComparator);

        return sortList;
    }

    public void getFriendListInfoById() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendListById()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendListResponses -> {

                    adapter.setNewData(friendListResponses);
                    friendListResponseList = friendListResponses;
                    for (int i = 0; i < friendListResponses.size(); i++) {
                        LogUtils.d("DEBUG", friendListResponses.get(i).toString());
                    }
                }, throwable -> LogUtils.d("DDD", throwable.getMessage()));

    }
}
