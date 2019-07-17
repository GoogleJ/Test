package com.zxjk.duoduo.ui.msgpage;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.msgpage.rongIM.CusConversationListAdapter;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.widget.QuickPopup;

public class CusConversationListFragment extends ConversationListFragment {

    @Override
    public ConversationListAdapter onResolveAdapter(Context context) {
        return new CusConversationListAdapter(context);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        handleLongClick((UIConversation) parent.getItemAtPosition(position));
        return true;
    }

    @Override
    public boolean onPortraitItemLongClick(View v, UIConversation data) {
        handleLongClick(data);
        return true;
    }

    private void handleLongClick(UIConversation c) {
        QuickPopup longClickPop = QuickPopupBuilder.with(((Fragment) this).getContext())
                .contentView(R.layout.pop_conversationlist)
                .config(new QuickPopupConfig()
                        .withClick(R.id.tv1, v -> RongIM.getInstance().setConversationToTop(c.getConversationType(),
                                c.getConversationTargetId(), !c.isTop(), null), true)
                        .withClick(R.id.tv2, v -> {
                            RongIM.getInstance().removeConversation(c.getConversationType(), c.getConversationTargetId(), null);
                            RongIM.getInstance().clearMessages(c.getConversationType(), c.getConversationTargetId(), null);
                        }, true))
                .show();

        TextView tvSetTop = longClickPop.findViewById(R.id.tv1);
        tvSetTop.setText(c.isTop() ? "取消置顶" : "置顶会话");
    }

}
