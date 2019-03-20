// Generated code from Butter Knife. Do not modify!
package com.zxjk.duoduo.ui.msgpage;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.msgpage.widget.IndexView;
import com.zxjk.duoduo.weight.TitleBar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ConstactsNewFriendFragment_ViewBinding implements Unbinder {
  private ConstactsNewFriendFragment target;

  private View view7f090145;

  private View view7f090149;

  private View view7f09014c;

  private View view7f090150;

  @UiThread
  public ConstactsNewFriendFragment_ViewBinding(final ConstactsNewFriendFragment target,
      View source) {
    this.target = target;

    View view;
    target.titleBar = Utils.findRequiredViewAsType(source, R.id.m_constacts_new_friend_title_bar, "field 'titleBar'", TitleBar.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.m_contact_recycler_view, "field 'mRecyclerView'", RecyclerView.class);
    target.indexView = Utils.findRequiredViewAsType(source, R.id.index_view, "field 'indexView'", IndexView.class);
    target.constactsDialog = Utils.findRequiredViewAsType(source, R.id.m_constacts_dialog, "field 'constactsDialog'", TextView.class);
    view = Utils.findRequiredView(source, R.id.m_constacts_new_friend_group_chat_btn, "method 'onClick'");
    view7f090145 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.m_contact_add_friend_btn, "method 'onClick'");
    view7f090149 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.m_contact_new_friend_btn, "method 'onClick'");
    view7f09014c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.m_contact_search_btn, "method 'onClick'");
    view7f090150 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ConstactsNewFriendFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.titleBar = null;
    target.mRecyclerView = null;
    target.indexView = null;
    target.constactsDialog = null;

    view7f090145.setOnClickListener(null);
    view7f090145 = null;
    view7f090149.setOnClickListener(null);
    view7f090149 = null;
    view7f09014c.setOnClickListener(null);
    view7f09014c = null;
    view7f090150.setOnClickListener(null);
    view7f090150 = null;
  }
}
