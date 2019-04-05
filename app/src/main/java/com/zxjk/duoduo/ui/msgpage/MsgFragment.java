package com.zxjk.duoduo.ui.msgpage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.ContentActivity;
import com.zxjk.duoduo.ui.grouppage.SelectContactActivity;
import com.zxjk.duoduo.ui.msgpage.base.BaseFragment;
import com.zxjk.duoduo.ui.msgpage.widget.CommonPopupWindow;
import com.zxjk.duoduo.weight.TitleBar;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * @author Administrator
 * @// TODO: 2019\3\19 0019 聊天列表
 */
public class MsgFragment extends BaseFragment implements View.OnClickListener  ,CommonPopupWindow.ViewInterface{
    TitleBar titleBar;

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
        titleBar=rootView.findViewById(R.id.title_bar);
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("tag", 3);
                getActivity().startActivity(intent);
            }
        });
        titleBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    return;
                }
                popupWindow = new CommonPopupWindow.Builder(getActivity())
                        .setView(R.layout.pop_msg_top)
                        .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setAnimationStyle(R.style.AnimDown)
                        .setBackGroundLevel(0.5f)
                        .setViewOnclickListener(MsgFragment.this::getChildView)
                        .setOutsideTouchable(true)
                        .create();
                popupWindow.showAsDropDown(titleBar.getRightImageView());
            }
        });
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_group_chat:
                Intent intent = new Intent(getActivity(), SelectContactActivity.class);
                intent.putExtra("addGroupType", 0);
                startActivity(intent);
                break;
            case R.id.invite_friends:
                startActivity(new Intent(getActivity(),AddContactActivity.class));

                break;
            case R.id.scan:
                startActivity(new Intent(getActivity(),QrCodeActivity.class));
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

    @Override
    public void onStop() {
        super.onStop();
        if (popupWindow!=null){
            popupWindow.dismiss();
        }
    }
}
