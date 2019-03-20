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

public class VerificationFragment_ViewBinding implements Unbinder {
  private VerificationFragment target;

  private View view7f09018c;

  private View view7f09018e;

  @UiThread
  public VerificationFragment_ViewBinding(final VerificationFragment target, View source) {
    this.target = target;

    View view;
    target.titleBar = Utils.findRequiredViewAsType(source, R.id.m_verification_title_bar, "field 'titleBar'", TitleBar.class);
    view = Utils.findRequiredView(source, R.id.m_verification_icon, "method 'onClick'");
    view7f09018c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.m_verification_send_btn, "method 'onClick'");
    view7f09018e = view;
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
    VerificationFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.titleBar = null;

    view7f09018c.setOnClickListener(null);
    view7f09018c = null;
    view7f09018e.setOnClickListener(null);
    view7f09018e = null;
  }
}
