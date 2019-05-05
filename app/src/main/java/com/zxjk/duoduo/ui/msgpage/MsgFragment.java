package com.zxjk.duoduo.ui.msgpage;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.ui.msgpage.widget.CommonPopupWindow;
import com.zxjk.duoduo.ui.walletpage.RecipetQRActivity;
import com.zxjk.duoduo.ui.widget.TitleBar;

import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.message.CommandMessage;

/**
 * @author Administrator
 * 消息模块
 */
public class MsgFragment extends BaseFragment implements View.OnClickListener, CommonPopupWindow.ViewInterface {
    TitleBar titleBar;

    private static final String CONVERSATIONLIST_FRAGMENT_KEY = "conversationlist_fragment_key";
    private CusConversationListFragment mConversationListFragment = null;
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
        titleBar = rootView.findViewById(R.id.title_bar);
        if (!Constant.isVerifyVerision) {
            titleBar.setLeftImageResource(R.drawable.ic_head_address_book);
            titleBar.getLeftImageView().setOnClickListener(v -> startActivity(new Intent(getActivity(), ContactsNewFriendActivity.class)));
        }
        titleBar.getRightImageView().setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                return;
            }
            popupWindow = new CommonPopupWindow.Builder(getActivity())
                    .setView(R.layout.pop_msg_top)
                    .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setAnimationStyle(R.style.AnimDown)
                    .setBackGroundLevel(1.0f)
                    .setViewOnclickListener(MsgFragment.this::getChildView)
                    .setOutsideTouchable(true)
                    .create();
            getPermisson(popupWindow.getContentView().findViewById(R.id.scan), result -> {
                if (result) startActivity(new Intent(getActivity(), QrCodeActivity.class));
                popupWindow.dismiss();
            }, Manifest.permission.CAMERA);
            popupWindow.showAsDropDown(titleBar.getRightImageView());
        });
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.pop_msg_top:
                view.findViewById(R.id.send_group_chat).setOnClickListener(this);
                view.findViewById(R.id.invite_friends).setOnClickListener(this);
                view.findViewById(R.id.collection_and_payment).setOnClickListener(this);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_group_chat:
                Intent intent = new Intent(getActivity(), CreateGroupActivity.class);
                intent.putExtra("eventType", 1);
                startActivity(intent);
                break;
            case R.id.invite_friends:
                startActivity(new Intent(getActivity(), AddContactActivity.class));
                break;
            case R.id.collection_and_payment:
                startActivity(new Intent(getActivity(), RecipetQRActivity.class));
                break;
            default:
        }
    }

    private void initConversationList(Bundle bundle) {
        if (bundle != null) {
            mCurrentFragment = getCurrentFragment(bundle);
            String currentFragmentTag = bundle.getString(CONVERSATIONLIST_FRAGMENT_KEY, CONVERSATIONLIST_FRAGMENT_KEY);
            mConversationListFragment = (CusConversationListFragment) getFragment(currentFragmentTag);
        }

        if (mConversationListFragment == null) {
            mConversationListFragment = createConversationList();
            RongIM.getInstance().setOnReceiveMessageListener((message, i) -> {
                if (message.getContent() instanceof CommandMessage) {
                    RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE
                            , message.getSenderUserId(), null);
                }
                return false;
            });
        }

        switchFragment(mConversationListFragment, R.id.conversationlist);
    }


    private CusConversationListFragment createConversationList() {
        CusConversationListFragment listFragment = new CusConversationListFragment();
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

    @Override
    public void onStop() {
        super.onStop();
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    private static final String CURRENT_FRAGMENT_KEY = "current_fragment_key";
    protected Fragment mCurrentFragment;

    protected <T extends Fragment> T getFragment(String tag) {
        return (T) getChildFragmentManager()
                .findFragmentByTag(tag);
    }

    protected Fragment getCurrentFragment(Bundle bundle) {
        String currentFragmentTag = bundle.getString(CURRENT_FRAGMENT_KEY, CURRENT_FRAGMENT_KEY);
        return getFragment(currentFragmentTag);
    }

    protected void switchFragment(Fragment targetFragment, int contentId) {

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (targetFragment == mCurrentFragment) {
            return;
        }

        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
        }

        if (!targetFragment.isAdded()) {
            transaction.add(contentId, targetFragment, targetFragment.getClass().getSimpleName());
        } else {
            transaction.show(targetFragment);
        }

        transaction.commitAllowingStateLoss();

        mCurrentFragment = targetFragment;

    }
}
