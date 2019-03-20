// Generated code from Butter Knife. Do not modify!
package com.zxjk.duoduo.ui.msgpage;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.weight.TitleBar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyQrCodeActivity_ViewBinding implements Unbinder {
  private MyQrCodeActivity target;

  @UiThread
  public MyQrCodeActivity_ViewBinding(MyQrCodeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MyQrCodeActivity_ViewBinding(MyQrCodeActivity target, View source) {
    this.target = target;

    target.titleBar = Utils.findRequiredViewAsType(source, R.id.m_qr_code_title_bar_me, "field 'titleBar'", TitleBar.class);
    target.qrCode = Utils.findRequiredViewAsType(source, R.id.m_my_qr_code_qr_code_icon, "field 'qrCode'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyQrCodeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.titleBar = null;
    target.qrCode = null;
  }
}
