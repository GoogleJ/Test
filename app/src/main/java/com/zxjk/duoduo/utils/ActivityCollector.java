package com.zxjk.duoduo.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @// TODO: 2019\3\25 0025  管理活动页面 
 */
public class ActivityCollector {
    private ActivityCollector() {}

    private static List<Activity> actList = new ArrayList<>();

    public static void addActivity(Activity act) {
        actList.add(act);
    }

    public static void removeActivity(Activity act) {
        actList.remove(act);
    }

    public static void finishAll() {
        for (Activity act : actList) {
            if (!act.isFinishing()) {
                act.finish();
            }
        }
    }
}
