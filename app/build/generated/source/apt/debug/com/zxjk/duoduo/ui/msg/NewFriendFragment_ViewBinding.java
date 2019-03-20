// Generated code from Butter Knife. Do not modify!
package com.zxjk.duoduo.ui.msg;

import android.view.View;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.weight.TitleBar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NewFriendFragment_ViewBinding implements Unbinder {
  private NewFriendFragment target;

  @UiThread
  public NewFriendFragment_ViewBinding(NewFriendFragment target, View source) {
    this.target = target;

    target.titleBar = Utils.findRequiredViewAsType(source, R.id.m_fragment_new_friend_title_bar, "field 'titleBar'", TitleBar.class);
    target.editText = Utils.findRequiredViewAsType(source, R.id.m_contact_search_edit_1, "field 'editText'", EditText.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.m_fragment_new_friend_recycler_view, "field 'mRecyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    NewFriendFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.titleBar = null;
    target.editText = null;
    target.mRecyclerView = null;
  }
}
