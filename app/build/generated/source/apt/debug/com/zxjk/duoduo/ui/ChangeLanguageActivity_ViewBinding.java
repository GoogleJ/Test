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

  private View view7f09006d;

  private View view7f09006e;

  private View view7f09006c;

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
    view7f09006d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.checkbox_zh_tw, "field 'checkBoxZhTw' and method 'onClick'");
    target.checkBoxZhTw = Utils.castView(view, R.id.checkbox_zh_tw, "field 'checkBoxZhTw'", ImageView.class);
    view7f09006e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.checkbox_en_us, "field 'checkBoxEnUs' and method 'onClick'");
    target.checkBoxEnUs = Utils.castView(view, R.id.checkbox_en_us, "field 'checkBoxEnUs'", ImageView.class);
    view7f09006c = view;
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

    view7f09006d.setOnClickListener(null);
    view7f09006d = null;
    view7f09006e.setOnClickListener(null);
    view7f09006e = null;
    view7f09006c.setOnClickListener(null);
    view7f09006c = null;
  }
}
