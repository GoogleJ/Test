// Generated code from Butter Knife. Do not modify!
package com.zxjk.duoduo.ui;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.zxjk.duoduo.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChangeLanguageActivity_ViewBinding implements Unbinder {
  private ChangeLanguageActivity target;

  private View view7f090080;

  private View view7f090081;

  private View view7f09007f;

  @UiThread
  public ChangeLanguageActivity_ViewBinding(ChangeLanguageActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ChangeLanguageActivity_ViewBinding(final ChangeLanguageActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.checkbox_zh_cn, "field 'checkBoxZhCn' and method 'onClick'");
    target.checkBoxZhCn = Utils.castView(view, R.id.checkbox_zh_cn, "field 'checkBoxZhCn'", ImageView.class);
    view7f090080 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.checkbox_zh_tw, "field 'checkBoxZhTw' and method 'onClick'");
    target.checkBoxZhTw = Utils.castView(view, R.id.checkbox_zh_tw, "field 'checkBoxZhTw'", ImageView.class);
    view7f090081 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.checkbox_en_us, "field 'checkBoxEnUs' and method 'onClick'");
    target.checkBoxEnUs = Utils.castView(view, R.id.checkbox_en_us, "field 'checkBoxEnUs'", ImageView.class);
    view7f09007f = view;
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
    ChangeLanguageActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.checkBoxZhCn = null;
    target.checkBoxZhTw = null;
    target.checkBoxEnUs = null;

    view7f090080.setOnClickListener(null);
    view7f090080 = null;
    view7f090081.setOnClickListener(null);
    view7f090081 = null;
    view7f09007f.setOnClickListener(null);
    view7f09007f = null;
  }
}
