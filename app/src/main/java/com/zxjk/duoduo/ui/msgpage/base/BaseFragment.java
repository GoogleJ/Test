package com.zxjk.duoduo.ui.msgpage.base;

import android.os.Bundle;

import com.trello.rxlifecycle3.components.support.RxFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class BaseFragment extends RxFragment {
    private static final String CURRENT_FRAGMENT_KEY = "current_fragment_key";
    protected Fragment mCurrentFragment;
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
