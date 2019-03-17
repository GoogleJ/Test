package com.zxjk.duoduo.ui.base;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.zxjk.duoduo.network.rx.RxException;

public class BaseActivity extends RxAppCompatActivity {

    public void handleApiError(Throwable throwable) {
        if (throwable instanceof RxException.DuplicateLoginExcepiton) {
            //TODO code601，后续考虑如何处理(弹对话框、强制退出)
            LogUtils.e("已经登录，提示用户退出");
            return;
        }

        ToastUtils.showShort(throwable.getMessage());
    }

}
