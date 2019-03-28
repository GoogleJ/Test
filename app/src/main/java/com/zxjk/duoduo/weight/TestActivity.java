package com.zxjk.duoduo.weight;

import android.os.Bundle;
import android.view.View;

import com.trello.rxlifecycle3.components.RxActivity;
import com.zxjk.duoduo.R;

import androidx.annotation.Nullable;

/**
 * @author Administrator
 * @// TODO: 2019\3\27 0027 测试
 */
public class TestActivity extends RxActivity {
    /**
     * 支付密码验证
     */
    PwdEditText pwdVerified;
    /**
     * 安全键盘的弹框
     */
    private KeyboardPopupWindow keyboardPopupWindow;
    private boolean isUiCreated = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        initView();
    }

    //    private void initView() {
//        pwdVerified=findViewById(R.id.pwd_verified);
//        keyboardPopupWindow = new KeyboardPopupWindow(TestActivity.this, getWindow().getDecorView(), pwdVerified, false);
//        pwdVerified.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (keyboardPopupWindow != null) {
//                    keyboardPopupWindow.show();
//                }
//            }
//        });
//        pwdVerified.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (keyboardPopupWindow != null && isUiCreated) {
//                    keyboardPopupWindow.refreshKeyboardOutSideTouchable(!hasFocus);
//                }
//                //隐藏系统软键盘
//                if (hasFocus) {
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(pwdVerified.getWindowToken(), 0);
//                }
//            }
//        });
//    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (keyboardPopupWindow != null && keyboardPopupWindow.isShowing()) {
//                keyboardPopupWindow.dismiss();
//                return true;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        isUiCreated = true;
//    }
//    @Override
//    protected void onDestroy() {
//        if (keyboardPopupWindow != null) {
//            keyboardPopupWindow.releaseResources();
//        }
//        super.onDestroy();
//    }
    public void test(View view) {
//        PayPwdDialog pwdDialog=new PayPwdDialog(this);
//        pwdDialog.setOnTextChangeListener(new PayPwdDialog.OnTextChangeListener() {
//            @Override
//            public void getPwd(String pwd) {
//                ToastUtils.showShort(pwd);
//            }
//
//        });
//        pwdDialog.show();
//        KeyboardPopupWindowTst popupWindowTst = new KeyboardPopupWindowTst(this,  getWindow().getDecorView(), true);
//        popupWindowTst.show();
    }
}
