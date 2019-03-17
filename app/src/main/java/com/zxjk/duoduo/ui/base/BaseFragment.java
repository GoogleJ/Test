package com.zxjk.duoduo.ui.base;

import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle3.components.RxFragment;
import com.zxjk.duoduo.network.rx.RxException;

public class BaseFragment extends RxFragment {

    public void handleApiError(Throwable throwable) {
        if (throwable instanceof RxException.DuplicateLoginExcepiton) {
            //TODO code601，后续考虑如何处理(弹对话框、强制退出)
            return;
        }

        ToastUtils.showShort(throwable.getMessage());
    }
}
