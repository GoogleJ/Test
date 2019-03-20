// Generated code from Butter Knife. Do not modify!
package com.zxjk.duoduo.ui.msgpage;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.zxjk.duoduo.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MsgFragment_ViewBinding implements Unbinder {
  private MsgFragment target;

  private View view7f09006e;

  private View view7f09006d;

  @UiThread
  public MsgFragment_ViewBinding(final MsgFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btn_head_right, "method 'showMsgMenuPop'");
    view7f09006e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showMsgMenuPop(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_head_left, "method 'showForContactFragment'");
    view7f09006d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showForContactFragment();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view7f09006e.setOnClickListener(null);
    view7f09006e = null;
    view7f09006d.setOnClickListener(null);
    view7f09006d = null;
  }
}
