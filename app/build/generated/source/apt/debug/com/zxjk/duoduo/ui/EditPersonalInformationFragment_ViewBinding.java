// Generated code from Butter Knife. Do not modify!
package com.zxjk.duoduo.ui;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.zxjk.duoduo.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EditPersonalInformationFragment_ViewBinding implements Unbinder {
  private EditPersonalInformationFragment target;

  private View view7f09015e;

  @UiThread
  public EditPersonalInformationFragment_ViewBinding(EditPersonalInformationFragment target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public EditPersonalInformationFragment_ViewBinding(final EditPersonalInformationFragment target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.m_edit_information_btn, "method 'onClick'");
    view7f09015e = view;
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
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view7f09015e.setOnClickListener(null);
    view7f09015e = null;
  }
}
