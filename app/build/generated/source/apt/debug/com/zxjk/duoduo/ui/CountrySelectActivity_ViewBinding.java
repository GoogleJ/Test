// Generated code from Butter Knife. Do not modify!
package com.zxjk.duoduo.ui;

import android.view.View;
import android.widget.ListView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.zxjk.duoduo.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CountrySelectActivity_ViewBinding implements Unbinder {
  private CountrySelectActivity target;

  @UiThread
  public CountrySelectActivity_ViewBinding(CountrySelectActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CountrySelectActivity_ViewBinding(CountrySelectActivity target, View source) {
    this.target = target;

    target.lv_list = Utils.findRequiredViewAsType(source, R.id.lv_list, "field 'lv_list'", ListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CountrySelectActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.lv_list = null;
  }
}
