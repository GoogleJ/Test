package com.zxjk.duoduo.ui.msgpage;

import android.content.Context;
import com.zxjk.duoduo.ui.msgpage.adapter.CusConversationListAdapter;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.widget.adapter.ConversationListAdapter;

public class CusConversationListFragment extends ConversationListFragment {

    @Override
    public ConversationListAdapter onResolveAdapter(Context context) {
        return new CusConversationListAdapter(context);
    }
}
