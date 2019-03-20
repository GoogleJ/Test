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

public class PersonalInformationFragment_ViewBinding implements Unbinder {
  private PersonalInformationFragment target;

  private View view7f09017b;

  @UiThread
  public PersonalInformationFragment_ViewBinding(final PersonalInformationFragment target,
      View source) {
    this.target = target;

    View view;
    target.titleBar = Utils.findRequiredViewAsType(source, R.id.m_information_title_bar, "field 'titleBar'", TitleBar.class);
    view = Utils.findRequiredView(source, R.id.m_personal_information_add_contact_btn, "method 'onClick'");
    view7f09017b = view;
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
    PersonalInformationFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.titleBar = null;

    view7f09017b.setOnClickListener(null);
    view7f09017b = null;
  }
}
