package com.zxjk.duoduo.ui.base;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.trello.rxlifecycle3.components.support.RxFragment;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.rx.RxException;
import com.zxjk.duoduo.utils.DensityUtils;
import java.io.File;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * @author Administrator
 * @// TODO: 2019\3\18 0018  关于Fragment的基类 
 */
public class BaseFragment extends RxFragment {


    private View rootView;
    protected Fragment mCurrentFragment;
    private static final String CURRENT_FRAGMENT_KEY = "current_fragment_key";
    public void handleApiError(Throwable throwable) {
        if (throwable instanceof RxException.DuplicateLoginExcepiton) {
            //TODO code601，后续考虑如何处理(弹对话框、强制退出)
            LogUtils.e("已经登录，提示用户退出");
            return;
        }

        ToastUtils.showShort(RxException.getMessage(throwable));
    }


    public void zipFile(List<String> files, BaseActivity.OnZipFileFinish onFinish) {
        Observable.just(files)
                .observeOn(Schedulers.io())
                .map(origin -> Luban.with(Utils.getApp()).load(origin).get())
                .observeOn(AndroidSchedulers.mainThread())
//                .compose(bindToLifecycle())
                .subscribe(result -> {
                    if (onFinish != null) {
                        onFinish.onFinish(result);
                    }
                });
    }
    public interface OnZipFileFinish {
        void onFinish(List<File> result);
    }












    protected <T extends Fragment> T getFragment(String tag) {
        return (T) getChildFragmentManager()
                .findFragmentByTag(tag);
    }
    protected Fragment getCurrentFragment(Bundle bundle) {
        String currentFragmentTag = bundle.getString(CURRENT_FRAGMENT_KEY, CURRENT_FRAGMENT_KEY);
        return getFragment(currentFragmentTag);
    }
    protected void switchFragment(Fragment targetFragment, int contentId) {

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (targetFragment == mCurrentFragment) {
            return;
        }

        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
        }

        if (!targetFragment.isAdded()) {
            transaction.add(contentId, targetFragment, targetFragment.getClass().getSimpleName());
        } else {
            transaction.show(targetFragment);
        }

        transaction.commitAllowingStateLoss();

        mCurrentFragment = targetFragment;

    }

}
