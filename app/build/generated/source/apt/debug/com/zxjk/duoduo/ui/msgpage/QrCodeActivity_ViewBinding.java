// Generated code from Butter Knife. Do not modify!
package com.zxjk.duoduo.ui.msgpage;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.weight.TitleBar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class QrCodeActivity_ViewBinding implements Unbinder {
  private QrCodeActivity target;

  @UiThread
  public QrCodeActivity_ViewBinding(QrCodeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public QrCodeActivity_ViewBinding(QrCodeActivity target, View source) {
    this.target = target;

    target.zxingview = Utils.findRequiredViewAsType(source, R.id.m_qr_code_zxing_view, "field 'zxingview'", ZXingView.class);
    target.titleBar = Utils.findRequiredViewAsType(source, R.id.m_qr_code_title_bar, "field 'titleBar'", TitleBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    QrCodeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.zxingview = null;
    target.titleBar = null;
  }
}
