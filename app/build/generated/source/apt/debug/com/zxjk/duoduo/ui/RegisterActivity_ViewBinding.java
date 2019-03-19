// Generated code from Butter Knife. Do not modify!
package com.zxjk.duoduo.ui;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class RegisterActivity_ViewBinding implements Unbinder {
  private RegisterActivity target;

  private View view7f090060;

  private View view7f0900db;

  private View view7f090116;

  private View view7f090168;

  private View view7f090165;

  private View view7f0900dc;

  @UiThread
  public RegisterActivity_ViewBinding(RegisterActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public RegisterActivity_ViewBinding(final RegisterActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btn_register, "field 'btn_register' and method 'onClick'");
    target.btn_register = Utils.castView(view, R.id.btn_register, "field 'btn_register'", Button.class);
    view7f090060 = view;
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
    view = Utils.findRequiredView(source, R.id.register_code, "field 'register_code' and method 'onClick'");
    target.register_code = Utils.castView(view, R.id.register_code, "field 'register_code'", TextView.class);
    view7f090116 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.text_user_agreement, "field 'user_agreement' and method 'agreement'");
    target.user_agreement = Utils.castView(view, R.id.text_user_agreement, "field 'user_agreement'", CheckBox.class);
    view7f090168 = view;
    ((CompoundButton) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton p0, boolean p1) {
        target.agreement(Utils.castParam(p0, "onCheckedChanged", 0, "agreement", 0, CheckBox.class), p1);
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
    target.edit_register_code = Utils.findRequiredViewAsType(source, R.id.edit_register_code, "field 'edit_register_code'", EditText.class);
    target.edit_password = Utils.findRequiredViewAsType(source, R.id.edit_password, "field 'edit_password'", EditText.class);
    view = Utils.findRequiredView(source, R.id.login_country_bottom, "method 'onClick'");
    view7f0900dc = view;
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
    RegisterActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.btn_register = null;
    target.login_country = null;
    target.register_code = null;
    target.user_agreement = null;
    target.go_login = null;
    target.edit_mobile = null;
    target.edit_register_code = null;
    target.edit_password = null;

    view7f090060.setOnClickListener(null);
    view7f090060 = null;
    view7f0900db.setOnClickListener(null);
    view7f0900db = null;
    view7f090116.setOnClickListener(null);
    view7f090116 = null;
    ((CompoundButton) view7f090168).setOnCheckedChangeListener(null);
    view7f090168 = null;
    view7f090165.setOnClickListener(null);
    view7f090165 = null;
    view7f0900dc.setOnClickListener(null);
    view7f0900dc = null;
  }
}
