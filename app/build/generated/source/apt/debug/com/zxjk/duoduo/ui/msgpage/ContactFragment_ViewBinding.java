// Generated code from Butter Knife. Do not modify!
package com.zxjk.duoduo.ui.msgpage;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.msgpage.widget.IndexView;
import com.zxjk.duoduo.weight.TitleBar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ContactFragment_ViewBinding implements Unbinder {
  private ContactFragment target;

  @UiThread
  public ContactFragment_ViewBinding(ContactFragment target, View source) {
    this.target = target;

    target.titleBar = Utils.findRequiredViewAsType(source, R.id.m_contact_title_bar, "field 'titleBar'", TitleBar.class);
    target.textView = Utils.findRequiredViewAsType(source, R.id.previewText, "field 'textView'", TextView.class);
    target.indexView = Utils.findRequiredViewAsType(source, R.id.index_view, "field 'indexView'", IndexView.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.m_contact_recycler_view, "field 'mRecyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ContactFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.titleBar = null;
    target.textView = null;
    target.indexView = null;
    target.mRecyclerView = null;
  }
}
