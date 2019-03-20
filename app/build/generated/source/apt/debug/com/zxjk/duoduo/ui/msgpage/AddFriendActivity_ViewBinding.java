// Generated code from Butter Knife. Do not modify!
package com.zxjk.duoduo.ui.msgpage;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.weight.TitleBar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AddFriendActivity_ViewBinding implements Unbinder {
  private AddFriendActivity target;

  private View view7f090132;

  private View view7f090127;

  private View view7f090175;

  private View view7f090130;

  private View view7f09012c;

  @UiThread
  public AddFriendActivity_ViewBinding(AddFriendActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AddFriendActivity_ViewBinding(final AddFriendActivity target, View source) {
    this.target = target;

    View view;
    target.titleBar = Utils.findRequiredViewAsType(source, R.id.m_add_friend_title_bar, "field 'titleBar'", TitleBar.class);
    view = Utils.findRequiredView(source, R.id.m_add_friend_wechat_btn, "method 'onClick'");
    view7f090132 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.m_add_friend_contact_btn, "method 'onClick'");
    view7f090127 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.m_my_qr_code_btn, "method 'onClick'");
    view7f090175 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.m_add_friend_search_edit, "method 'onClick'");
    view7f090130 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.m_add_friend_scan_it_btn, "method 'onClick'");
    view7f09012c = view;
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
    AddFriendActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.titleBar = null;

    view7f090132.setOnClickListener(null);
    view7f090132 = null;
    view7f090127.setOnClickListener(null);
    view7f090127 = null;
    view7f090175.setOnClickListener(null);
    view7f090175 = null;
    view7f090130.setOnClickListener(null);
    view7f090130 = null;
    view7f09012c.setOnClickListener(null);
    view7f09012c = null;
  }
}
