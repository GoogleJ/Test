// Generated code from Butter Knife. Do not modify!
package com.zxjk.duoduo.ui.msgpage;

import android.view.View;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.weight.TitleBar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SearchActivity_ViewBinding implements Unbinder {
  private SearchActivity target;

  @UiThread
  public SearchActivity_ViewBinding(SearchActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SearchActivity_ViewBinding(SearchActivity target, View source) {
    this.target = target;

    target.titleBar = Utils.findRequiredViewAsType(source, R.id.m_fragment_search_title_bar, "field 'titleBar'", TitleBar.class);
    target.searchEdit = Utils.findRequiredViewAsType(source, R.id.m_search_edit, "field 'searchEdit'", EditText.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'mRecyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SearchActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.titleBar = null;
    target.searchEdit = null;
    target.mRecyclerView = null;
  }
}
