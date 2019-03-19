// Generated code from Butter Knife. Do not modify!
package com.zxjk.duoduo.ui;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.zxjk.duoduo.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ForgetRegisterActivity_ViewBinding implements Unbinder {
  private ForgetRegisterActivity target;

  private View view7f09005d;

  private View view7f0900db;

  private View view7f0900f7;

  private View view7f090168;

  private View view7f090165;

  @UiThread
  public ForgetRegisterActivity_ViewBinding(ForgetRegisterActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ForgetRegisterActivity_ViewBinding(final ForgetRegisterActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btn_commit, "field 'btn_commit' and method 'onClick'");
    target.btn_commit = Utils.castView(view, R.id.btn_commit, "field 'btn_commit'", Button.class);
    view7f09005d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.login_country, "field 'login_country' and method 'onClick'");
    target.login_country = Utils.castView(view, R.id.login_country, "field 'login_country'", TextView.class);
    view7f0900db = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.mobile_code, "field 'mobile_code' and method 'onClick'");
    target.mobile_code = Utils.castView(view, R.id.mobile_code, "field 'mobile_code'", TextView.class);
    view7f0900f7 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.text_user_agreement, "field 'user_agreement' and method 'onClick'");
    target.user_agreement = Utils.castView(view, R.id.text_user_agreement, "field 'user_agreement'", CheckBox.class);
    view7f090168 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.text_go_login, "field 'go_login' and method 'onClick'");
    target.go_login = Utils.castView(view, R.id.text_go_login, "field 'go_login'", TextView.class);
    view7f090165 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.edit_mobile = Utils.findRequiredViewAsType(source, R.id.edit_mobile, "field 'edit_mobile'", EditText.class);
    target.edit_mobile_code = Utils.findRequiredViewAsType(source, R.id.edit_mobile_code, "field 'edit_mobile_code'", EditText.class);
    target.edit_password = Utils.findRequiredViewAsType(source, R.id.edit_password, "field 'edit_password'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ForgetRegisterActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.btn_commit = null;
    target.login_country = null;
    target.mobile_code = null;
    target.user_agreement = null;
    target.go_login = null;
    target.edit_mobile = null;
    target.edit_mobile_code = null;
    target.edit_password = null;

    view7f09005d.setOnClickListener(null);
    view7f09005d = null;
    view7f0900db.setOnClickListener(null);
    view7f0900db = null;
    view7f0900f7.setOnClickListener(null);
    view7f0900f7 = null;
    view7f090168.setOnClickListener(null);
    view7f090168 = null;
    view7f090165.setOnClickListener(null);
    view7f090165 = null;
  }
}
