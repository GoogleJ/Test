package com.zxjk.duoduo.ui.msgpage;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GroupResponse;
import com.zxjk.duoduo.ui.base.ContentActivity;
import com.zxjk.duoduo.ui.grouppage.SelectContactActivity;
import com.zxjk.duoduo.ui.msgpage.base.BaseFragment;
import com.zxjk.duoduo.ui.msgpage.widget.CommonPopupWindow;
import com.zxjk.duoduo.utils.DensityUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * @author Administrator
 * @// TODO: 2019\3\19 0019 聊天列表
 */
public class MsgFragment extends BaseFragment implements View.OnClickListener, CommonPopupWindow.ViewInterface {

    private static final String CONVERSATIONLIST_FRAGMENT_KEY = "conversationlist_fragment_key";
    private ConversationListFragment mConversationListFragment = null;
    private CommonPopupWindow popupWindow;


    View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, rootView);
        initHear();

        initConversationList(null);


        return rootView;
    }

    private void initHear() {
        setHeadStatusBar();
        setHeadTitle(R.string.message);
        Button leftView = getLeftBtn(20, 20);
        leftView.setBackgroundResource(R.drawable.ic_head_address_book);

        Button rightView = getRightBtn(20, 20);
        rightView.setBackgroundResource(R.drawable.ic_head_add);
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
//                startActivity(new Intent(getActivity(), SelectContactActivity.class));
                Intent intent = new Intent(getActivity(), SelectContactActivity.class);
                intent.putExtra("addGroupType", 0);
                startActivity(intent);

                break;
            case R.id.invite_friends:
                AddContactActivity.start(getActivity());
                break;
            case R.id.scan:
                QrCodeActivity.start(getActivity());
                break;
            case R.id.collection_and_payment:
                break;
            default:
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
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
                //群组
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")
                //公共服务号
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "true")
                //订阅号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "true")
                //系统
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")
                .build();

        listFragment.setUri(uri);

        return listFragment;
    }

    public Button getLeftBtn() {
        return ((Button) rootView.findViewById(R.id.btn_head_left));
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
        return ((Button) rootView.findViewById(R.id.btn_head_right));
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
        View view1 = rootView.findViewById(R.id.head_StatusBar);
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
        ((TextView) rootView.findViewById(R.id.tv_head_title)).setText(resId);
    }

    public void setHeadTitle(String title) {
        ((TextView) rootView.findViewById(R.id.tv_head_title)).setText(title);
    }
}
