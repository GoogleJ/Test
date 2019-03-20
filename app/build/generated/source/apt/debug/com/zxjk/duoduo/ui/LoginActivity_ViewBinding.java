// Generated code from Butter Knife. Do not modify!
package com.zxjk.duoduo.ui;

import android.view.View;
import android.widget.Button;
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

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  private View view7f09006f;

  private View view7f090122;

  private View view7f090303;

  private View view7f090305;

  private View view7f09007d;

  private View view7f090123;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(final LoginActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btn_login, "field 'btn_login' and method 'onClick'");
    target.btn_login = Utils.castView(view, R.id.btn_login, "field 'btn_login'", Button.class);
    view7f09006f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.login_country, "field 'login_country' and method 'onClick'");
    target.login_country = Utils.castView(view, R.id.login_country, "field 'login_country'", TextView.class);
    view7f090122 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.text_forget_password, "field 'forget_password' and method 'onClick'");
    target.forget_password = Utils.castView(view, R.id.text_forget_password, "field 'forget_password'", TextView.class);
    view7f090303 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.text_go_register, "field 'go_register' and method 'onClick'");
    target.go_register = Utils.castView(view, R.id.text_go_register, "field 'go_register'", TextView.class);
    view7f090305 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.edit_mobile = Utils.findRequiredViewAsType(source, R.id.edit_mobile, "field 'edit_mobile'", EditText.class);
    target.edit_password = Utils.findRequiredViewAsType(source, R.id.edit_password, "field 'edit_password'", EditText.class);
    view = Utils.findRequiredView(source, R.id.change_language, "method 'onClick'");
    view7f09007d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.login_country_bottom, "method 'onClick'");
    view7f090123 = view;
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
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.btn_login = null;
    target.login_country = null;
    target.forget_password = null;
    target.go_register = null;
    target.edit_mobile = null;
    target.edit_password = null;

    view7f09006f.setOnClickListener(null);
    view7f09006f = null;
    view7f090122.setOnClickListener(null);
    view7f090122 = null;
    view7f090303.setOnClickListener(null);
    view7f090303 = null;
    view7f090305.setOnClickListener(null);
    view7f090305 = null;
    view7f09007d.setOnClickListener(null);
    view7f09007d = null;
    view7f090123.setOnClickListener(null);
    view7f090123 = null;
  }
}
