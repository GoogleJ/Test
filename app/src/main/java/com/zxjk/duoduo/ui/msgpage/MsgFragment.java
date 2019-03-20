package com.zxjk.duoduo.ui.msgpage;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.ContentActivity;
import com.zxjk.duoduo.ui.msgpage.RongIMAdapter.ConversationListAdapterEx;
import com.zxjk.duoduo.ui.msgpage.base.BaseFragment;
import com.zxjk.duoduo.ui.msgpage.widget.CommonPopupWindow;
import com.zxjk.duoduo.utils.DensityUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * @author Administrator
 * @// TODO: 2019\3\19 0019 聊天列表
 */
public class MsgFragment extends BaseFragment implements View.OnClickListener, CommonPopupWindow.ViewInterface {

    private static final String CONVERSATIONLIST_FRAGMENT_KEY = "conversationlist_fragment_key";
    private ConversationListFragment mConversationListFragment = null;
    private Conversation.ConversationType[] mConversationsTypes = null;
    private CommonPopupWindow popupWindow;

    public static MsgFragment newInstance() {
        MsgFragment fragment = new MsgFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initConversationList(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, null);
        ButterKnife.bind(this, view);

        initHear();
        return view;
    }

    private void initHear() {
        setHeadStatusBar();
        setHeadTitle(R.string.message);
        Button leftView = getLeftBtn(20, 20);
        leftView.setBackgroundResource(R.drawable.ic_head_address_book);

        Button rightView = getRightBtn(20, 20);
        rightView.setBackgroundResource(R.drawable.ic_head_add);
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                conversations.size();
                if (conversations != null || conversations.size() != 0) {
                    for (int i = 0; i < conversations.size(); i++) {
                        Log.i("GJson", "好饿呀！" + conversations.get(i));
                    }
                } else {

                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.i("GJson", "" + errorCode.getMessage());
            }
        });
//        RongIM.getInstance().refreshUserInfoCache(userInfo);
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put(Conversation.ConversationType.PRIVATE.getName(), false);//会话类型
//        RongIM.getInstance().startConversationList(context,map);

    }


    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.pop_msg_top:
                view.findViewById(R.id.send_group_chat).setOnClickListener(this);
                view.findViewById(R.id.invite_friends).setOnClickListener(this);
                view.findViewById(R.id.scan).setOnClickListener(this);
                view.findViewById(R.id.collection_and_payment).setOnClickListener(this);
                break;
        }
    }


    @OnClick(R.id.btn_head_right)
    public void showMsgMenuPop(View view) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        popupWindow = new CommonPopupWindow.Builder(getActivity())
                .setView(R.layout.pop_msg_top)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimDown)
                .setBackGroundLevel(0.5f)
                .setViewOnclickListener(this)
                .setOutsideTouchable(true)
                .create();
        popupWindow.showAsDropDown(view);
    }

    @OnClick(R.id.btn_head_left)
    public void showForContactFragment() {
        Intent intent = new Intent(getActivity(), ContentActivity.class);
        intent.putExtra("tag", 3);
        getActivity().startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_group_chat:
//                SendGroupChatActivity.start(getActivity());
                break;
            case R.id.invite_friends:
                AddFriendActivity.start(getActivity());
                break;
            case R.id.scan:
                QrCodeActivity.start(getActivity());
                break;
            case R.id.collection_and_payment:
                break;
        }
    }

    private void initConversationList(Bundle bundle) {
        if (bundle != null) {
            mCurrentFragment = getCurrentFragment(bundle);
            String currentFragmentTag = bundle.getString(CONVERSATIONLIST_FRAGMENT_KEY, CONVERSATIONLIST_FRAGMENT_KEY);
            mConversationListFragment = (ConversationListFragment) getFragment(currentFragmentTag);
        }

        if (mConversationListFragment == null) {
            mConversationListFragment = createConversationList();
        }

        switchFragment(mConversationListFragment, R.id.conversationlist);

    }

    private ConversationListFragment createConversationList() {
        ConversationListFragment listFragment = new ConversationListFragment();
        listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
                //群组
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
                //公共服务号
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "true")
                //订阅号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "true")
                //系统
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")
                .build();
        mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.PUBLIC_SERVICE,
                Conversation.ConversationType.APP_PUBLIC_SERVICE,
                Conversation.ConversationType.SYSTEM
        };


        listFragment.setUri(uri);

        return listFragment;
    }

    //设置容云用户信息
    private void setRongUserInfo(final String targetid) {
        if (RongIM.getInstance() != null) {
            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                @Override
                public UserInfo getUserInfo(String s) {

                    return null;
                }
            }, true);
        }

    }

    public Button getLeftBtn() {
        return ((Button) view.findViewById(R.id.btn_head_left));
    }

    public Button getLeftBtn(int width, int height) {
        Button leftView = getLeftBtn();
        ViewGroup.LayoutParams leftViewLayoutParams = leftView.getLayoutParams();
        leftViewLayoutParams.width = DensityUtils.dip2px(getActivity(), width);
        leftViewLayoutParams.height = DensityUtils.dip2px(getActivity(), height);
        leftView.setLayoutParams(leftViewLayoutParams);
        return leftView;
    }

    public Button getRightBtn() {
        return ((Button) view.findViewById(R.id.btn_head_right));
    }

    public Button getRightBtn(int width, int height) {
        Button rightView = getRightBtn();
        ViewGroup.LayoutParams rightViewLayoutParams = rightView.getLayoutParams();
        rightViewLayoutParams.width = DensityUtils.dip2px(getActivity(), width);
        rightViewLayoutParams.height = DensityUtils.dip2px(getActivity(), height);
        rightView.setLayoutParams(rightViewLayoutParams);
        return rightView;
    }

    public void setHeadStatusBar() {
        View view1 = view.findViewById(R.id.head_StatusBar);
        ViewGroup.LayoutParams layoutParams = view1.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
        view1.setLayoutParams(layoutParams);
    }

    protected int getStatusBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public void setHeadTitle(int resId) {
        ((TextView) view.findViewById(R.id.tv_head_title)).setText(resId);
    }

    public void setHeadTitle(String title) {
        ((TextView) view.findViewById(R.id.tv_head_title)).setText(title);
    }
}
